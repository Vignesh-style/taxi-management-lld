package in.co.app.taxi.service;

import in.co.app.taxi.models.Location;
import in.co.app.taxi.models.Taxi;
import in.co.app.taxi.models.TaxiState;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaxiService {

    private Map<Location, List<Integer>> taxiLocationRegistry = new HashMap<>();
    @Getter
    private Map<Integer, Taxi> taxiRegistry = new HashMap<>();

    public TaxiService() {
        // Initialize taxis at Location A
        for (int i = 0; i < 4; i++) {
            Taxi taxi = new Taxi();
            taxi.setTaxiNumber(i);
            taxi.setTaxiName("Taxi-" + i);
            taxi.setTaxiState(TaxiState.IDLE);
            taxi.setAvailableTime(System.currentTimeMillis());
            taxi.setLocation(Location.A);
            taxiRegistry.put(i, taxi);
            taxiLocationRegistry.computeIfAbsent(Location.A, k -> new ArrayList<>()).add(i);
        }

        for (Map.Entry<Location, List<Integer>> locationListEntry : taxiLocationRegistry.entrySet()) {

            locationListEntry.getValue().forEach(e -> {
                System.out.println("Location : " + locationListEntry.getKey() + " Taxi : " + taxiRegistry.get(e));
            });
        }

    }

    public List<Integer> getTaxisAt(Location location) {
        return taxiLocationRegistry.getOrDefault(location, Collections.emptyList());
    }

    public List<Taxi> getAvailableTaxi(List<Integer> taxiNumbers, long pickupTime) {

        List<Taxi> available = new ArrayList<>();

        for (Integer number : taxiNumbers) {
            Taxi taxi = taxiRegistry.get(number);
            if (taxi == null) continue;

            boolean isFree = taxi.getAvailableTime() <= pickupTime;

            if (isFree) {
                available.add(taxi);
            }
        }
        return available;
    }

    public Taxi findAvailableTaxisNear(Location pickupPoint, long pickupTime) {

        List<Taxi> forward = traverseForAvailableTaxisNearBy(pickupPoint, true, pickupTime);
        List<Taxi> backward = traverseForAvailableTaxisNearBy(pickupPoint, false, pickupTime);

        if (forward.isEmpty() && backward.isEmpty()) return null;

        Taxi nearestForward = forward.stream().min(Comparator.comparingDouble(Taxi::getTotalEarnings)).orElse(null);
        Taxi nearestBackward = backward.stream().min(Comparator.comparingDouble(Taxi::getTotalEarnings)).orElse(null);

        int minDistance = Integer.MAX_VALUE;
        Taxi nearest = null;

        if (nearestForward != null) {
            minDistance = (nearestForward.getLocation().ordinal() - pickupPoint.ordinal()) * 15;
            nearest = nearestForward;
        }

        if (nearestBackward != null) {
            int backwardDistance = (pickupPoint.ordinal() - nearestBackward.getLocation().ordinal()) * 15;
            if (backwardDistance < minDistance) {
                nearest = nearestBackward;
            }
        }

        return nearest;
    }

    private List<Taxi> traverseForAvailableTaxisNearBy(Location pickupPoint, boolean forward, long pickupTime) {

        List<Taxi> nearby = new ArrayList<>();
        Location[] all = Location.values();
        int index = pickupPoint.ordinal();

        if (forward) {
            for (int i = index + 1; i < all.length; i++) {
                nearby.addAll(getAvailableTaxi(getTaxisAt(all[i]), pickupTime));
                if (!nearby.isEmpty()) break;
            }
        } else {
            for (int i = index - 1; i >= 0; i--) {
                nearby.addAll(getAvailableTaxi(getTaxisAt(all[i]), pickupTime));
                if (!nearby.isEmpty()) break;
            }
        }

        return nearby;
    }

    public Map<Integer, Taxi> getRegistry() {
        return getTaxiRegistry();
    }
}
