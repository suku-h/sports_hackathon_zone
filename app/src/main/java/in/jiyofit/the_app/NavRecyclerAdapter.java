package in.jiyofit.the_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NavRecyclerAdapter extends RecyclerView.Adapter<NavRecyclerAdapter.NavRecyclerViewHolder>{
    private LayoutInflater layoutInflater;
    private List<NavReel> data = Collections.emptyList();
    private NavItemClickListener navItemClickListener;
    private Context ctx;

    public NavRecyclerAdapter (Context ctx){
        layoutInflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        data = getNavData();
    }

    @Override
    public NavRecyclerAdapter.NavRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.nav_row, parent, false);
        return new NavRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NavRecyclerAdapter.NavRecyclerViewHolder holder, int position) {
        NavReel current = data.get(position);
        holder.activityTextView.setText(current.buttonName);
        holder.activityImage.setImageResource(current.navImageViewID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //cannot be static as it has to take reference of the context above
    class NavRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView activityTextView;
        ImageView activityImage;

        public NavRecyclerViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            activityTextView = (TextView) itemView.findViewById(R.id.rnav_tv);
            activityImage = (ImageView) itemView.findViewById(R.id.rnav_iv);
        }

        @Override
        public void onClick(View v) {
            if(navItemClickListener!=null){
                navItemClickListener.NavItemClick(v, data.get(getAdapterPosition()).activityName);
            }
        }
    }

    protected interface NavItemClickListener {
        void NavItemClick(View view, String activityName);
    }

    public void setNavItemClickListener (NavItemClickListener navListener){
        this.navItemClickListener = navListener;
    }

    private static class NavReel {
        String activityName;
        int navImageViewID;
        String buttonName;
    }

    public List<NavReel> getNavData(){
        List<NavReel> data = new ArrayList<>();
        String[] temparr1 = ctx.getResources().getStringArray(R.array.activity_names);
        String[] activityName = Arrays.copyOfRange(temparr1, 1, temparr1.length);
        String[] temparr2 = ctx.getResources().getStringArray(R.array.menu);
        String[] menu = Arrays.copyOfRange(temparr2, 1, temparr2.length);
        int[] recyclerImageID = {R.drawable.workout_icon, R.drawable.statboard,
                                 R.drawable.performance_icon, R.drawable.social_icon, R.drawable.profile_icon};
        //this is for each type of loop
        for (int i = 0; i < activityName.length; i++) {
            NavReel current = new NavReel();
            current.activityName = activityName[i];
            current.navImageViewID = recyclerImageID[i];
            current.buttonName = menu[i];
            data.add(current);
        }
        return data;
    }
}
