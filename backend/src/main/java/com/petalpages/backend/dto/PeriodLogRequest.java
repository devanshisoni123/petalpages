package com.petalpages.backend.dto;

import java.time.LocalDate;

public class PeriodLogRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer cycleLength;
    private String symptoms;
    private String mood;

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getCycleLength() { return cycleLength; }
    public void setCycleLength(Integer cycleLength) { this.cycleLength = cycleLength; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
}