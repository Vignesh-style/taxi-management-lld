package in.co.app.taxi.controller;

import in.co.app.taxi.dto.BookingRequestDTO;
import in.co.app.taxi.dto.BookingResponseDTO;
import in.co.app.taxi.models.Booking;
import in.co.app.taxi.models.Taxi;
import in.co.app.taxi.service.TaxiBookingService;
import in.co.app.taxi.service.TaxiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Collection;

@RestController
@Validated
@RequestMapping("/v1/taxi/service/")
public class TaxiBookingController {

    @Autowired
    TaxiBookingService taxiBookingService;

    @Autowired
    TaxiService taxiService;

    @PostMapping("/new")
    public BookingResponseDTO bookTaxi(@RequestBody @Validated BookingRequestDTO request) {

        if (request.getPickupPoint() == request.getDropPoint()) {
            return new BookingResponseDTO("Pickup and Drop points cannot be the same", null, 0);
        }

        if (request.getPickupTime() <= Instant.now().toEpochMilli()) {
            return new BookingResponseDTO("Pickup time must be in the future", null, 0);
        }

        Booking booking = taxiBookingService.book(
                request.getCustomerId(),
                request.getPickupPoint(),
                request.getDropPoint(),
                request.getPickupTime()
        );

        if (booking == null) {
            return new BookingResponseDTO("No taxis available at the moment", null, 0);
        }

        return new BookingResponseDTO("Taxi booked successfully",
                booking.getTaxiNumber(),
                booking.getFare());
    }

    @GetMapping("/all")
    public Collection<Taxi> getAllTaxis() {
        return taxiService.getRegistry().values();
    }
}
