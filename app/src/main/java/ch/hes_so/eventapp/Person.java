package ch.hes_so.eventapp;

/**
 * Created by Mysteriosis on 16.11.16.
 */

public class Person {
    private String nom;
    private String prenom;
    private Integer state;
    private Boolean favorite;

    public Person(String nom, String prenom, Integer state, Boolean favorite) {
        this.nom = nom;
        this.prenom = prenom;
        this.state = state;
        this.favorite = favorite;
    }

    public Person(String nom, String prenom, Integer state) {
        this(nom, prenom, state, false);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

}
