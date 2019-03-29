package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 2/3/2018.
 */

public class PaymentModel {
    /*
     "id": "1",
      "user_id": "64",
      "bill_id": "6",
      "amount": "25000",
      "note": "this is dummy and demo testing content",
      "transaction_id": "25658",
      "transaction_date": "2018-04-05",
      "created_date": "2018-04-21 00:16:10",
      "status": "1",
      "fname": "Jeetu",
      "lname": "Singh"
     */


    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("bill_id")
    private String bill_id;

    @SerializedName("amount")
    private String amount;

    @SerializedName("note")
    private String note;

    @SerializedName("transaction_id")
    private String transaction_id;

    @SerializedName("transaction_date")
    private String transaction_date;

    @SerializedName("created_date")
    private String created_date;

    @SerializedName("status")
    private String status;

    @SerializedName("fname")
    private String fname;

    @SerializedName("lname")
    private String lname;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
