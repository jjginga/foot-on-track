package com.jjginga.TrackingService.client;

import com.jjginga.TrackingService.model.ElevationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "elevation-api", url = "https://api.open-elevation.com")
public interface ElevationClient {

    @GetMapping("/api/v1/lookup")
    ElevationResponse getElevation(@RequestParam("locations") String locations);
}