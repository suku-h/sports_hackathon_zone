package in.jiyofit.the_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AutoLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_login);

        Boolean DEFAULT =false;
        SharedPreferences loginPrefs = getSharedPreferences("LData", MODE_PRIVATE);
        SharedPreferences profilePrefs = getSharedPreferences("PData", MODE_PRIVATE);

        if(loginPrefs.getBoolean("isItSaved", DEFAULT)){
            if(profilePrefs.getBoolean("profileIsSet", DEFAULT)) {
                startActivity(new Intent(this, FirstActivity.class));
            }else{
                startActivity(new Intent(this, ProfileActivity.class));
            }
        }else{
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
