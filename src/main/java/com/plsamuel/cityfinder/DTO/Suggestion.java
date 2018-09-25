package com.plsamuel.cityfinder.DTO;


public class Suggestion {
    private String name;
    private String latitude;
    private String longitude;
    private Double score;

    public Suggestion(String name, String latitude, String longitude, Double score) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Double getScore() {
        return score;
    }
}
