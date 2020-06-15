
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExamHistory implements Serializable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_paid")
    @Expose
    private Integer is_paid;
    @SerializedName("marks_obtained")
    @Expose
    private Integer marks_obtained;
    @SerializedName("exam_status")
    @Expose
    private String exam_status;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("total_marks")
    @Expose
    private String total_marks;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("resultsslug")
    @Expose
    private String resultsslug;
    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("exam_type")
    @Expose
    private String exam_type;


    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        this.exam_type = exam_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIs_paid() {
        return is_paid;
    }

    public void setIs_paid(Integer is_paid) {
        this.is_paid = is_paid;
    }

    public Integer getMarks_obtained() {
        return marks_obtained;
    }

    public void setMarks_obtained(Integer marks_obtained) {
        this.marks_obtained = marks_obtained;
    }

    public String getExam_status() {
        return exam_status;
    }

    public void setExam_status(String exam_status) {
        this.exam_status = exam_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTotal_marks() {
        return total_marks;
    }

    public void setTotal_marks(String total_marks) {
        this.total_marks = total_marks;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getResultsslug() {
        return resultsslug;
    }

    public void setResultsslug(String resultsslug) {
        this.resultsslug = resultsslug;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

}
