
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExamCategoryList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dueration")
    @Expose
    private String dueration;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("is_paid")
    @Expose
    private String is_paid;
    @SerializedName("is_purchased")
    @Expose
    private String is_purchased;
    @SerializedName("total_marks")
    @Expose
    private String total_marks;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("total_questions")
    @Expose
    private String total_questions;

    @SerializedName("exam_type")
    @Expose
    private String exam_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueration() {
        return dueration;
    }

    public void setDueration(String dueration) {
        this.dueration = dueration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(String total_questions) {
        this.total_questions = total_questions;
    }

    public String getIs_purchased() {
        return is_purchased;
    }

    public void setIs_purchased(String is_purchased) {
        this.is_purchased = is_purchased;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }
}
