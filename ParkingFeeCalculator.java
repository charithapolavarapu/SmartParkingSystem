public class ParkingFeeCalculator {

    public double calculateFee(String vehicleType, long hours) {

        double rate;

        if (vehicleType.equalsIgnoreCase("Bike")) {
            rate = 10;
        }
        else if (vehicleType.equalsIgnoreCase("Car")) {
            rate = 30;
        }
        else if (vehicleType.equalsIgnoreCase("EV")) {
            rate = 20;
        }
        else if (vehicleType.equalsIgnoreCase("Truck")) {
            rate = 50;
        }
        else {
            rate = 30;
        }

        // Minimum charge is for 1 hour
        if (hours <= 0) {
            hours = 1;
        }

        return rate * hours;
    }
}