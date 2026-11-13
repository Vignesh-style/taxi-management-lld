package in.co.app.taxi.models;

import lombok.Data;

@Data
public class Taxi {
    private Integer taxiNumber;
    private String taxiName;
    private TaxiState taxiState;
    private Long availableTime;
    private Location location;
    private Double totalEarnings = 0.0;
}
