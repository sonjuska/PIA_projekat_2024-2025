package com.example.backend.modeli.responses;

import java.util.List;

public class ChartData {
    
    private String name;
    private List<ChartSeries> series;

    public ChartData(){};
    public ChartData(String name, List<ChartSeries> series) {
        this.name = name;
        this.series = series;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<ChartSeries> getSeries() {
        return series;
    }
    public void setSeries(List<ChartSeries> series) {
        this.series = series;
    }

    

}
