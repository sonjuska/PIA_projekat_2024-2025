package com.example.backend.modeli.responses;

public class ChartSeries {
    private String name;
    private int value;

    public ChartSeries(){}
    public ChartSeries(String name, int value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    
}
