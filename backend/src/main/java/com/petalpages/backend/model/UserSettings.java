package com.petalpages.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    private Long userId; // same as User's id — one settings row per user

    private String displayName;
    private String avatarUrl;
    private boolean darkMode = false;
    private String accentColor;
    private String font;
    private boolean ruledLines = true;
    private boolean appPasswordEnabled = false;
    private String appPassword;
    private boolean hidePreviews = false;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }
    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }
    public String getFont() { return font; }
    public void setFont(String font) { this.font = font; }
    public boolean isRuledLines() { return ruledLines; }
    public void setRuledLines(boolean ruledLines) { this.ruledLines = ruledLines; }
    public boolean isAppPasswordEnabled() { return appPasswordEnabled; }
    public void setAppPasswordEnabled(boolean appPasswordEnabled) { this.appPasswordEnabled = appPasswordEnabled; }
    public String getAppPassword() { return appPassword; }
    public void setAppPassword(String appPassword) { this.appPassword = appPassword; }
    public boolean isHidePreviews() { return hidePreviews; }
    public void setHidePreviews(boolean hidePreviews) { this.hidePreviews = hidePreviews; }
}