package in.jiyofit.the_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class StatboardActivity extends BaseActivity implements StatAdapter.FBClickListener{
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private ArrayList<Stat> statBoardData = new ArrayList<Stat>();
    private final static String STAT_BOARD_URL = "http://jiyofit.in/appwork/statboard.php";
    private StatAdapter statAdapter;
    private RecyclerView statRecyclerView;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_statboard, contentFrameLayout);
        statRecyclerView = (RecyclerView) findViewById(R.id.astatboard_rv);

        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        statAdapter = new StatAdapter(this);
        getStatboardJSON();
        statRecyclerView.setAdapter(statAdapter);
        statAdapter.setFBClickListener(this);
        statRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!FacebookSdk.isInitialized()){
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        shareDialog = new ShareDialog(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new
                FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                }
        );

    }

    public void getStatboardJSON() {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, STAT_BOARD_URL,
                //Both both listeners running in main thread
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        statBoardData = parseJSONResponse(response);
                        statAdapter.setStatList(statBoardData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonRequest);
    }

    private ArrayList<Stat> parseJSONResponse(JSONObject response) {
        //response may be = null or response may not contain any data
        ArrayList<Stat> parsedStatListData = new ArrayList<Stat>();
        if (response == null || response.length() == 0) {
            return null;
        }
        try {
            JSONArray statArray = response.getJSONArray("data");
            for (int i = 0; i < statArray.length(); i++) {
                JSONObject currentStatJSON = statArray.getJSONObject(i);
                Integer statID = currentStatJSON.getInt("Stat_ID");
                String statText = currentStatJSON.getString("Stat_Text");
                String statImageURL = currentStatJSON.getString("Stat_Image_URL");
                Date timeStamp = StaticMethods.convertDate(currentStatJSON.getString("Timestamp"));
                String statType = currentStatJSON.getString("Stat_Type");

                Stat statList = new Stat();
                statList.setStatID(statID);
                statList.setStatText(statText);
                statList.setStatImageURL(statImageURL);
                statList.setStatDate(timeStamp);
                statList.setStatType(statType);

                parsedStatListData.add(statList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parsedStatListData;
    }

    @Override
    public void FBClick(String badgeURL, String statPost) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent;
            if(badgeURL!=null) {
                linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("please ignore, this is for testing Facebook sdk" + statPost)
                        .setImageUrl(Uri.parse(badgeURL))
                        .build();
            } else {
                linkContent = new ShareLinkContent.Builder()
                        .setContentDescription("please ignore, this is for testing Facebook sdk" + statPost)
                        .setContentTitle("Testing")
                        .build();
            }
            shareDialog.show(linkContent);
        }
    }
}
