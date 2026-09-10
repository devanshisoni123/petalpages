package com.petalpages.backend.controller;

import com.petalpages.backend.dto.JournalEntryRequest;
import com.petalpages.backend.model.JournalEntry;
import com.petalpages.backend.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin(origins = "*")
public class JournalEntryController {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    private boolean isOwner(Authentication auth, Long userId) {
        if (auth == null || auth.getPrincipal() == null) return false;
        Long tokenUserId = (Long) auth.getPrincipal();
        return tokenUserId.equals(userId);
    }

   @PostMapping("/{userId}")
public ResponseEntity<?> createEntry(@PathVariable Long userId, @RequestBody JournalEntryRequest request, Authentication auth) {
    if (!isOwner(auth, userId)) {
        return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
    }
    JournalEntry entry = new JournalEntry();
    entry.setUserId(userId);
    entry.setTitle(request.getTitle());
    entry.setContent(request.getContent());
    entry.setMood(request.getMood());
    entry.setTheme(request.getTheme());
    entry.setTags(request.getTags());
    entry.setLocked(request.isLocked());
    entry.setLockPassword(request.getLockPassword());
    journalEntryRepository.save(entry);
    return ResponseEntity.ok(entry);
}

    @GetMapping("/{userId}")
    public ResponseEntity<?> getEntries(@PathVariable Long userId, Authentication auth) {
        if (!isOwner(auth, userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        List<JournalEntry> entries = journalEntryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/single/{entryId}")
    public ResponseEntity<?> getEntry(@PathVariable Long entryId, Authentication auth) {
        Optional<JournalEntry> entry = journalEntryRepository.findById(entryId);
        if (entry.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Entry not found"));
        if (!isOwner(auth, entry.get().getUserId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        return ResponseEntity.ok(entry.get());
    }

    @PutMapping("/single/{entryId}")
public ResponseEntity<?> updateEntry(@PathVariable Long entryId, @RequestBody JournalEntryRequest request, Authentication auth) {
    Optional<JournalEntry> entryOpt = journalEntryRepository.findById(entryId);
    if (entryOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Entry not found"));
    if (!isOwner(auth, entryOpt.get().getUserId())) {
        return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
    }
    JournalEntry entry = entryOpt.get();
    entry.setTitle(request.getTitle());
    entry.setContent(request.getContent());
    entry.setMood(request.getMood());
    entry.setTheme(request.getTheme());
    entry.setTags(request.getTags());
    entry.setLocked(request.isLocked());
    entry.setLockPassword(request.getLockPassword());
    entry.setUpdatedAt(LocalDateTime.now());
    journalEntryRepository.save(entry);
    return ResponseEntity.ok(entry);
}

    @DeleteMapping("/single/{entryId}")
    public ResponseEntity<?> deleteEntry(@PathVariable Long entryId, Authentication auth) {
        Optional<JournalEntry> entryOpt = journalEntryRepository.findById(entryId);
        if (entryOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Entry not found"));
        if (!isOwner(auth, entryOpt.get().getUserId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        journalEntryRepository.deleteById(entryId);
        return ResponseEntity.ok(Map.of("message", "Entry deleted successfully"));
    }
}