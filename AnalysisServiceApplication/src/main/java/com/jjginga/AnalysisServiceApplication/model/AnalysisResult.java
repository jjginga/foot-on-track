package com.jjginga.AnalysisServiceApplication.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    private double time5km;
    private double time10km;
    private double timeHalfMarathon;
    private double timeMarathon;

}
