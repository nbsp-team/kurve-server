package model;

import java.io.Serializable;
import java.util.Date;

/**
 * nickolay, 13.09.2014.
 */
public class Score implements Serializable {
    private String userId;
    private int points;
    private Date date;

    public Score(String userId, int points) {
        this.userId = userId;
        this.points = points;
        this.date = new Date();
    }

    public Score(String userId, int points, Date date) {
        this.userId = userId;
        this.points = points;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
