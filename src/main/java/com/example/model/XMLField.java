package com.example.model;

public class XMLField {
    private String id;
    private String mapping;
    private String value;

    // Constructors
    public XMLField(String id, String mapping, String value) {
        this.id = id;
        this.mapping = mapping;
        this.value = value;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
