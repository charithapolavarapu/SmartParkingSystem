import java.time.LocalDateTime;

public class ParkingRecord {

    private Vehicle vehicle;
    private ParkingSlot slot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double parkingFee;

    public ParkingRecord(Vehicle vehicle, ParkingSlot slot) {
        this.vehicle = vehicle;
        this.slot = slot;
        this.entryTime = LocalDateTime.now();
        this.parkingFee = 0;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }
    public void setEntryTime(LocalDateTime entryTime) {
    this.entryTime = entryTime;
}

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getParkingFee() {
        return parkingFee;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setParkingFee(double parkingFee) {
        this.parkingFee = parkingFee;
    }

    public void displayRecord() {

        System.out.println("----------------------------");
        System.out.println("Vehicle Number : " +
                vehicle.getVehicleNumber());

        System.out.println("Vehicle Type   : " +
                vehicle.getVehicleType());

        System.out.println("Slot           : " +
                slot.getSlotId());

        System.out.println("Entry Time     : " +
                entryTime);

        System.out.println("Exit Time      : " +
                (exitTime == null ? "Still Parked" : exitTime));

System.out.println("Parking Fee    : Rs." +
        parkingFee);

        System.out.println("----------------------------");
    }
}