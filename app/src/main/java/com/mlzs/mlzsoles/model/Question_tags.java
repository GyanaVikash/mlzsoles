
package com.mlzs.mlzsoles.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question_tags implements Serializable {

    @SerializedName("sno")
    @Expose
    private String sno;
    @SerializedName("is_bookmarked")
    @Expose
    private int is_bookmarked;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public int getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(int is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

}
