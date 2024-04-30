package com.jjginga.TrackingService.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElevationResponse {

    private List<Elevation> elevations;

    @Getter
    @Setter
    public static class Elevation {
        private Double latitude;
        private Double longitude;
        private Double elevation;
    }

}
