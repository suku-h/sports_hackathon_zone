package in.jiyofit.the_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class NumReelAdapter extends RecyclerView.Adapter<NumReelAdapter.recyclerViewHolder> {

    private LayoutInflater inflater;
    private Context ctx;
    List<NumReel> data = Collections.emptyList();
    private RepsClickListener repsClickListener;
    private WeightClickListener weightClickListener;

    public NumReelAdapter(Context context, List<NumReel> data){
        inflater = LayoutInflater.from(context);
        this.ctx = context;
        this.data = data;
    }

    @Override
    public recyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.number_row, parent, false);
        return new recyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(recyclerViewHolder holder, final int position) {
        final NumReel current = data.get(position);
        holder.number.setText(String.valueOf(current.num));
        //when the Adapter is reused, then a tag has to be attached with the data (NumReel) that differentiates
        holder.vItemView.setTag(current.numType);
        /*
        To set onClickListener in the viewholder, I would have to send NumReel to the ViewHolder
        and then setTag. It would be quite messy. Instead access to data is available in onBindViewHolder.
        Hence, setting up tag like you showed is much easier. Hence, when adapter is reused,
        the onclicklistener should be set in the onBindViewHolder
         */
        holder.vItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("reps")){
                    if(repsClickListener!=null){
                        //repsClickListener.repsClick(v, position); alternate method where data can't be sent
                        repsClickListener.repsClick(v, current.num);
                    }
                } else {
                    if(weightClickListener!=null){
                        weightClickListener.weightClick(v, current.num);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class recyclerViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        View vItemView;

        //this is the constructor for the viewholder class
        public recyclerViewHolder(View itemView) {
            super(itemView);
            //from number_row.xml, the entire view is also used
            number = (TextView) itemView.findViewById(R.id.rnumber_tv);
            vItemView = itemView;
        }
    }

    public interface RepsClickListener{
        void repsClick(View view, int position);
    }

    public void setRepsClickListener (RepsClickListener repsListener){
        this.repsClickListener = repsListener;
    }

    public interface WeightClickListener{
        void weightClick(View view, int position);
    }

    public void setWeightClickListener (WeightClickListener weightListener){
        this.weightClickListener = weightListener;
    }
}
