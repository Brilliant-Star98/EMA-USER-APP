package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 4/14/2018.
 */

public class UserModel {

    @SerializedName("userId")
    private String userId;

    @SerializedName("email")
    private String email;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("roleId")
    private String roleId;

    @SerializedName("country_code")
    private String country_code;

    @SerializedName("country")
    private String country;

    @SerializedName("short_code")
    private String short_code;

    @SerializedName("profile_picture")
    private String profile_picture;

    @SerializedName("dob")
    private String dob;

    @SerializedName("house_number")
    private String house_number;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("street")
    private String street;

    @SerializedName("court")
    private String court;

    @SerializedName("full_address")
    private String full_address;

    @SerializedName("residents")
    private String residents;

    @SerializedName("gender")
    private String gender;

    @SerializedName("primary_contact")
    private String primary_contact;

    @SerializedName("secondary_contact")
    private String secondary_contact;

    @SerializedName("created_date")
    private String created_date;

    @SerializedName("agent_id")
    private String agent_id;

    @SerializedName("alt_mobile")
    private String alt_mobile;

    @SerializedName("contact_email")
    private String contact_email;

    @SerializedName("content")
    private String content;

    @SerializedName("fb_token")
    private String fb_token;

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public String getFb_token() {
        return fb_token;
    }

    public void setFb_token(String fb_token) {
        this.fb_token = fb_token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getResidents() {
        return residents;
    }

    public void setResidents(String residents) {
        this.residents = residents;
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

    public String getSecondary_contact() {
        return secondary_contact;
    }

    public void setSecondary_contact(String secondary_contact) {
        this.secondary_contact = secondary_contact;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getAlt_mobile() {
        return alt_mobile;
    }

    public void setAlt_mobile(String alt_mobile) {
        this.alt_mobile = alt_mobile;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
