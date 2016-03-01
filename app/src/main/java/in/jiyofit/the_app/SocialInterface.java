package in.jiyofit.the_app;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;

public interface SocialInterface {
    void getJSONObject(String url, RequestQueue requestQueue, final SocialAdapter socialAdapter, String page);
    void loadMore (RecyclerView photoRecyclerView, final LinearLayoutManager layoutManager);
}
