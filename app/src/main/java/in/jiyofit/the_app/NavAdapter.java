package in.jiyofit.the_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class NavAdapter extends BaseAdapter {
    private Context context;
    String[] menu;
    int[] drawerImages = {R.drawable.home_icon, R.drawable.workout_icon, R.drawable.statboard,
                            R.drawable.performance_icon, R.drawable.social_icon, R.drawable.profile_icon};

    public NavAdapter (Context context){
        this.context = context;
        menu = context.getResources().getStringArray(R.array.menu);
    }
    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return menu[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null){
            //this means that the view is being created for the first time, hence needs layout inflater
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.drawer_row, parent, false);
        } else {
            row = convertView;
        }
        TextView drawerTV = (TextView) row.findViewById(R.id.rdrawer_tv);
        ImageView drawerIV = (ImageView) row.findViewById(R.id.rdrawer_iv);
        drawerTV.setText(menu[position]);
        drawerIV.setImageResource(drawerImages[position]);
        return row;
    }
}
