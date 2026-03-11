import java.util.*;

public class Problem8 {

    // Parking spot status
    enum Status {
        EMPTY,
        OCCUPIED,
        DELETED
    }

    // Parking Spot class
    class ParkingSpot {
        String licensePlate;
        long entryTime;
        Status status;

        ParkingSpot() {
            status = Status.EMPTY;
        }
    }

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedSpots = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public Problem8(int capacity) {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].status == Status.OCCUPIED) {
            index = (index + 1) % capacity;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = Status.OCCUPIED;

        occupiedSpots++;
        totalProbes += probes;
        totalParks++;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].status != Status.EMPTY) {

            if (table[index].status == Status.OCCUPIED &&
                    table[index].licensePlate.equals(licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);
                double fee = hours * 5.5; // $5.5 per hour

                table[index].status = Status.DELETED;
                occupiedSpots--;

                System.out.printf(
                        "exitVehicle(\"%s\") → Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        licensePlate, index, hours, fee);

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    // Find nearest available spot (simple scan)
    public int findNearestSpot() {

        for (int i = 0; i < capacity; i++) {
            if (table[i].status != Status.OCCUPIED) {
                return i;
            }
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedSpots * 100.0) / capacity;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\nParking Statistics:");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }

    public static void main(String[] args) throws InterruptedException {

        Problem8 parkingLot = new Problem8(10);

        parkingLot.parkVehicle("ABC-1234");
        parkingLot.parkVehicle("ABC-1235");
        parkingLot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking time

        parkingLot.exitVehicle("ABC-1234");

        parkingLot.getStatistics();
    }
}