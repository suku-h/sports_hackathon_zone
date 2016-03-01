package in.jiyofit.the_app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DayDataFragment extends Fragment{
    ArrayList<String> dayArray;
    TextView dateTV, loadTV, activityTV, sleepTV;
    int hoursSleep, minutesSleep;
    FrameLayout overlayContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day_data, container, false);
        Bundle args = getArguments();
        dayArray = args.getStringArrayList("dayData");
        overlayContainer = (FrameLayout) getActivity().findViewById(R.id.overlaycontainer);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        final int SWIPE_MIN_DISTANCE = 120;
                        final int SWIPE_MAX_OFF_PATH = 250;
                        final int SWIPE_THRESHOLD_VELOCITY = 200;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                overlayContainer.setVisibility(View.GONE);
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                overlayContainer.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTV = (TextView) getActivity().findViewById(R.id.fdaydata_tv_date);
        activityTV = (TextView) getActivity().findViewById(R.id.fdaydata_tv_execise);
        loadTV = (TextView) getActivity().findViewById(R.id.fdaydata_tv_load);
        sleepTV = (TextView) getActivity().findViewById(R.id.fdaydata_tv_sleep);

        hoursSleep = (int)(Double.parseDouble(dayArray.get(2)));
        minutesSleep = (int)(Double.valueOf(dayArray.get(2))*60 - ((int)(Double.parseDouble(dayArray.get(2))))*60);

        dateTV.setText("Date: "+dayArray.get(0));
        activityTV.setText("You did a " + dayArray.get(4) + " and burned " + dayArray.get(5) + " kilocalories");
        loadTV.setText("The load of your exercise was "+dayArray.get(1)+" units performed at "+dayArray.get(6)+"% of average");
        sleepTV.setText("The duration of your sleep was " + hoursSleep + " hrs and " + minutesSleep + " mins. " +
                "The quality of sleep was " + dayArray.get(3));
    }
}
