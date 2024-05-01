package com.jjginga.AnalysisServiceApplication.repository;

import com.jjginga.AnalysisServiceApplication.entity.RunningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningSessionRepository extends JpaRepository<RunningSession, Long> {
    List<RunningSession> findAllByUserId(Long userId);

}
