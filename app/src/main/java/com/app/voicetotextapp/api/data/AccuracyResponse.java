package com.app.voicetotextapp.api.data;

import com.google.gson.annotations.SerializedName;

public class AccuracyResponse {

    @SerializedName("time")
    private String time;

    @SerializedName("similarity")
    private String similarity;

    @SerializedName("lang")
    private String lang;

    @SerializedName("langConfidence")
    private String langConfidence;

    @SerializedName("timestamp")
    private String timestamp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLangConfidence() {
        return langConfidence;
    }

    public void setLangConfidence(String langConfidence) {
        this.langConfidence = langConfidence;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}