package com.jjginga.TrackingService.repository;

import com.jjginga.TrackingService.entity.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationPointRepository extends JpaRepository<LocationPoint, Long> {
    List<LocationPoint> findAllByRunningSessionId(Long sessionId);
}
