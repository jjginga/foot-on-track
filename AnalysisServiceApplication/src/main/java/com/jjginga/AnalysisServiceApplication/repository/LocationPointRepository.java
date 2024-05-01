package com.jjginga.AnalysisServiceApplication.repository;

import com.jjginga.AnalysisServiceApplication.entity.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {
    List<LocationPoint> findAllByRunningSessionId(Long sessionId);
}
