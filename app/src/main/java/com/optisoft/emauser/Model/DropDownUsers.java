package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 7/2/2018.
 */

public class DropDownUsers {
    //{"userId":"123","fname":"Karis","lname":"Gachoki","country_code":"254","mobile":"722341514"}

    @SerializedName("userId")
    private String userId;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("mobile")
    private String mobile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return fname +" "+lname;
    }
}
