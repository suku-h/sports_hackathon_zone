package in.jiyofit.the_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

//Fragmentactivity because of facebook fragment
public class LoginActivity extends FragmentActivity {
    EditText LOGIN_EMAIL,LOGIN_PASSWORD;
    String login_email, login_password;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private Boolean fbLoginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbLoginSuccess = false;
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        LOGIN_EMAIL=(EditText)findViewById(R.id.alogin_et_email);
        LOGIN_PASSWORD=(EditText)findViewById(R.id.alogin_et_password);
        loginButton = (LoginButton) findViewById(R.id.alogin_btn_facebook);

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                //accesstoken is not shown through getToken()/tostring() for security reasons
                //it has to be accessed in a different way
                GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            SharedPreferences loginPrefs = getSharedPreferences("LData", MODE_PRIVATE);
                            SharedPreferences.Editor loginEditor = loginPrefs.edit();
                            loginEditor.putString("Email ID", object.getString("email"));
                            loginEditor.putBoolean("isItSaved", true);
                            loginEditor.commit();
                            SharedPreferences profilePrefs = getSharedPreferences("PData", MODE_PRIVATE);
                            if (profilePrefs.getBoolean("profileIsSet", false)) {
                                SharedPreferences.Editor profileEditor = profilePrefs.edit();
                                profileEditor.putString("Email ID Facebook", object.getString("email"));
                                profileEditor.putString("First Name", object.getString("Name"));
                                profileEditor.putString("Gender", object.getString("gender"));
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                                Date birthday = dateFormat.parse(object.getString("birthday"));
                                String currentDateTime = Calendar.getInstance().getTime().toString();
                                profileEditor.putBoolean("Logged Using Facebook", true);
                                profileEditor.commit();
                                Toast.makeText(LoginActivity.this, object.getString("email") + " " + object.getString("sName") + " " + object.getString("gender"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), FirstActivity.class));
                            } else {
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            }
                            fbLoginSuccess = true;
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //without this you do not get the data which is required using permissions in the response
                //over here you point out all the parameters that you need
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, email, gender, birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                fbLoginSuccess = false;
            }

            @Override
            public void onError(FacebookException e) {
                fbLoginSuccess = false;
                Toast.makeText(LoginActivity.this, "Try again later or register with email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
