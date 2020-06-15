
package com.mlzs.mlzsoles.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paragraph {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("questionl2")
    @Expose
    private String questionl2;
    @SerializedName("total_options")
    @Expose
    private String total_options;
    @SerializedName("options")
    @Expose
    private List<String> options = null;
    @SerializedName("optionsl2")
    @Expose
    private List<String> optionsl2 = null;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionl2() {
        return questionl2;
    }

    public void setQuestionl2(String questionl2) {
        this.questionl2 = questionl2;
    }

    public String getTotal_options() {
        return total_options;
    }

    public void setTotal_options(String total_options) {
        this.total_options = total_options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public List<String> getOptionsl2() {
        return optionsl2;
    }

    public void setOptionsl2(List<String> optionsl2) {
        this.optionsl2 = optionsl2;
    }

}
