package com.wishfin_credit_card;

import org.json.JSONArray;

public class Gettersetterforall {
private String name;

    public String getInsta_apply_link() {
        return insta_apply_link;
    }

    public void setInsta_apply_link(String insta_apply_link) {
        this.insta_apply_link = insta_apply_link;
    }

    private String id;
    private String desc;
    private String card_state,insta_apply_link;

    public String getAppliedstatus() {
        return appliedstatus;
    }

    public void setAppliedstatus(String appliedstatus) {
        this.appliedstatus = appliedstatus;
    }

    private String appliedstatus;

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    private String bank_code;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCard_state() {
        return card_state;
    }

    public void setCard_state(String card_state) {
        this.card_state = card_state;
    }

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
