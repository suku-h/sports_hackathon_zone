package in.jiyofit.the_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.StatBoardViewHolder> {
    private LayoutInflater layoutInflater;
    ImageLoader imageLoader;
    Context ctx;
    private ArrayList<Stat> statBoardRecyclerList = new ArrayList<Stat>();
    FBClickListener fbClickListener;

    public StatAdapter(Context ctx) {
        this.ctx = ctx;
        layoutInflater = LayoutInflater.from(ctx);
        //to get the image for profile_picture
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setStatList(ArrayList<Stat> statList) {
        this.statBoardRecyclerList = statList;
        //update the adapter to reflect the new set of statlist
        notifyDataSetChanged();
    }


    @Override
    public StatBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.statboard_row, parent, false);
        return new StatBoardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StatBoardViewHolder holder, int position) {
        final Stat currentStat = statBoardRecyclerList.get(position);
        holder.tvStatText.setText(currentStat.getStatText());
        holder.tvStatDate.setText(android.text.format.DateFormat.format("dd MMM, yy", currentStat.getStatDate()).toString());
        if(currentStat.getStatType().equals("image")){
            holder.ivStatBoard.setVisibility(View.VISIBLE);
            String statImage = currentStat.getStatImageURL();
            imageLoader.get(statImage, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.ivStatBoard.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
        holder.celebrateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Shout Out your achievement to your online Fitness Community", Toast.LENGTH_LONG).show();
            }
        });
        holder.fbShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentStat.getStatType().equals("image")){
                    fbClickListener.FBClick(currentStat.getStatImageURL(), currentStat.getStatText());
                } else {
                    fbClickListener.FBClick(null, currentStat.getStatText());
                }
            }
        });
        holder.tweetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Tweet the success", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return statBoardRecyclerList.size();
    }

    static class StatBoardViewHolder extends RecyclerView.ViewHolder {
        ImageView ivStatBoard;
        TextView tvStatText, tvStatDate;
        ImageButton celebrateBtn;
        Button fbShareBtn, tweetBtn;
        public StatBoardViewHolder(View itemView) {
            super(itemView);

            ivStatBoard = (ImageView) itemView.findViewById(R.id.rstatboard_iv);
            tvStatText = (TextView) itemView.findViewById(R.id.rstatboard_tv_stat);
            tvStatDate = (TextView) itemView.findViewById(R.id.rstatboard_tv_date);
            celebrateBtn = (ImageButton) itemView.findViewById(R.id.rstatboard_ib);
            fbShareBtn = (Button) itemView.findViewById(R.id.rstatboard_btn_fb);
            tweetBtn = (Button) itemView.findViewById(R.id.rstatboard_btn_twitter);
        }
    }

    public interface FBClickListener{
        void FBClick(String badgeURL, String statPost);
    }

    public void setFBClickListener (FBClickListener fbListener){
        this.fbClickListener = fbListener;
    }
}
