
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Options {

    @SerializedName("option_value")
    @Expose
    private String option_value;
    @SerializedName("optionl2_value")
    @Expose
    private String optionl2_value;
    @SerializedName("has_file")
    @Expose
    private String has_file;
    @SerializedName("file_name")
    @Expose
    private String file_name;

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    public String getOptionl2_value() {
        return optionl2_value;
    }

    public void setOptionl2_value(String optionl2_value) {
        this.optionl2_value = optionl2_value;
    }

    public String getHas_file() {
        return has_file;
    }

    public void setHas_file(String has_file) {
        this.has_file = has_file;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

}
