package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 5/24/2018.
 */

public class ResidentModel {

    @SerializedName("name")
    private String fullname;

    @SerializedName("gender")
    private String gender;

    @SerializedName("pcontact")
    private String primary_contact;

    @SerializedName("scontact")
    private String secondry_contact;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(String primary_contact) {
        this.primary_contact = primary_contact;
    }

    public String getSecondry_contact() {
        return secondry_contact;
    }

    public void setSecondry_contact(String secondry_contact) {
        this.secondry_contact = secondry_contact;
    }
}
