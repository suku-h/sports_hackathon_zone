package in.jiyofit.the_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SuggestionFragment extends Fragment {
    String exerciseName;
    TextView tv1, tv2, tv3;
    ImageView iv;
    ImageButton y, n, PLAY, PAUSE, STOP;
    LinearLayout l1, l2;
    Button RECORD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        exerciseName = "cycling";
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv1 = (TextView) getActivity().findViewById(R.id.tv_suggestion);
        tv2 = (TextView) getActivity().findViewById(R.id.tv_distance);
        tv3 = (TextView) getActivity().findViewById(R.id.tv_duration);
        iv = (ImageView) getActivity().findViewById(R.id.iv_suggestion);

        if(exerciseName.equals("cycling")){
            tv1.setText("This cycling route should provide load of 700");
            tv2.setText("Distance: 30km");
            tv3.setText("Duration: 1hr 10mins");
            iv.setImageResource(R.drawable.cycling_route);
        }

        y = (ImageButton) getActivity().findViewById(R.id.accept);
        n = (ImageButton) getActivity().findViewById(R.id.reject);
        l1 = (LinearLayout) getActivity().findViewById(R.id.acceptreject);
        l2 = (LinearLayout) getActivity().findViewById(R.id.playpausestop);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.VISIBLE);
            }
        });

        PLAY = (ImageButton) getActivity().findViewById(R.id.play);
        PAUSE = (ImageButton) getActivity().findViewById(R.id.pause);
        STOP = (ImageButton) getActivity().findViewById(R.id.stop);
        RECORD = (Button) getActivity().findViewById(R.id.record);

        PLAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PLAY.setVisibility(View.GONE);
                PAUSE.setVisibility(View.VISIBLE);
                STOP.setVisibility(View.VISIBLE);
                RECORD.setVisibility(View.GONE);
            }
        });

        PAUSE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PAUSE.setVisibility(View.GONE);
                PLAY.setVisibility(View.VISIBLE);
                STOP.setVisibility(View.VISIBLE);
                RECORD.setVisibility(View.GONE);
            }
        });

        STOP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STOP.setVisibility(View.GONE);
                PAUSE.setVisibility(View.GONE);
                PLAY.setVisibility(View.VISIBLE);
                RECORD.setVisibility(View.VISIBLE);
            }
        });

        RECORD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackFragment feedbackFragment = new FeedbackFragment();
                getFragmentManager().beginTransaction().replace(R.id.exercise_layout, feedbackFragment, "feedbackFragment").commit();
            }
        });

    }
}
