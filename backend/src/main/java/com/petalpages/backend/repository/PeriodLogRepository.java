package com.petalpages.backend.repository;

import com.petalpages.backend.model.PeriodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PeriodLogRepository extends JpaRepository<PeriodLog, Long> {
    List<PeriodLog> findByUserIdOrderByStartDateDesc(Long userId);
}