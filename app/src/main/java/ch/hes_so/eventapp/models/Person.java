package ch.hes_so.eventapp.models;

import com.orm.SugarRecord;

/**
 * Created by Mysteriosis on 16.11.16.
 */

public class Person extends SugarRecord {
    private String lastname;
    private String firstname;
    private Integer state;
    private Boolean favorite;

    private Calendar calendar;

    public Person(){}

    public Person(String lastname, String firstname, Integer state, String calId, Boolean favorite) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.state = state;
        this.favorite = favorite;

        Calendar cal = new Calendar(calId);
        cal.save();
        this.calendar = cal;
    }

    public Person(String lastname, String firstname, Integer state, String calId) {
        this(lastname, firstname, state, calId, false);
    }

    public Person(String lastname, String firstname, Integer state) {
        this(lastname, firstname, state, null, false);
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(String id) {
        this.calendar = new Calendar(id);
    }
}
