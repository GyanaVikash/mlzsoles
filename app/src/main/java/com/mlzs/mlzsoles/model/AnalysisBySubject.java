package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AnalysisBySubject implements Serializable{
    
    @SerializedName("subject_name")
    @Expose
    private String subject_name;
    @SerializedName("correct_answers")
    @Expose
    private String correct_answers;
    @SerializedName("wrong_answers")
    @Expose
    private String wrong_answers;
    @SerializedName("not_answered")
    @Expose
    private String not_answered;
    @SerializedName("time_to_spend")
    @Expose
    private String time_to_spend;
    @SerializedName("time_spent")
    @Expose
    private String time_spent;
    @SerializedName("time_spent_on_correct_answers")
    @Expose
    private String time_spent_on_correct_answers;
    @SerializedName("time_spent_on_wrong_answers")
    @Expose
    private String time_spent_on_wrong_answers;
    @SerializedName("subject_id")
    @Expose
    private String subject_id;

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public String getWrong_answers() {
        return wrong_answers;
    }

    public void setWrong_answers(String wrong_answers) {
        this.wrong_answers = wrong_answers;
    }

    public String getNot_answered() {
        return not_answered;
    }

    public void setNot_answered(String not_answered) {
        this.not_answered = not_answered;
    }

    public String getTime_to_spend() {
        return time_to_spend;
    }

    public void setTime_to_spend(String time_to_spend) {
        this.time_to_spend = time_to_spend;
    }

    public String getTime_spent() {
        return time_spent;
    }

    public void setTime_spent(String time_spent) {
        this.time_spent = time_spent;
    }

    public String getTime_spent_on_correct_answers() {
        return time_spent_on_correct_answers;
    }

    public void setTime_spent_on_correct_answers(String time_spent_on_correct_answers) {
        this.time_spent_on_correct_answers = time_spent_on_correct_answers;
    }

    public String getTime_spent_on_wrong_answers() {
        return time_spent_on_wrong_answers;
    }

    public void setTime_spent_on_wrong_answers(String time_spent_on_wrong_answers) {
        this.time_spent_on_wrong_answers = time_spent_on_wrong_answers;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }
    
}
