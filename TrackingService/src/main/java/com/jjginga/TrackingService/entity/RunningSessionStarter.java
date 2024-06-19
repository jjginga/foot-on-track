package com.jjginga.TrackingService.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RunningSessionStarter {

    private String userId;
    private String startTime;

    @Override
    public String toString() {
        return "RunningSessionStarter{" +
                "userId='" + userId + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
