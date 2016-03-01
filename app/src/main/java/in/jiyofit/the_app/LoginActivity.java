package in.jiyofit.the_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//Fragmentactivity because of facebook fragment
public class LoginActivity extends FragmentActivity {
    EditText LOGIN_EMAIL,LOGIN_PASSWORD;
    String login_email, login_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        LOGIN_EMAIL = (EditText) findViewById(R.id.alogin_et_email);
        LOGIN_PASSWORD = (EditText) findViewById(R.id.alogin_et_password);

    }
    public void userLogin(View view){
        StaticMethods.HideKeypad(this);
        login_email = LOGIN_EMAIL.getText().toString();
        login_password = LOGIN_PASSWORD.getText().toString();
        String method = "login";
        if (!StaticMethods.isNetworkAvailable(this)){
            Toast.makeText(this, "Unable to connect with internet", Toast.LENGTH_LONG).show();
        } else {
            new LRBackgroundTask(this).execute(method, login_email, login_password);
        }
    }

    public void registerNow(View view){
        StaticMethods.HideKeypad(this);
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // to minimize app when back is pressed. When a person logs out and then presses back,
    //it goes back to firstactivity. Hence, this debug
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
