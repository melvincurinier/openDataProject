package com.mc.opendataproject;

import java.io.Serializable;

public class Association implements Serializable {
    private String city;
    private String address;
    private String title;
    private String description;
    private String postal_code;
    private String region;

    public Association(String city, String address, String title, String description, String postal_code, String region) {
        this.city = city;
        this.address = address;
        this.title = title;
        this.description = description;
        this.postal_code = postal_code;
        this.region = region;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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

    public String getPostal_code() {
        return postal_code;
    }
    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
}
