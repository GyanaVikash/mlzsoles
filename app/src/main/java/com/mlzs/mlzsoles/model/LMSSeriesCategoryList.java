package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LMSSeriesCategoryList implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lmsseries_id")
    @Expose
    private Integer lmsseries_id;
    @SerializedName("lmscontent_id")
    @Expose
    private Integer lmscontent_id;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("subject_id")
    @Expose
    private Integer subject_id;
    @SerializedName("content_type")
    @Expose
    private String content_type;
    @SerializedName("is_url")
    @Expose
    private Integer is_url;
    @SerializedName("file_path")
    @Expose
    private String file_path;
    @SerializedName("description")
    @Expose

    private String description;
    @SerializedName("record_updated_by")
    @Expose
    private Integer record_updated_by;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLmsseries_id() {
        return lmsseries_id;
    }

    public void setLmsseries_id(Integer lmsseries_id) {
        this.lmsseries_id = lmsseries_id;
    }

    public Integer getLmscontent_id() {
        return lmscontent_id;
    }

    public void setLmscontent_id(Integer lmscontent_id) {
        this.lmscontent_id = lmscontent_id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(Integer subject_id) {
        this.subject_id = subject_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public Integer getIs_url() {
        return is_url;
    }

    public void setIs_url(Integer is_url) {
        this.is_url = is_url;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRecord_updated_by() {
        return record_updated_by;
    }

    public void setRecord_updated_by(Integer record_updated_by) {
        this.record_updated_by = record_updated_by;
    }
}
