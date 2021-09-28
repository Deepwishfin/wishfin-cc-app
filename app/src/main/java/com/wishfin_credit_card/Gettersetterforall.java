package com.wishfin_credit_card;

import org.json.JSONArray;

public class Gettersetterforall {
private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String image;

    private JSONArray feauters;

    public JSONArray getFeauters() {
        return feauters;
    }

    public void setFeauters(JSONArray feauters) {
        this.feauters = feauters;
    }

    public String getAnnualfees() {
        return annualfees;
    }

    public void setAnnualfees(String annualfees) {
        this.annualfees = annualfees;
    }

    public String getJoiningfees() {
        return joiningfees;
    }

    public void setJoiningfees(String joiningfees) {
        this.joiningfees = joiningfees;
    }

    private String annualfees,joiningfees;
}
