package in.jiyofit.the_app;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class SettingsActivity extends BaseActivity{
    Bundle bundle;
    private SettingsFragment settingsFragment;
    private RelativeLayout prefLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, contentFrameLayout);
        prefLayout = (RelativeLayout) findViewById(R.id.asettings_layout_preference);
    }

    public void callSettingPreference (View v) {
        String xmlName = v.getTag().toString();
        bundle = new Bundle();
        bundle.putString("prefFragment", xmlName);
        settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(bundle);
        prefLayout.setVisibility(View.VISIBLE);
        getFragmentManager().beginTransaction().replace(R.id.asettings_layout_overlaycontainer, settingsFragment).commit();
    }

    public void closePreferenceFragment (View v){
        prefLayout.setVisibility(View.GONE);
    }
}
