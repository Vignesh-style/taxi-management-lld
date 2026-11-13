
📋 Requirements Overview

Taxi and Location Setup

There are six points: A, B, C, D, E, F in a straight line, each 15 km apart.

Initially, all taxis are stationed at point A.

Booking Request. Each booking includes: 
- Customer ID
- Pickup point (one of A–F)
- Drop point (one of A–F, different from pickup)
- Pickup time

Taxi Allocation Logic

First preference: Assign a free taxi at the pickup point if available.

Second preference: If no free taxi at the pickup point, assign from the nearest point.

Third preference: If multiple taxis are free at the same point, choose the one with lower total earnings.

Booking Rejection

If no taxi is free or available at the required time, the booking is rejected.

Fare Calculation

Minimum charge: ₹100 for the first 5 km.

Additional charge: ₹10 per km for every km beyond 5 km.

Taxi State Tracking

For each taxi, maintain:

Current location

Next available time

Total earnings

Trip Records (Optional)

Keep a record of each trip for reporting or analytics.