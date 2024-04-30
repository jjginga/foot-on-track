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
@Table(name = "location_points")
public class LocationPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double latitude;

    private double longitude;

    private double elevation;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "running_session_id", referencedColumnName = "id")
    private RunningSession runningSession;
}
