package com.jjginga.TrackingService.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElevationResponse {

    @JsonProperty("results")
    private List<Elevation> elevations;

    @Getter
    @Setter
    @Data
    public static class Elevation {
        private Double latitude;
        private Double longitude;
        private Double elevation;
    }

}
