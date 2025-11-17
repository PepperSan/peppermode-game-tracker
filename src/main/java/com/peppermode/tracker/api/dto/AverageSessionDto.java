package com.peppermode.tracker.api.dto;

public class AverageSessionDto {

    private final double averageMinutes;

    public AverageSessionDto(double averageMinutes) {
        this.averageMinutes = averageMinutes;
    }

    public double getAverageMinutes() {
        return averageMinutes;
    }
}

