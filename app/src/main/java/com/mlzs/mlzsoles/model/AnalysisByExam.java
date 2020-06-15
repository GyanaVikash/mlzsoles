
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnalysisByExam implements Serializable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_paid")
    @Expose
    private String is_paid;
    @SerializedName("dueration")
    @Expose
    private String dueration;
    @SerializedName("total_marks")
    @Expose
    private String total_marks;
    @SerializedName("attempts")
    @Expose
    private String attempts;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("user_id")
    @Expose
    private String user_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(String is_paid) {
        this.is_paid = is_paid;
    }

    public String getDueration() {
        return dueration;
    }

    public void setDueration(String dueration) {
        this.dueration = dueration;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getAttempts() {
        return attempts;
    }

    public void setAttempts(String attempts) {
        this.attempts = attempts;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
