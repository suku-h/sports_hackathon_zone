package in.jiyofit.the_app;

import java.util.Date;

public class Stat {
    private int statID;
    private String statText;
    private Date statDate;
    private String statImageURL;
    private String statType;

    public Stat(){

    }

    public Stat(int statID, String statText, Date statDate, String statImageURL, String statType) {
        this.statID = statID;
        this.statText = statText;
        this.statDate = statDate;
        this.statImageURL = statImageURL;
        this.statType = statType;
    }

    public int getStatID() {
        return statID;
    }

    public void setStatID(int statID) {
        this.statID = statID;
    }

    public String getStatText() {
        return statText;
    }

    public void setStatText(String statText) {
        this.statText = statText;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public String getStatImageURL() {
        return statImageURL;
    }

    public void setStatImageURL(String statImageURL) {
        this.statImageURL = statImageURL;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }
}
