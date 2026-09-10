package com.petalpages.backend.controller;

import com.petalpages.backend.dto.UserSettingsRequest;
import com.petalpages.backend.model.UserSettings;
import com.petalpages.backend.repository.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
@CrossOrigin(origins = "*")
public class SettingsController {

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    private boolean isOwner(Authentication auth, Long userId) {
        if (auth == null || auth.getPrincipal() == null) return false;
        Long tokenUserId = (Long) auth.getPrincipal();
        return tokenUserId.equals(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getSettings(@PathVariable Long userId, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        Optional<UserSettings> existing = userSettingsRepository.findById(userId);
        if (existing.isPresent()) return ResponseEntity.ok(existing.get());
        UserSettings blank = new UserSettings();
        blank.setUserId(userId);
        return ResponseEntity.ok(blank);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> saveSettings(@PathVariable Long userId, @RequestBody UserSettingsRequest request, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        UserSettings settings = userSettingsRepository.findById(userId).orElse(new UserSettings());
        settings.setUserId(userId);
        settings.setDisplayName(request.getDisplayName());
        settings.setAvatarUrl(request.getAvatarUrl());
        settings.setDarkMode(request.isDarkMode());
        settings.setAccentColor(request.getAccentColor());
        settings.setFont(request.getFont());
        settings.setRuledLines(request.isRuledLines());
        settings.setAppPasswordEnabled(request.isAppPasswordEnabled());
        settings.setAppPassword(request.getAppPassword());
        settings.setHidePreviews(request.isHidePreviews());
        userSettingsRepository.save(settings);
        return ResponseEntity.ok(settings);
    }
}