package in.jiyofit.the_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity{
    EditText REGISTER_EMAIL,REGISTER_PASSWORD,REGISTER_REPASSWORD;
    String register_email,register_password, register_repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void userRegister(View v){
        StaticMethods.HideKeypad(this);
        REGISTER_EMAIL=(EditText)findViewById(R.id.aregister_et_email);
        REGISTER_PASSWORD=(EditText)findViewById(R.id.aregister_et_password);
        REGISTER_REPASSWORD=(EditText)findViewById(R.id.aregister_et_repassword);
        register_email=REGISTER_EMAIL.getText().toString();
        register_password=REGISTER_PASSWORD.getText().toString();
        register_repassword=REGISTER_REPASSWORD.getText().toString();
        if (register_email.length()==0 || register_password.length()==0 || register_repassword.length()==0){
            Toast.makeText(this, "Please fill all 3 fields", Toast.LENGTH_SHORT).show();
        } else if (register_password.length()<6){
            Toast.makeText(this, "Password should be 6 or more characters long", Toast.LENGTH_SHORT).show();
        } else if (!register_password.equals(register_repassword)) {
            Toast.makeText(this, "Password and retyped password do not match", Toast.LENGTH_SHORT).show();
        } else {
            String method = "register";
            if (!StaticMethods.isNetworkAvailable(this)){
                Toast.makeText(this, "Unable to connect with internet", Toast.LENGTH_LONG).show();
            } else {
                new LRBackgroundTask(this).execute(method, register_email, register_password);
                //calling finish() closed the activity and it went back to loginactivity
            }
        }
    }
}
