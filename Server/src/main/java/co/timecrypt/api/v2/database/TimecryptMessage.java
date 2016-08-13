package co.timecrypt.api.v2.database;

import java.sql.Date;

/**
 * The data holder class for all Timecrypt messages.
 */
public class TimecryptMessage {

    private String text;
    private String title;
    private Date destructDate;
    private int viewCount;

    public TimecryptMessage(String text, String title, int viewCount, Date destructDate) {
        super();
        this.text = text;
        this.title = title;
        this.viewCount = viewCount;
        this.destructDate = destructDate;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public Date getDestructDate() {
        return destructDate;
    }

    public int getViewCount() {
        return viewCount;
    }

}