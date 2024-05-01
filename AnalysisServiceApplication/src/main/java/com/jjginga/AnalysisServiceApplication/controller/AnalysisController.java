package com.jjginga.AnalysisServiceApplication.controller;

import com.jjginga.AnalysisServiceApplication.model.AnalysisResult;
import com.jjginga.AnalysisServiceApplication.model.CurrentAnalysisResult;
import com.jjginga.AnalysisServiceApplication.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/performance/{userId}")
    public ResponseEntity<AnalysisResult> getPerformanceAnalysis(@PathVariable Long userId) {
        try {
            AnalysisResult result = analysisService.predictPerformance(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<CurrentAnalysisResult> analyzeSession(@PathVariable Long sessionId) {
        try {
            CurrentAnalysisResult result = analysisService.analyzeSession(sessionId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}