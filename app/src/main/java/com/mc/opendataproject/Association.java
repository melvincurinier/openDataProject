package com.mc.opendataproject;

import java.io.Serializable;

public class Association implements Serializable {
    private String city;
    private String adress;
    private String title;
    private String description;

    public Association(String city, String adress, String title, String description) {
        this.city = city;
        this.adress = adress;
        this.title = title;
        this.description = description;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }
    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
