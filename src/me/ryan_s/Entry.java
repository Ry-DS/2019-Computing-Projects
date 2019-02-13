package me.ryan_s;

import java.util.Date;

/**
 * Created by SimplyBallistic on 13/02/2019
 *
 * @author SimplyBallistic
 **/
public class Entry {
    private String name;
    private String gender;
    private Event event;
    private String yearLevel;
    private String result;
    private Date timeStamp;

    public Entry(String name, String gender, Event event, String yearLevel, String result) {
        this.name = name;
        this.gender = gender;
        this.event = event;
        this.yearLevel = yearLevel;
        this.result = result;
        timeStamp = new Date();
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Event getEvent() {
        return event;
    }

    public String getYearLevel() {
        return yearLevel;
    }

    public String getResult() {
        return result;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
