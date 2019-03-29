package com.optisoft.emauser.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by OptiSoft_A on 4/5/2018.
 */

public class BillModel {
   /*
   * "id": "5",
      "bill_type": "5",
      "bill_month": "2018-03-01",
      "bill_amount": "345",
      "bill_generated_date": "2018-04-16",
      "due_date": "2018-04-20",
      "rate": "4343",
      "unit": "4353",
      "status": "1",
      "client_id": "65",
      "agent_id": "64",
      "late_fee": "34453",
      "total_amount": "4434",
      "created_date": "2018-04-16 07:02:43",
      "bill_title": "Electricity"*/

        @SerializedName("id")
        private String id;

        @SerializedName("bill_type")
        private String bill_type;

        @SerializedName("bill_month")
        private String bill_month;

        @SerializedName("bill_amount")
        private String bill_amount;

        @SerializedName("bill_generated_date")
        private String bill_generated_date;

       @SerializedName("due_date")
        private String due_date;

        @SerializedName("rate")
        private String rate;

        @SerializedName("unit")
        private String unit;

        @SerializedName("status")
        private String status;

        @SerializedName("txn_id")
        private String txn_id;

        @SerializedName("agent_id")
        private String agent_id;

       @SerializedName("client_id")
       private String client_id;

        @SerializedName("late_fee")
        private String late_fee;

         @SerializedName("total_amount")
        private String total_amount;

         @SerializedName("created_date")
        private String created_date;

         @SerializedName("bill_title")
        private String bill_title;

    public String getTxn_id() {
        return txn_id;
    }

    public void setTxn_id(String txn_id) {
        this.txn_id = txn_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBill_type() {
        return bill_type;
    }

    public void setBill_type(String bill_type) {
        this.bill_type = bill_type;
    }

    public String getBill_month() {
        return bill_month;
    }

    public void setBill_month(String bill_month) {
        this.bill_month = bill_month;
    }

    public String getBill_amount() {
        return bill_amount;
    }

    public void setBill_amount(String bill_amount) {
        this.bill_amount = bill_amount;
    }

    public String getBill_generated_date() {
        return bill_generated_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setBill_generated_date(String bill_generated_date) {
        this.bill_generated_date = bill_generated_date;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getLate_fee() {
        return late_fee;
    }

    public void setLate_fee(String late_fee) {
        this.late_fee = late_fee;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getBill_title() {
        return bill_title;
    }

    public void setBill_title(String bill_title) {
        this.bill_title = bill_title;
    }
}
