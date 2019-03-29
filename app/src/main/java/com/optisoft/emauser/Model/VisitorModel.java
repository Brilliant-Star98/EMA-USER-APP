package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 7/3/2018.
 */

public class VisitorModel {

    @SerializedName("id")
    private String id;

    @SerializedName("visitor_name")
    private String visitor_name;

    @SerializedName("visit_to")
    private String visit_to;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("date_in")
    private String date_in;

    @SerializedName("time_in")
    private String time_in;

    @SerializedName("date_out")
    private String date_out;

    @SerializedName("time_out")
    private String time_out;

    @SerializedName("gender")
    private String gender;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("guard_id")
    private String guard_id;

    @SerializedName("vical_no")
    private String vical_no;

    @SerializedName("status")
    private String status;

    @SerializedName("num_person")
    private String num_person;

    @SerializedName("image")
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVisitor_name() {
        return visitor_name;
    }

    public void setVisitor_name(String visitor_name) {
        this.visitor_name = visitor_name;
    }

    public String getVisit_to() {
        return visit_to;
    }

    public void setVisit_to(String visit_to) {
        this.visit_to = visit_to;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_in() {
        return date_in;
    }

    public void setDate_in(String date_in) {
        this.date_in = date_in;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public void setDate_out(String date_out) {
        this.date_out = date_out;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGuard_id() {
        return guard_id;
    }

    public void setGuard_id(String guard_id) {
        this.guard_id = guard_id;
    }

    public String getVical_no() {
        return vical_no;
    }

    public void setVical_no(String vical_no) {
        this.vical_no = vical_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNum_person() {
        return num_person;
    }

    public void setNum_person(String num_person) {
        this.num_person = num_person;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
