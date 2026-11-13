package in.co.app.taxi.dto;

import in.co.app.taxi.models.Location;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotBlank(message = "Customer ID cannot be empty")
    private String customerId;

    @NotNull(message = "Pickup point cannot be null")
    private Location pickupPoint;

    @NotNull(message = "Drop point cannot be null")
    private Location dropPoint;

    @NotNull(message = "Pickup time cannot be null")
    private Long pickupTime;


}