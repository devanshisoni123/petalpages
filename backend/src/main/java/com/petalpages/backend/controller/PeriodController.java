package com.petalpages.backend.controller;

import com.petalpages.backend.dto.DailyMoodRequest;
import com.petalpages.backend.dto.PeriodLogRequest;
import com.petalpages.backend.model.DailyMood;
import com.petalpages.backend.model.PeriodLog;
import com.petalpages.backend.repository.DailyMoodRepository;
import com.petalpages.backend.repository.PeriodLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/period")
@CrossOrigin(origins = "*")
public class PeriodController {

    @Autowired
    private PeriodLogRepository periodLogRepository;

    @Autowired
    private DailyMoodRepository dailyMoodRepository;

    private boolean isOwner(Authentication auth, Long userId) {
        if (auth == null || auth.getPrincipal() == null) return false;
        Long tokenUserId = (Long) auth.getPrincipal();
        return tokenUserId.equals(userId);
    }

    // ── PERIOD LOGS ──

    @PostMapping("/logs/{userId}")
    public ResponseEntity<?> createLog(@PathVariable Long userId, @RequestBody PeriodLogRequest request, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        PeriodLog log = new PeriodLog();
        log.setUserId(userId);
        log.setStartDate(request.getStartDate());
        log.setEndDate(request.getEndDate());
        log.setCycleLength(request.getCycleLength());
        log.setSymptoms(request.getSymptoms());
        log.setMood(request.getMood());
        periodLogRepository.save(log);
        return ResponseEntity.ok(log);
    }

    @GetMapping("/logs/{userId}")
    public ResponseEntity<?> getLogs(@PathVariable Long userId, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        List<PeriodLog> logs = periodLogRepository.findByUserIdOrderByStartDateDesc(userId);
        return ResponseEntity.ok(logs);
    }

    @DeleteMapping("/logs/single/{logId}")
    public ResponseEntity<?> deleteLog(@PathVariable Long logId, Authentication auth) {
        Optional<PeriodLog> logOpt = periodLogRepository.findById(logId);
        if (logOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Log not found"));
        if (!isOwner(auth, logOpt.get().getUserId())) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        periodLogRepository.deleteById(logId);
        return ResponseEntity.ok(Map.of("message", "Log deleted successfully"));
    }

    // ── DAILY MOODS ──

    @PostMapping("/moods/{userId}")
    public ResponseEntity<?> setMood(@PathVariable Long userId, @RequestBody DailyMoodRequest request, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        Optional<DailyMood> existing = dailyMoodRepository.findByUserIdAndDate(userId, request.getDate());
        DailyMood dm = existing.orElse(new DailyMood());
        dm.setUserId(userId);
        dm.setDate(request.getDate());
        dm.setMood(request.getMood());
        dailyMoodRepository.save(dm);
        return ResponseEntity.ok(dm);
    }

    @GetMapping("/moods/{userId}")
    public ResponseEntity<?> getMoods(@PathVariable Long userId, Authentication auth) {
        if (!isOwner(auth, userId)) return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        List<DailyMood> moods = dailyMoodRepository.findByUserId(userId);
        return ResponseEntity.ok(moods);
    }
}