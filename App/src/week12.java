import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    public ParkingSpot() {
        this.occupied = false;
    }
}

public class week12 {

    private ParkingSpot[] table;
    private int capacity;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int totalParks = 0;

    public week12(int capacity) {
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

        while (table[index].occupied) {
            index = (index + 1) % capacity; // linear probing
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        totalParks++;

        System.out.println("Vehicle " + licensePlate +
                " assigned spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (table[index].occupied) {

            if (licensePlate.equals(table[index].licensePlate)) {

                long exitTime = System.currentTimeMillis();

                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedCount--;

                System.out.println("Spot #" + index + " freed");
                System.out.println("Duration: "
                        + String.format("%.2f", hours) + " hours");
                System.out.println("Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy =
                (occupiedCount * 100.0) / capacity;

        double avgProbes =
                totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("Occupancy: "
                + String.format("%.2f", occupancy) + "%");

        System.out.println("Average Probes: "
                + String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) throws InterruptedException {

        week12 lot = new week12(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000); // simulate parking duration

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}