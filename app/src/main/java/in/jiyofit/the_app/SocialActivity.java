package in.jiyofit.the_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

import static in.jiyofit.the_app.SocialKeys.caption;
import static in.jiyofit.the_app.SocialKeys.commentCount;
import static in.jiyofit.the_app.SocialKeys.comments;
import static in.jiyofit.the_app.SocialKeys.created_time;
import static in.jiyofit.the_app.SocialKeys.data;
import static in.jiyofit.the_app.SocialKeys.from;
import static in.jiyofit.the_app.SocialKeys.image_id;
import static in.jiyofit.the_app.SocialKeys.images;
import static in.jiyofit.the_app.SocialKeys.likeCount;
import static in.jiyofit.the_app.SocialKeys.likes;
import static in.jiyofit.the_app.SocialKeys.profile_picture;
import static in.jiyofit.the_app.SocialKeys.res_url;
import static in.jiyofit.the_app.SocialKeys.standard_res;
import static in.jiyofit.the_app.SocialKeys.tags;
import static in.jiyofit.the_app.SocialKeys.text;
import static in.jiyofit.the_app.SocialKeys.type;
import static in.jiyofit.the_app.SocialKeys.username;

public class SocialActivity extends FragmentActivity implements SocialInterface{
    ViewPager viewPager = null;
    private ArrayList<Social> socialData = new ArrayList<Social>();
    boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    ScheduledExecutorService newSocialExecutor;
    PagerTabStrip tabStrip;
    private RequestQueue requestQueue;
    String[] socialURL = {"https://api.instagram.com/v1/tags/adidasuprising/media/recent?access_token=" + AppApplication.INSTAGRAM_ACCESS_TOKEN,
                          "https://api.instagram.com/v1/tags/fitness/media/recent?access_token=" + AppApplication.INSTAGRAM_ACCESS_TOKEN,
                          "https://api.instagram.com/v1/tags/instatest/media/recent?access_token=" + AppApplication.INSTAGRAM_ACCESS_TOKEN};
    //space in front of <font is needed to keep space between text and number
    String prefontHtml = " <font color='red'>";
    String postfontHtml = "</font>";
    String[] fragBaseTitle = {"Latest","Inspirational","Followed"};
    private int urlIndex = 0;
    int[] updateCount = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        viewPager = (ViewPager) findViewById(R.id.asocial_viewpager);
        //android.support.v4.app.Fragment requires getSupportFragmentManager instead of getFragmentManager
        FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new PagerAdapter(fm));
        viewPager.setCurrentItem(1, false);
        tabStrip = (PagerTabStrip) findViewById(R.id.asocial_ptabstrip);

        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        newSocialExecutor.shutdownNow();
    }

    /*
    When you move from fragA to fragB for the first time, it is created.
    But when you go back to fragA from fragB, it is not recreated. i.e. UI not destroyed
    hence getItem is not called. It is called only when the fragment is created for the first time
    If you are downloading something in fragA and then change to fragB, the data being downloaded is lost
    Hence you use FragmentStatePagerAdapter instead of FragmentPagerAdapter
    onSavedInstance is called on flipping fragments in a fragmentpagerAdapter
    onDetach is also called in FragmentStatePagerAdapter not in fragmentpagerAdapter
    When you return to fragA again then onCreate is called again as it uses the savedInstance,
    which does not happen in fragmentpagerAdapter
    */
    class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            if(position==0){
                //requires android.support.v4.app.Fragment
                frag = new LatestSocialFragment();
            } else if (position==1){
                //requires android.support.v4.app.Fragment
                frag = new SocialFragment();
            } else if (position==2){
                //requires android.support.v4.app.Fragment
                frag = new FollowedSocialFragment();
            }

            return frag;
        }

        @Override
        public int getCount() {
            //number of pages/ fragment
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Spanned fragTitle = null;
            String updateCountText;
            for (int i = 0; i < 3; i++){
                if(position == i){
                    if(updateCount[i] > 0){
                        updateCountText = prefontHtml + String.valueOf(updateCount[i]) + postfontHtml;
                    } else {
                        updateCountText = "";
                    }
                    fragTitle = Html.fromHtml(fragBaseTitle[i] + updateCountText);
                }
            }
            return fragTitle;
        }
    }

    @Override
    public void getJSONObject(String SOCIAL_URL, RequestQueue requestQueue, final SocialAdapter socialAdapter, final String pageName){
        loading = true;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, SOCIAL_URL,
                //Both both listeners running in main thread
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        socialData = parseJSONResponse(response, pageName);
                        Collections.sort(socialData, new CustomComparator());
                        socialAdapter.setSocialList(socialData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        if(requestQueue.getCache().get(SOCIAL_URL)!=null){
            //response exists
            String cachedResponse = new String(requestQueue.getCache().get(SOCIAL_URL).data);
            try {
                socialData = parseJSONResponse(new JSONObject(cachedResponse), pageName);
                socialAdapter.setSocialList(socialData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            //no response
            requestQueue.add(jsonRequest);
        }
    }

    private ArrayList<Social> parseJSONResponse(JSONObject response, String _pageName) {
        //response may be = null or response may not contain any data
        ArrayList<Social> parsedSocialData = new ArrayList<Social>();
        if(response==null || response.length()==0){
            return null;
        }
        try{
            JSONArray socialArray = response.getJSONArray(data);
            for (int i=0; i<socialArray.length(); i++){
                JSONObject currentImageJSON = socialArray.getJSONObject(i);
                String _id = currentImageJSON.getString(image_id);
                String _type = currentImageJSON.getString(type);
                JSONObject _images = currentImageJSON.getJSONObject(images);
                JSONObject _standard_resolution = _images.getJSONObject(standard_res);
                String url = _standard_resolution.getString(res_url);
                JSONArray jsontags = currentImageJSON.getJSONArray(tags);
                String[] _tags = new String[jsontags.length()];
                for(int j=0;j<jsontags.length();j++) {
                    _tags[j] = jsontags.getString(j);
                }
                JSONObject _caption = currentImageJSON.getJSONObject(caption);
                JSONObject _from = _caption.getJSONObject(from);
                String _profile_picture = _from.getString(profile_picture);
                String _username = _from.getString(username);
                String _text = _caption.getString(text);
                String string_created_time = _caption.getString(created_time);
                Date _dateTime = StaticMethods.convertDate(string_created_time);
                JSONObject _likes = currentImageJSON.getJSONObject(likes);
                int _likescount = _likes.getInt(likeCount);
                JSONObject _comments = currentImageJSON.getJSONObject(comments);
                int _commentscount = _comments.getInt(commentCount);

                Social social = new Social();
                social.setId(_id);
                social.setType(_type);
                social.setUrl_standard_res(url);
                social.setTags(_tags);
                social.setUsername(_username);
                social.setPostText(_text);
                social.setDateTime(_dateTime);
                social.setProfile_picture(_profile_picture);
                social.setLikesCount(_likescount);
                social.setCommentsCount(_commentscount);
                social.setPageName(_pageName);

                parsedSocialData.add(social);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return parsedSocialData;
    }

    public void loadMore (RecyclerView photoRecyclerView, final LinearLayoutManager layoutManager){
        photoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){ //check for scroll down
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading){
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                            loading = false;
                            Toast.makeText(SocialActivity.this, "reached end", Toast.LENGTH_SHORT).show();
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });
    }

    public class CustomComparator implements Comparator<Social> {
        @Override
        public int compare(Social o1, Social o2) {
            return o1.getDateTime().compareTo(o2.getDateTime());
        }
    }
}


