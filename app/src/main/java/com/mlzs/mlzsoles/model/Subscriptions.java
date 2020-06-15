package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Subscriptions implements Serializable {

    @SerializedName("item_name")
    @Expose
    private String item_name;
    @SerializedName("plan_type")
    @Expose
    private String plan_type;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("payment_gateway")
    @Expose
    private String payment_gateway;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("payment_status")
    @Expose
    private String payment_status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("after_discount")
    @Expose
    private String after_discount;
    @SerializedName("paid_amount")
    @Expose
    private String paid_amount;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPayment_gateway() {
        return payment_gateway;
    }

    public void setPayment_gateway(String payment_gateway) {
        this.payment_gateway = payment_gateway;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAfter_discount() {
        return after_discount;
    }

    public void setAfter_discount(String after_discount) {
        this.after_discount = after_discount;
    }

    public String getPaid_amount() {
        return paid_amount;
    }

    public void setPaid_amount(String paid_amount) {
        this.paid_amount = paid_amount;
    }
}
