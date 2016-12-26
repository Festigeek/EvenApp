package ch.hes_so.eventapp.models;

import com.orm.SugarRecord;

import java.util.List;

import ch.hes_so.eventapp.MainActivity;

/**
 * Created by Mysteriosis on 01.12.16.
 */

public class Calendar extends SugarRecord {
    public static String DOMAIN = "group.calendar.google.com";
    String calId;
    Boolean busy = false;

    public Calendar(){}

    public Calendar(String id) {
        this.calId = id;
    }

    public String getCalId() {
        return calId;
    }

    public String getCompleteUrl() {
        return this.calId + "@" + DOMAIN;
    }

    public Boolean isBusy() {
        return this.busy;
    }
    public void setBusy(Boolean bool) {
        this.busy = bool;
    }
}
