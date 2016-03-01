package in.jiyofit.the_app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    private int resID;
    private String prefName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        prefName = args.getString("prefFragment");
        resID = getResources().getIdentifier(prefName, "xml", getActivity().getPackageName());
        addPreferencesFromResource(resID);
    }
}
