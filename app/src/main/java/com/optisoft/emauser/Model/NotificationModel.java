package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 2/3/2018.
 */

public class NotificationModel {
    String instruction, description;

  /*
      "id": "1",
      "from": "64",
      "to": "75",
      "title": "SDFSDFSF",
      "message": "gfhgfh65656",
      "type": "1",
      "status": "0",
      "created_date": "2018-04-19 04:10:54"
      , B.lname, B.country_code, B.mobile
   */

  public String listType;

    public NotificationModel(String listType) {
        this.listType = listType;
    }

    @SerializedName("id")
      private String id;

      @SerializedName("fname")
      private String fname;

      @SerializedName("lname")
      private String lname;

      @SerializedName("country_code")
      private String country_code;

      @SerializedName("mobile")
      private String mobile;

      @SerializedName("from")
      private String from;

      @SerializedName("to")
      private String to;

      @SerializedName("title")
      private String title;

      @SerializedName("message")
      private String message;

      @SerializedName("type")
      public String type;

      @SerializedName("status")
      private String status;

    @SerializedName("created_date")
    private String created_date;

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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
}
