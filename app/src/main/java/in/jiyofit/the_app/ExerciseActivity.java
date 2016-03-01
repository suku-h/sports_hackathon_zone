package in.jiyofit.the_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ExerciseActivity extends BaseActivity {
    ImageButton ibSprint, ibSwimming, ibStrength, ibCycling;
    SuggestionFragment suggestionFragment;
    View.OnClickListener exerciseClick;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_exercise, contentFrameLayout);

        ibSprint = (ImageButton) findViewById(R.id.sprint);
        ibSwimming = (ImageButton) findViewById(R.id.swimming);
        ibStrength = (ImageButton) findViewById(R.id.strength);
        ibCycling = (ImageButton) findViewById(R.id.cycling);

        ibSprint.setImageResource(R.drawable.sprint_icon);
        ibSprint.setTag("sprint");
        ibSwimming.setImageResource(R.drawable.swim_icon);
        ibSwimming.setTag("swimming");
        ibStrength.setImageResource(R.drawable.strength_icon);
        ibStrength.setTag("strength");
        ibCycling.setImageResource(R.drawable.cycling_icon);
        ibCycling.setTag("cycling");

        exerciseClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestionFragment = new SuggestionFragment();
                getFragmentManager().beginTransaction().add(R.id.exercise_layout, suggestionFragment, "suggestionFragment").commit();
            }
        };

        ibSprint.setOnClickListener(exerciseClick);
        ibSwimming.setOnClickListener(exerciseClick);
        ibStrength.setOnClickListener(exerciseClick);
        ibCycling.setOnClickListener(exerciseClick);
    }


}
