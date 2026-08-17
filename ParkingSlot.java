public class ParkingSlot {

    private String slotId;
    private String vehicleType;
    private boolean occupied;
    private Vehicle vehicle;

    public ParkingSlot(String slotId, String vehicleType) {

        this.slotId = slotId;
        this.vehicleType = vehicleType;
        this.occupied = false;
        this.vehicle = null;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void parkVehicle(Vehicle vehicle) {

        this.vehicle = vehicle;
        this.occupied = true;
    }

    public void removeVehicle() {

        this.vehicle = null;
        this.occupied = false;
    }
}