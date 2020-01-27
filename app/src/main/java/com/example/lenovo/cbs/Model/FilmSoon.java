package com.example.lenovo.cbs.Model;

import java.util.Date;

import io.realm.RealmObject;


public class FilmSoon extends RealmObject {
    private String titleUA;
    private String rating;
    private String titleENG;
    private Date date;
    private String id;
    private String time;
    private String synopsis;
    private String  genres;
    private String  studios;
    private String  countries;
    private byte[] image;

    public String getTitleUA() {
        return titleUA;
    }

    public void setTitleUA(String titleUA) {
        this.titleUA = titleUA;
    }

    public String getTitleENG() {
        return titleENG;
    }

    public void setTitleENG(String titleENG) {
        this.titleENG = titleENG;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getStudios() {
        return studios;
    }

    public void setStudios(String studios) {
        this.studios = studios;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRating() {
        return rating + "+";
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
