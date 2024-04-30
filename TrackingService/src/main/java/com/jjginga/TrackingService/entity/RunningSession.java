package com.jjginga.TrackingService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RunningSession {

    public static final String ENDED = "status_ended";
    public static final String STARTED = "status_started";
    public static final String PAUSED = "status_paused";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

}
