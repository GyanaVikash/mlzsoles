
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KeyTakeExam implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subject_id")
    @Expose
    private String subject_id;
    @SerializedName("topic_id")
    @Expose
    private String topic_id;
    @SerializedName("question_tags")
    @Expose
    private Question_tags question_tags;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("question_type")
    @Expose
    private String question_type;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("question_file")
    @Expose
    private String question_file;
    @SerializedName("question_file_is_url")
    @Expose
    private String question_file_is_url;
    @SerializedName("total_answers")
    @Expose
    private String total_answers;
    @SerializedName("answers")
    @Expose
    private String answers;
    @SerializedName("total_correct_answers")
    @Expose
    private String total_correct_answers;
    @SerializedName("correct_answers")
    @Expose
    private String correct_answers;
    @SerializedName("marks")
    @Expose
    private String marks;
    @SerializedName("time_to_spend")
    @Expose
    private String time_to_spend;
    @SerializedName("difficulty_level")
    @Expose
    private String difficulty_level;
    @SerializedName("hint")
    @Expose
    private String hint;
    @SerializedName("explanation")
    @Expose
    private String explanation;
    @SerializedName("explanation_file")
    @Expose
    private String explanation_file;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("question_l2")
    @Expose
    private String question_l2;
    @SerializedName("explanation_l2")
    @Expose
    private String explanation_l2;
    @SerializedName("user_submitted")
    @Expose
    private String user_submitted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public Question_tags getQuestion_tags() {
        return question_tags;
    }

    public void setQuestion_tags(Question_tags question_tags) {
        this.question_tags = question_tags;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion_file() {
        return question_file;
    }

    public void setQuestion_file(String question_file) {
        this.question_file = question_file;
    }

    public String getQuestion_file_is_url() {
        return question_file_is_url;
    }

    public void setQuestion_file_is_url(String question_file_is_url) {
        this.question_file_is_url = question_file_is_url;
    }

    public String getTotal_answers() {
        return total_answers;
    }

    public void setTotal_answers(String total_answers) {
        this.total_answers = total_answers;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    public String getTotal_correct_answers() {
        return total_correct_answers;
    }

    public void setTotal_correct_answers(String total_correct_answers) {
        this.total_correct_answers = total_correct_answers;
    }

    public String getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getTime_to_spend() {
        return time_to_spend;
    }

    public void setTime_to_spend(String time_to_spend) {
        this.time_to_spend = time_to_spend;
    }

    public String getDifficulty_level() {
        return difficulty_level;
    }

    public void setDifficulty_level(String difficulty_level) {
        this.difficulty_level = difficulty_level;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanation_file() {
        return explanation_file;
    }

    public void setExplanation_file(String explanation_file) {
        this.explanation_file = explanation_file;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getQuestion_l2() {
        return question_l2;
    }

    public void setQuestion_l2(String question_l2) {
        this.question_l2 = question_l2;
    }

    public String getExplanation_l2() {
        return explanation_l2;
    }

    public void setExplanation_l2(String explanation_l2) {
        this.explanation_l2 = explanation_l2;
    }

    public String getUser_submitted() {
        return user_submitted;
    }

    public void setUser_submitted(String user_submitted) {
        this.user_submitted = user_submitted;
    }

}
