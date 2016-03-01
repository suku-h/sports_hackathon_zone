package in.jiyofit.the_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LRBackgroundTask extends AsyncTask<String, Void, String> {
    Context ctx;
    String method;
    private String email,password;
    LRBackgroundTask(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... params) {
        String register_url="http://jiyofit.in/appwork/prabeesh/register.php";
        String login_url="http://jiyofit.in/appwork/prabeesh/login.php";
        method=params[0];
        if (method.equals("register")) {
            email = params[1];
            password = params[2];
            try {
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream opStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(opStream, "UTF-8"));
                String data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                opStream.close();
                InputStream ipStream = httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(ipStream,"ISO-8859-1"));
                String response="";
                String line;
                while ((line=reader.readLine())!=null){
                    response=line;
                }
                reader.close();
                ipStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(method.equals("login")){
            email=params[1];
            password=params[2];
            try{
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream opStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(opStream, "UTF-8"));
                String data_string =  URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(data_string);
                bufferedWriter.flush();
                bufferedWriter.close();
                opStream.close();

                InputStream ipStream = httpURLConnection.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(ipStream,"ISO-8859-1"));
                String response="";
                String line;
                while ((line=reader.readLine())!=null){
                    response=line;
                }
                reader.close();
                ipStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void...values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String result= jsonObject.getString("success");
            SharedPreferences profilePrefs = ctx.getSharedPreferences("PData", Context.MODE_PRIVATE);
            SharedPreferences.Editor profileEditor = profilePrefs.edit();
            if (method.equals("register")){
                switch (result) {
                    case "1":
                        ctx.startActivity(new Intent(ctx, LoginActivity.class));
                        break;
                    case "0":
                        Toast.makeText(ctx, "This email ID is already registered", Toast.LENGTH_LONG).show();
                        ctx.startActivity(new Intent(ctx, RegisterActivity.class));
                        break;
                    case "-1":
                        ctx.startActivity(new Intent(ctx, RegisterActivity.class));
                        Toast.makeText(ctx, "Signup Failed. Try again", Toast.LENGTH_LONG).show();
                        break;
                }
                profileEditor.putBoolean("profileIsSet", false);
                profileEditor.commit();
            }  else if (method.equals("login")){
                switch (result) {
                    case "1":
                        //getSharedPreferences needs a context to work
                        SharedPreferences loginPrefs = ctx.getSharedPreferences("LData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor loginEditor = loginPrefs.edit();
                        loginEditor.putString("Email ID", email);
                        loginEditor.putString("Password", password);
                        loginEditor.putBoolean("isItSaved", true);
                        loginEditor.commit();
                        profileEditor.putString("Email ID", email);
                        profileEditor.putBoolean("Logged Using Facebook", false);
                        profileEditor.commit();
                        if (profilePrefs.getBoolean("profileIsSet", false)) {
                            ctx.startActivity(new Intent(ctx, FirstActivity.class));
                        } else {
                            ctx.startActivity(new Intent(ctx, ProfileActivity.class));
                        }
                        break;
                    case "0":
                        Toast.makeText(ctx, "Password is incorrect", Toast.LENGTH_LONG).show();
                        break;
                    case "-1":
                        Toast.makeText(ctx, "This email ID is not registered", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

