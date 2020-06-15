
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyProfile implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("login_enabled")
    @Expose
    private String login_enabled;
    @SerializedName("role_id")
    @Expose
    private String role_id;
    @SerializedName("parent_id")
    @Expose
    private String parent_id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("stripe_active")
    @Expose
    private String stripe_active;
    @SerializedName("stripe_id")
    @Expose
    private String stripe_id;
    @SerializedName("stripe_plan")
    @Expose
    private String stripe_plan;
    @SerializedName("paypal_email")
    @Expose
    private String paypal_email;
    @SerializedName("card_brand")
    @Expose
    private String card_brand;
    @SerializedName("card_last_four")
    @Expose
    private String card_last_four;
    @SerializedName("trial_ends_at")
    @Expose
    private String trial_ends_at;
    @SerializedName("subscription_ends_at")
    @Expose
    private String subscription_ends_at;
    @SerializedName("settings")
    @Expose
    private String settings;
    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return "3HDrSOvRIs";
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLogin_enabled() {
        return login_enabled;
    }

    public void setLogin_enabled(String login_enabled) {
        this.login_enabled = login_enabled;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStripe_active() {
        return stripe_active;
    }

    public void setStripe_active(String stripe_active) {
        this.stripe_active = stripe_active;
    }

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public String getStripe_plan() {
        return stripe_plan;
    }

    public void setStripe_plan(String stripe_plan) {
        this.stripe_plan = stripe_plan;
    }

    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }

    public String getCard_last_four() {
        return card_last_four;
    }

    public void setCard_last_four(String card_last_four) {
        this.card_last_four = card_last_four;
    }

    public String getTrial_ends_at() {
        return trial_ends_at;
    }

    public void setTrial_ends_at(String trial_ends_at) {
        this.trial_ends_at = trial_ends_at;
    }

    public String getSubscription_ends_at() {
        return subscription_ends_at;
    }

    public void setSubscription_ends_at(String subscription_ends_at) {
        this.subscription_ends_at = subscription_ends_at;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

}
