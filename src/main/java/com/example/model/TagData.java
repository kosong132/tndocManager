package com.example.model;

public class TagData {
    private String tag;
    private String cusdecData;
    private String cusresData;

    public TagData(String tag, String cusdecData, String cusresData) {
        this.tag = tag;
        this.cusdecData = cusdecData;
        this.cusresData = cusresData;
    }

    // Getters and setters
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCusdecData() {
        return cusdecData;
    }

    public void setCusdecData(String cusdecData) {
        this.cusdecData = cusdecData;
    }

    public String getCusresData() {
        return cusresData;
    }

    public void setCusresData(String cusresData) {
        this.cusresData = cusresData;
    }
}
