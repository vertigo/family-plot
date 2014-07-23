package com.vertigo.familyplot.library;

public class Entry {
    String label;
    float value;

    public Entry(String label, float value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public float getValue() {
        return value;
    }
}
