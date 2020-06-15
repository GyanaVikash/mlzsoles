
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopularExams {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("dueration")
    @Expose
    private String dueration;
    @SerializedName("category_id")
    @Expose
    private String category_id;
    @SerializedName("is_paid")
    @Expose
    private String is_paid;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("total_marks")
    @Expose
    private String total_marks;
    @SerializedName("having_negative_mark")
    @Expose
    private String having_negative_mark;
    @SerializedName("negative_mark")
    @Expose
    private String negative_mark;
    @SerializedName("total_questions")
    @Expose
    private String total_questions;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("exam_type")
    @Expose
    private String exam_type;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("has_language")
    @Expose
    private String has_language;
    @SerializedName("language_name")
    @Expose
    private String language_name;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDueration() {
        return dueration;
    }

    public void setDueration(String dueration) {
        this.dueration = dueration;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getHaving_negative_mark() {
        return having_negative_mark;
    }

    public void setHaving_negative_mark(String having_negative_mark) {
        this.having_negative_mark = having_negative_mark;
    }

    public String getNegative_mark() {
        return negative_mark;
    }

    public void setNegative_mark(String negative_mark) {
        this.negative_mark = negative_mark;
    }

    public String getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(String total_questions) {
        this.total_questions = total_questions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
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

    public String getHas_language() {
        return has_language;
    }

    public void setHas_language(String has_language) {
        this.has_language = has_language;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

}
