package in.jiyofit.the_app;

import java.util.Date;

public class Social {
    private String id;
    private String type;
    private String url_standard_res;
    private Date dateTime;
    private String[] tags;
    private String username;
    private String text;
    private String profile_picture;
    private int likesCount;
    private int commentsCount;
    private String pageName;

    public Social(){

    }

    public Social(String id, String type, String url_standard_res, Date dateTime, String[] tags, String username, String text, String profile_picture, int likesCount, int commentsCount, String pageName) {
        this.id = id;
        this.type = type;
        this.url_standard_res = url_standard_res;
        this.dateTime = dateTime;
        this.tags = tags;
        this.username = username;
        this.text = text;
        this.profile_picture = profile_picture;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.pageName = pageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl_standard_res() {
        return url_standard_res;
    }

    public void setUrl_standard_res(String url_standard_res) {
        this.url_standard_res = url_standard_res;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPostText() {
        return text;
    }

    public void setPostText(String text) {
        this.text = text;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
