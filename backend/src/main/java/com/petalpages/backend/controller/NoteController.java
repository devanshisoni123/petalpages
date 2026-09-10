package com.petalpages.backend.controller;

import com.petalpages.backend.dto.NoteRequest;
import com.petalpages.backend.model.Note;
import com.petalpages.backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    private boolean isOwner(Authentication auth, Long userId) {
        if (auth == null || auth.getPrincipal() == null) return false;
        Long tokenUserId = (Long) auth.getPrincipal();
        return tokenUserId.equals(userId);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createNote(@PathVariable Long userId, @RequestBody NoteRequest request, Authentication auth) {
        if (!isOwner(auth, userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setColor(request.getColor());
        note.setTags(request.getTags());
        note.setPinned(request.isPinned());
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getNotes(@PathVariable Long userId, Authentication auth) {
        if (!isOwner(auth, userId)) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        List<Note> notes = noteRepository.findByUserIdOrderByUpdatedAtDesc(userId);
        return ResponseEntity.ok(notes);
    }

    @PutMapping("/single/{noteId}")
    public ResponseEntity<?> updateNote(@PathVariable Long noteId, @RequestBody NoteRequest request, Authentication auth) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (noteOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Note not found"));
        if (!isOwner(auth, noteOpt.get().getUserId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        Note note = noteOpt.get();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setColor(request.getColor());
        note.setTags(request.getTags());
        note.setPinned(request.isPinned());
        note.setUpdatedAt(LocalDateTime.now());
        noteRepository.save(note);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/single/{noteId}")
    public ResponseEntity<?> deleteNote(@PathVariable Long noteId, Authentication auth) {
        Optional<Note> noteOpt = noteRepository.findById(noteId);
        if (noteOpt.isEmpty()) return ResponseEntity.status(404).body(Map.of("error", "Note not found"));
        if (!isOwner(auth, noteOpt.get().getUserId())) {
            return ResponseEntity.status(403).body(Map.of("error", "Not authorized"));
        }
        noteRepository.deleteById(noteId);
        return ResponseEntity.ok(Map.of("message", "Note deleted successfully"));
    }
}
