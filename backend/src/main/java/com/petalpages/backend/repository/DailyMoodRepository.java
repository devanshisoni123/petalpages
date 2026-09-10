package com.petalpages.backend.repository;

import com.petalpages.backend.model.DailyMood;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface DailyMoodRepository extends JpaRepository<DailyMood, Long> {
    List<DailyMood> findByUserId(Long userId);
    Optional<DailyMood> findByUserIdAndDate(Long userId, LocalDate date);
}