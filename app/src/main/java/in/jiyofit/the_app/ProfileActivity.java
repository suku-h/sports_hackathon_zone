package in.jiyofit.the_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity implements AdapterView.OnItemSelectedListener {
    SharedPreferences profilePrefs;
    TextView tvHi, tvSelectedItem;
    Spinner ddFeet, ddInches, ddGoal;
    public String sName, sAge, sWeight, sGender;
    EditText etName, etAge, etWeight;
    RadioGroup rgGender;
    RadioButton rbGender;
    public int feetPos, inchesPos, goalPos, check, rbID;
    int iTransparent,iRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        iTransparent = Color.parseColor("#00000000");
        iRed  = Color.RED;

        ddFeet = (Spinner) findViewById(R.id.aprofile_dd_feet);
        ddInches = (Spinner) findViewById(R.id.aprofile_dd_inches);
        ddGoal = (Spinner) findViewById(R.id.aprofile_dd_goal);
        ArrayAdapter aa_feet = ArrayAdapter.createFromResource(this, R.array.feet, android.R.layout.simple_spinner_item);
        ArrayAdapter aa_inches = ArrayAdapter.createFromResource(this, R.array.inches, android.R.layout.simple_spinner_item);
        ArrayAdapter aa_goal = ArrayAdapter.createFromResource(this, R.array.goal, android.R.layout.simple_spinner_item);
        ddFeet.setAdapter(aa_feet);
        ddInches.setAdapter(aa_inches);
        ddGoal.setAdapter(aa_goal);
        ddFeet.setOnItemSelectedListener(this);
        ddInches.setOnItemSelectedListener(this);
        ddGoal.setOnItemSelectedListener(this);

        etAge = (EditText) findViewById(R.id.aprofile_et_age);
        etWeight = (EditText) findViewById(R.id.aprofile_et_weight);
        tvHi = (TextView) findViewById(R.id.aprofile_tv_hi);

        String DEFAULT = "";
        profilePrefs = getSharedPreferences("PData", MODE_PRIVATE);
        if(!profilePrefs.getBoolean("Logged Using Facebook", false)){
            etName = (EditText) findViewById(R.id.aprofile_et_name);
            rgGender = (RadioGroup) findViewById(R.id.aprofile_rg);
            LinearLayout llName = (LinearLayout) findViewById(R.id.aprofile_layout_name);
            llName.setVisibility(View.VISIBLE);
            rgGender.setVisibility(View.VISIBLE);
            etName.setText(profilePrefs.getString("First Name", DEFAULT));
            RadioButton rb_male = (RadioButton) findViewById(R.id.aprofile_rb_male);
            RadioButton rb_female = (RadioButton) findViewById(R.id.aprofile_rb_female);
            if (profilePrefs.getString("Gender", DEFAULT).equals("Male")) {
                rgGender.check(rb_male.getId());
            } else if (profilePrefs.getString("Gender", DEFAULT).equals("Female")) {
                rgGender.check(rb_female.getId());
            } else {
                rgGender.clearCheck();
            }
        }

        String first_name = profilePrefs.getString("First Name", DEFAULT);
        tvHi.setText("Hi " + first_name + ",");
        etAge.setText(profilePrefs.getString("Age", DEFAULT));
        etWeight.setText(profilePrefs.getString("Weight", DEFAULT));
        ddFeet.setSelection(profilePrefs.getInt("Feet Position", 0));
        ddInches.setSelection(profilePrefs.getInt("Inches Position", 0));
        ddGoal.setSelection(profilePrefs.getInt("Fitness Goal Position", 0));

        if (!profilePrefs.getBoolean("profileIsSet", false)) {
            Toast.makeText(this, "Please fill your profile before we proceed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvSelectedItem = (TextView) view;
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.aprofile_dd_feet) {
            feetPos = position;
        }
        if (spinner.getId() == R.id.aprofile_dd_inches) {
            inchesPos = position;
        }
        if (spinner.getId() == R.id.aprofile_dd_goal) {
            goalPos = position;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveProfile(View view) {
        StaticMethods.HideKeypad(this);
        sAge = etAge.getText().toString();
        sWeight = etWeight.getText().toString();

        SharedPreferences.Editor profileEditor = profilePrefs.edit();
        profileEditor.putString("Age", sAge);
        profileEditor.putString("Weight", sWeight);
        profileEditor.putInt("Feet Position", feetPos);
        profileEditor.putInt("Inches Position", inchesPos);
        profileEditor.putInt("Fitness Goal Position", goalPos);
        if(!profilePrefs.getBoolean("Logged Using Facebook", false)) {
            sName = etName.getText().toString();
            profileEditor.putString("First Name", sName);
            rbID = rgGender.getCheckedRadioButtonId();
            if (rbID != -1) {
                rbGender = (RadioButton) findViewById(rbID);
                sGender = rbGender.getText().toString();
            } else {
                sGender = "-1";
            }
            profileEditor.putString("Gender", sGender);
            profileEditor.commit();
        }
    }

    public void updateProfile(View view) {
        //no need to call hidekeypad as this method calls saveProfile which in turn calls hidekeypad
        saveProfile(view);
        check = 0;
        checkEditTextEmpty();
        if(!profilePrefs.getBoolean("Logged Using Facebook", false)) {
            checkRadioEmpty();
        }
        checkSpinnerEmpty();
        SharedPreferences.Editor profileEditor = profilePrefs.edit();
        if (check == 0) {
            profileEditor.putBoolean("profileIsSet", true);
            profileEditor.commit();
            startActivity(new Intent(this, FirstActivity.class));
        } else {
            profileEditor.putBoolean("profileIsSet", false);
            profileEditor.commit();
            Toast.makeText(this, "Please fill your profile before we proceed", Toast.LENGTH_LONG).show();
        }
    }

    public void checkEditTextEmpty() {
        etAge.setBackgroundColor(iTransparent);
        etWeight.setBackgroundColor(iTransparent);
        //First name is bull only if edittext never had data
        //If existing data is deleted, or a data is entered and deleted before saving, sName!=null.
        if(!profilePrefs.getBoolean("Logged Using Facebook", false)){
            etName.setBackgroundColor(iTransparent);
            if (sName.length()==0) {
                etName.setBackgroundColor(iRed);
                check = 1;
            }
        }
        if (sAge.length()==0) {
            etAge.setBackgroundColor(iRed);
            check = 1;
        }
        if (sWeight.length()==0) {
            etWeight.setBackgroundColor(iRed);
            check = 1;
        }
    }

    public void checkRadioEmpty() {
        rgGender.setBackgroundColor(iTransparent);
        if (rgGender.getCheckedRadioButtonId() == -1) {
            rgGender.setBackgroundColor(iRed);
            check = 1;
        }
    }

    public void checkSpinnerEmpty() {
        ddFeet.setBackgroundColor(iTransparent);
        ddInches.setBackgroundColor(iTransparent);
        ddGoal.setBackgroundColor(iTransparent);
        if (feetPos == 0) {
            ddFeet.setBackgroundColor(iRed);
            check = 1;
        }
        if (inchesPos == 0) {
            ddInches.setBackgroundColor(iRed);
            check = 1;
        }
        if (goalPos == 0) {
            ddGoal.setBackgroundColor(iRed);
            check = 1;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //minimize application
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}