package com.petalpages.backend.dto;

import java.time.LocalDate;

public class DailyMoodRequest {
    private LocalDate date;
    private String mood;

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
}