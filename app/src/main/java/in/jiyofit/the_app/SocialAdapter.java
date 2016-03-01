package in.jiyofit.the_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.text.DateFormat;
import java.util.ArrayList;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.VHSocialAdapter> {

    private LayoutInflater layoutInflater;
    private ArrayList<Social> socialRecyclerList = new ArrayList<>();
    private ImageLoader imageLoader;
    DateFormat[] formats = new DateFormat[]{DateFormat.getDateInstance()};

    public SocialAdapter(Context ctx) {
        layoutInflater = LayoutInflater.from(ctx);
        //to get the instagram images for photograph and profilePic
        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setSocialList(ArrayList<Social> socialList){
        this.socialRecyclerList = socialList;
        //update the adapter to reflect the new set of photos
        notifyDataSetChanged();
    }

    @Override
    public VHSocialAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.social_row, parent, false);
        return new VHSocialAdapter(v);
    }

    @Override
    public void onBindViewHolder(final VHSocialAdapter holder, int position) {
        Social currentPhotoData = socialRecyclerList.get(position);
        holder.username.setText(currentPhotoData.getUsername());
        holder.postText.setText(currentPhotoData.getPostText());
        holder.timeago.setText(android.text.format.DateFormat.format("dd MMM, yy", currentPhotoData.getDateTime()).toString());
        //setText takes only String values
        holder.likesCount.setText(String.valueOf(currentPhotoData.getLikesCount()));
        holder.commentsCount.setText(String.valueOf(currentPhotoData.getCommentsCount()));
        String profile_picture_url = currentPhotoData.getProfile_picture();
        String photograph_url = currentPhotoData.getUrl_standard_res();
        /*
        ImageListener is an interface for the response handlers on image requests.
        The call flow is this: 1. Upon being attached to a request, onResponse(response, true)
        will be invoked to reflect any cached data that was already available.
        If the data was available, response.getBitmap() will be non-null.
        2. After a network response returns, only one of the following cases will happen: -
        onResponse(response, false) will be called if the image was loaded. or -
        onErrorResponse will be called if there was an error loading the image.
         */
        imageLoader.get(profile_picture_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.profilePic.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        imageLoader.get(photograph_url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                holder.photograph.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        //return the number of elements in the recyclerview
        return socialRecyclerList.size();
    }

    static class VHSocialAdapter extends RecyclerView.ViewHolder{
        private ImageView profilePic, photograph, commentsIcon;
        private TextView username, timeago, likesCount, commentsCount, postText;
        EditText comment;
        ImageButton likeButton;

        public VHSocialAdapter(View itemView) {
            super(itemView);

            photograph = (ImageView) itemView.findViewById(R.id.rsocial_iv_photograph);
            username = (TextView) itemView.findViewById(R.id.rsocial_tv_username);
            postText = (TextView) itemView.findViewById(R.id.rsocial_tv_post);
            timeago = (TextView) itemView.findViewById(R.id.rsocial_tv_datetime);
            profilePic = (ImageView) itemView.findViewById(R.id.rsocial_iv_profilepic);
            likeButton = (ImageButton) itemView.findViewById(R.id.rsocial_ib_like);
            likesCount = (TextView) itemView.findViewById(R.id.rsocial_tv_likes);
            commentsIcon = (ImageView) itemView.findViewById(R.id.rsocial_iv_comments);
            commentsCount = (TextView) itemView.findViewById(R.id.rsocial_tv_commentscount);
            comment = (EditText) itemView.findViewById(R.id.rsocial_et_comment);
            commentsIcon.setImageResource(R.drawable.comment);
            likeButton.setImageResource(R.drawable.likebutton);
        }
    }
}
