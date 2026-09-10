package com.petalpages.backend.dto;

public class JournalEntryRequest {
    private String title;
    private String content;
    private String mood;
    private String theme;
    private String tags;
    private boolean locked;
    private String lockPassword;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public String getLockPassword() { return lockPassword; }
    public void setLockPassword(String lockPassword) { this.lockPassword = lockPassword; }
}