package com.jjginga.TrainingPlan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlan {

    public static final String ONGOING = "ongoing";
    public static final String COMPLETED = "completed";
    public static final String CANCELED = "canceled";
    public static final String TOSTART = "starting";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String description;
    private String status;

}
