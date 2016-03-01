package in.jiyofit.the_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFragment extends Fragment implements NumReelAdapter.RepsClickListener,
        NumReelAdapter.WeightClickListener{
    private RecyclerView rvReps, rvWeight;
    private NumReelAdapter repsReelAdapter, weightReelAdapter;
    private Integer repsNum, weightNum;
    Button submitFeedbackBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_feedback, container, false);

        rvReps = (RecyclerView) layout.findViewById(R.id.ffeedback_rv_reps);
        repsReelAdapter = new NumReelAdapter(getActivity(), getRepsData());
        //tells that the adapter implements the listener
        repsReelAdapter.setRepsClickListener(this);
        rvReps.setAdapter(repsReelAdapter);
        rvReps.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvWeight = (RecyclerView) layout.findViewById(R.id.ffeedback_rv_weight);
        weightReelAdapter = new NumReelAdapter(getActivity(), getWeightData());
        weightReelAdapter.setWeightClickListener(this);
        rvWeight.setAdapter(weightReelAdapter);
        rvWeight.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitFeedbackBtn = (Button)getActivity().findViewById(R.id.ffeedback_btn_submit);
        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( repsNum == null) {
                    Toast.makeText(getActivity(), "Please select the duration of the session", Toast.LENGTH_SHORT).show();
                } else {
                    if ( weightNum == null) {
                        Toast.makeText(getActivity(), "Please selecthow instense was the session", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "The load for the session was "+ repsNum * weightNum, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "This load is more than the recommended. Please do lighter exercise next day",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public static List<NumReel> getWeightData(){
            List<NumReel> data = new ArrayList<>();
            int[] numbers = {20,30,40,50,60,70,80,90,100};
            //this is for each type of loop
            for (int number : numbers) {
                NumReel current = new NumReel();
                current.num = number;
                current.numType = "weight";
                data.add(current);
            }
            return data;
        }

        public static List<NumReel> getRepsData(){
            List<NumReel> data = new ArrayList<>();
            int[] numbers = {1,2,3,5,5,6,7,8,9,10};
            //this is for each type of loop
            for (int number : numbers) {
                NumReel current = new NumReel();
                current.num = number;
                current.numType = "reps";
                data.add(current);
            }
            return data;
        }

        @Override
        public void repsClick(View view, int position) {
            repsNum = repsReelAdapter.data.get(position).num;
        }

        @Override
        public void weightClick(View view, int position) {
            weightNum = weightReelAdapter.data.get(position).num;
        }

}
