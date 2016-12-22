package ch.hes_so.eventapp.models;

import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

/**
 * Created by Mysteriosis on 16.11.16.
 */

public class Person extends SugarRecord {
    private String lastname;
    private String firstname;
    private Integer state;
    private Double latitude;
    private Double longitude;
    private Boolean favorite;

    private Calendar calendar;

    // Constructors
    public Person(){}

    public Person(String lastname, String firstname, Integer state, String calId, Boolean favorite, Double lat, Double lon) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.state = state;
        this.latitude = lat;
        this.longitude = lon;
        this.favorite = favorite;

        Calendar cal = new Calendar(calId);
        cal.save();
        this.calendar = cal;
    }

    public Person(String lastname, String firstname, Integer state, String calId, Double lat, Double lon) {
        this(lastname, firstname, state, calId, false, lat, lon);
    }

    public Person(String lastname, String firstname, Integer state, String calId) {
        this(lastname, firstname, state, calId, false, null, null);
    }

    public Person(String lastname, String firstname, Integer state) {
        this(lastname, firstname, state, null, false, null, null);
    }

    @Override
    public String toString() {
        return"<" + this.getClass() + "> : " +
            "lastname: " + this.lastname +
            ", firstname: " + this.firstname +
            ", state: " + this.state +
            ", latitude: " + this.latitude +
            ", longitude: " + this.longitude +
            ", favorite: " + this.favorite +
            ", CalId: "  + this.calendar.getCalId();
    }

    // Getters & Setters
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
    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
    public void setCalendar(String id) {
        this.calendar = new Calendar(id);
    }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public LatLng getCoords() { return new LatLng(this.latitude, this.longitude); }
}
