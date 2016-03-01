package in.jiyofit.the_app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;

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
import java.net.URL;
import java.net.URLEncoder;

public class FeedbackBackgroundTask extends AsyncTask<String, Void, String> {
    private Context ctx;
    private String feedbackType;
    private String exerciseFeedbackURL = "XYZ.com";
    private String userFeedbackURL = "http://jiyofit.in/appwork/prabeesh/userFeedback.php";
    SharedPreferences userFeedbackPrefs;
    public FeedbackBackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        feedbackType = params[0];
        String stringURL;
        if (feedbackType.equals("exerciseFeedback")) {
            stringURL = exerciseFeedbackURL;
        } else {
            stringURL = userFeedbackURL;
        }
        try {
            URL url = new URL(stringURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream opStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(opStream, "UTF-8"));
            String data_string;
            //when in a class, use Context.MODE_PRIVATE instead of simply MODE_PRIVATE
            SharedPreferences profilePrefs = ctx.getSharedPreferences("PData", Context.MODE_PRIVATE);
            userFeedbackPrefs = ctx.getSharedPreferences("UserFeedback", Context.MODE_PRIVATE);
            DisplayMetrics metrics = new DisplayMetrics();
            //getWindowManager() is a method of Activity class hence ctx has to be casted such
            ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            //to get RAM of the device
            ActivityManager actManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            String memVal = "0";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                memVal = String.valueOf(memInfo.totalMem);
            }
            if (feedbackType.equals("exerciseFeedback")) {
                //urlencoder.encode takes only string input
                data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode("x", "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode("y", "UTF-8");
            } else {
                data_string = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(profilePrefs.getString("Email ID", "NA"), "UTF-8") + "&"
                        + URLEncoder.encode("phone_number", "UTF-8") + "=" + URLEncoder.encode(profilePrefs.getString("Phone Number", "0"), "UTF-8") + "&"
                        + URLEncoder.encode("bug_presence", "UTF-8") + "=" + URLEncoder.encode(userFeedbackPrefs.getString("IsBugPresent", "NA"), "UTF-8") + "&"
                        + URLEncoder.encode("user_feedback", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(userFeedbackPrefs.getString("Feedback Text", "NA")), "UTF-8") + "&"
                        + URLEncoder.encode("mobile_manufacturer", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(Build.MANUFACTURER), "UTF-8") + "&"
                        + URLEncoder.encode("model", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(Build.MODEL), "UTF-8") + "&"
                        + URLEncoder.encode("android_version", "UTF-8") + "=" + URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8") + "&"
                        + URLEncoder.encode("display_size", "UTF-8") + "=" + URLEncoder.encode(metrics.widthPixels + "X" + metrics.heightPixels, "UTF-8") + "&"
                        + URLEncoder.encode("memory_size", "UTF-8") + "=" + URLEncoder.encode(memVal, "UTF-8");
            }
            bufferedWriter.write(data_string);
            bufferedWriter.flush();
            bufferedWriter.close();
            opStream.close();
            InputStream ipStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ipStream, "ISO-8859-1"));
            String response = "";
            String line;
            while ((line = reader.readLine()) != null) {
                response = line;
            }
            reader.close();
            ipStream.close();
            httpURLConnection.disconnect();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String result= jsonObject.getString("success");
            if (feedbackType.equals("exerciseFeedback")) {

            } else {
                SharedPreferences.Editor userFeedbackEditor = userFeedbackPrefs.edit();
                if(result.equals("1")) {
                    userFeedbackEditor.putBoolean("userFeedbackIsSent", true);
                } else {
                    userFeedbackEditor.putBoolean("userFeedbackIsSent", false);
                }
                userFeedbackEditor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
