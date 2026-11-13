package in.co.app.taxi.service;

import in.co.app.taxi.models.Booking;
import in.co.app.taxi.models.Location;
import in.co.app.taxi.models.Taxi;
import in.co.app.taxi.models.TaxiState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TaxiBookingService {

    @Autowired
    TaxiService taxiService;

    public Booking book(String customerId, Location pickupPoint, Location dropPoint, Long pickupTime) {

        List<Integer> taxiNumbersAtPickup = taxiService.getTaxisAt(pickupPoint);

        List<Taxi> availableAtPickup = taxiService.getAvailableTaxi(taxiNumbersAtPickup, pickupTime);

        Taxi assignedTaxi;

        // 3️⃣ Prefer available taxi at pickup location which low earning.
        if (!availableAtPickup.isEmpty()) {
            assignedTaxi = availableAtPickup.stream()
                    .min(Comparator.comparingDouble(Taxi::getTotalEarnings))
                    .orElse(null);
        } else {
            // 4️⃣ Otherwise search nearby locations (forward & backward)
            assignedTaxi = taxiService.findAvailableTaxisNear(pickupPoint, pickupTime);
        }

        if (assignedTaxi == null) {
            return null;
        }

        // 5️⃣ Calculate distance between pickup and drop (only this counts for fare)
        int distanceKm = Math.abs(dropPoint.ordinal() - pickupPoint.ordinal()) * 15;
        double fare = calculateFare(distanceKm);

        // 6️⃣ Estimate travel time (example: 1 minute per km)
        long estimatedTravelMillis = distanceKm * 60_000L;

        // 7️⃣ Update taxi state
        assignedTaxi.setTaxiState(TaxiState.BUSY);
        assignedTaxi.setAvailableTime(pickupTime + estimatedTravelMillis);
        assignedTaxi.setLocation(dropPoint);
        assignedTaxi.setTotalEarnings(assignedTaxi.getTotalEarnings() + fare);

        // 8️⃣ Create booking record
        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setTaxiNumber("Taxi-" + assignedTaxi.getTaxiNumber());
        booking.setPickUpPoint(pickupPoint.name());
        booking.setDropPoint(dropPoint.name());
        booking.setFare(fare);
        booking.setEstimatedTravelTime(estimatedTravelMillis);

        return booking;
    }

    private double calculateFare(double distanceKm) {
        double baseFare = 100.0;
        double baseDistance = 5.0;
        double ratePerKm = 10.0;

        if (distanceKm <= baseDistance) {
            return baseFare;
        }
        return baseFare + (distanceKm - baseDistance) * ratePerKm;
    }
}
