package com.jjginga.TrainingPlan.repository;

import com.jjginga.TrainingPlan.entity.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    List<TrainingPlan> findAllByUserId(String userId);
}
