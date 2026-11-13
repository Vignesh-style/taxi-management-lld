package in.co.app.taxi.models;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Booking {
    private String bookingId;
    private String taxiNumber;
    private String pickUpPoint;
    private String dropPoint;
    private LocalDateTime pickUpTime;
    private LocalDateTime dropTime;
    private Long estimatedTravelTime;
    private Double fare;
    private String customerId;
}
