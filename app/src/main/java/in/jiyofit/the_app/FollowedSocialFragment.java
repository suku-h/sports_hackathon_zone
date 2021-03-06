package in.jiyofit.the_app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

public class FollowedSocialFragment extends Fragment{
    private RecyclerView photoRecyclerView;
    private final static String INSTAGRAM_URL = "https://api.instagram.com/v1/tags/instatest/media/recent?access_token=" + AppApplication.INSTAGRAM_ACCESS_TOKEN;
    private SocialAdapter socialAdapter;
    SocialInterface socialInterface;
    private String pageTitle = "FollowedSocial";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followed_social, container, false);

        //RequestQueue requestQueue = Volley.newRequestQueue(getActivity()); -> for one requestqueue
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();
        socialAdapter = new SocialAdapter(getActivity());
        socialInterface = (SocialInterface) getActivity();
        socialInterface.getJSONObject(INSTAGRAM_URL, requestQueue, socialAdapter, pageTitle);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoRecyclerView = (RecyclerView) getActivity().findViewById(R.id.ffollowedsocial_rv);
        photoRecyclerView.setAdapter(socialAdapter);
        socialAdapter.notifyDataSetChanged();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        photoRecyclerView.setLayoutManager(layoutManager);
        socialInterface.loadMore(photoRecyclerView, layoutManager);
    }
}
