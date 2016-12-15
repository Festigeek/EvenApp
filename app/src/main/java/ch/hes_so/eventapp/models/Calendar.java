package ch.hes_so.eventapp.models;

import com.orm.SugarRecord;

/**
 * Created by Mysteriosis on 01.12.16.
 */

public class Calendar extends SugarRecord {
    static String domain = "group.calendar.google.com";
    String calId;
    Person person;

    public Calendar(){}

    public Calendar(String id) {
        this.calId = id;
    }

    public String getCalId() {
        return calId;
    }

    public String getCompleteUrl() {
        return this.calId + "@" + Calendar.domain;
    }
}
