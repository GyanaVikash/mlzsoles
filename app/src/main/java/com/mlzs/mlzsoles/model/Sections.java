
package com.mlzs.mlzsoles.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sections {

    @SerializedName("section_name")
    @Expose
    private String section_name;
    @SerializedName("section_time")
    @Expose
    private String section_time;
    @SerializedName("questions")
    @Expose
    private List<String> questions = null;

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getSection_time() {
        return section_time;
    }

    public void setSection_time(String section_time) {
        this.section_time = section_time;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

}
