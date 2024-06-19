package com.jjginga.TrackingService.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleRunningSession {

    private String userId;
    private String time;

    @Override
    public String toString() {
        return "RunningSessionStarter{" +
                "userId='" + userId + '\'' +
                ", startTime=" + time +
                '}';
    }
}
