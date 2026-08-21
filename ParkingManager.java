import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParkingManager {

    private ArrayList<ParkingSlot> slots;
    private ArrayList<ParkingRecord> records;

    private ParkingFeeCalculator feeCalculator;

    public ParkingManager() {

        slots = new ArrayList<>();
        records = new ArrayList<>();
        feeCalculator = new ParkingFeeCalculator();

        loadParkingSlots();
        loadParkingRecords();
    }

    // ============================================
    // LOAD PARKING SLOTS FROM MYSQL
    // ============================================

    private void loadParkingSlots() {

        String sql =
            "SELECT slot_number, vehicle_type, is_occupied " +
            "FROM parking_slots";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String slotNumber =
                    rs.getString("slot_number");

                String vehicleType =
                    rs.getString("vehicle_type");

                boolean occupied =
                    rs.getBoolean("is_occupied");

                ParkingSlot slot =
                    new ParkingSlot(
                        slotNumber,
                        vehicleType
                    );

                if (occupied) {

                    System.out.println(
                        "Slot " + slotNumber +
                        " is occupied in database."
                    );
                }

                slots.add(slot);
            }

            System.out.println(
                "Parking slots loaded from MySQL."
            );

        } catch (Exception e) {

            System.out.println(
                "Error loading parking slots."
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // LOAD PARKING RECORDS FROM MYSQL
    // ============================================

    private void loadParkingRecords() {

        String sql =
            "SELECT vehicle_number, slot_number, " +
            "entry_time, exit_time, parking_fee " +
            "FROM parking_records";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String vehicleNumber =
                    rs.getString("vehicle_number");

                String slotNumber =
                    rs.getString("slot_number");

                LocalDateTime entryTime =
                    rs.getObject(
                        "entry_time",
                        LocalDateTime.class
                    );

                LocalDateTime exitTime =
                    rs.getObject(
                        "exit_time",
                        LocalDateTime.class
                    );

                double parkingFee =
                    rs.getDouble("parking_fee");

                // Find vehicle from database
                Vehicle vehicle =
                    loadVehicle(vehicleNumber);

                // Find slot from loaded slots
                ParkingSlot slot =
                    findSlotById(slotNumber);

                if (vehicle != null && slot != null) {

                    ParkingRecord record =
                        new ParkingRecord(
                            vehicle,
                            slot
                        );

                    record.setEntryTime(entryTime);
                    record.setExitTime(exitTime);
                    record.setParkingFee(parkingFee);

                    records.add(record);

                    // If vehicle is still parked,
                    // mark slot as occupied in Java
                    if (exitTime == null) {

                        slot.parkVehicle(vehicle);
                    }

                    System.out.println(
                        "Parking record loaded: "
                        + vehicleNumber
                    );
                }
            }

            System.out.println(
                "Parking records loaded from MySQL."
            );

        } catch (Exception e) {

            System.out.println(
                "Error loading parking records."
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // LOAD VEHICLE FROM MYSQL
    // ============================================

    private Vehicle loadVehicle(
        String vehicleNumber
    ) {

        String sql =
            "SELECT vehicle_number, owner_name, vehicle_type " +
            "FROM vehicles " +
            "WHERE vehicle_number = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(
                1,
                vehicleNumber
            );

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Vehicle(
                        rs.getString("vehicle_number"),
                        rs.getString("owner_name"),
                        rs.getString("vehicle_type")
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                "Error loading vehicle."
            );

            e.printStackTrace();
        }

        return null;
    }

    // ============================================
    // FIND SLOT BY ID
    // ============================================

    private ParkingSlot findSlotById(
        String slotNumber
    ) {

        for (ParkingSlot slot : slots) {

            if (slot.getSlotId()
                    .equalsIgnoreCase(slotNumber)) {

                return slot;
            }
        }

        return null;
    }

    // ============================================
    // ADD PARKING SLOT
    // ============================================

    public void addSlot(
        ParkingSlot slot
    ) {

        slots.add(slot);
    }

    // ============================================
    // DISPLAY PARKING SLOTS
    // ============================================

    public void displaySlots() {

        System.out.println(
            "\n===== PARKING SLOTS ====="
        );

        for (ParkingSlot slot : slots) {

            System.out.println(
                slot.getSlotId() + " - " +
                slot.getVehicleType() + " - " +
                (slot.isOccupied()
                    ? "Occupied"
                    : "Available")
            );
        }
    }

    // ============================================
    // FIND AVAILABLE SLOT
    // ============================================

    public ParkingSlot findAvailableSlot(
        String vehicleType
    ) {

        for (ParkingSlot slot : slots) {

            if (!slot.isOccupied()
                && slot.getVehicleType()
                    .equalsIgnoreCase(vehicleType)) {

                return slot;
            }
        }

        return null;
    }

    // ============================================
    // PARK VEHICLE
    // ============================================

    public void parkVehicle(
        Vehicle vehicle
    ) {

        // Check vehicle number

        if (vehicle.getVehicleNumber() == null ||
            vehicle.getVehicleNumber()
                .trim()
                .isEmpty()) {

            System.out.println(
                "Vehicle number cannot be empty."
            );

            return;
        }

        // Check vehicle type

        String type =
            vehicle.getVehicleType();

        if (type == null ||
            !(type.equalsIgnoreCase("Car") ||
              type.equalsIgnoreCase("Bike") ||
              type.equalsIgnoreCase("EV"))) {

            System.out.println(
                "Invalid vehicle type."
            );

            System.out.println(
                "Please enter Car, Bike, or EV."
            );

            return;
        }

        // Check duplicate active vehicle

        for (ParkingRecord record : records) {

            if (record.getVehicle()
                    .getVehicleNumber()
                    .equalsIgnoreCase(
                        vehicle.getVehicleNumber()
                    )
                && record.getExitTime() == null) {

                System.out.println(
                    "Vehicle is already parked."
                );

                return;
            }
        }

        // Find available slot

        ParkingSlot slot =
            findAvailableSlot(type);

        if (slot == null) {

            System.out.println(
                "No available " +
                type +
                " parking slots."
            );

            return;
        }

        try {

            // ====================================
            // SAVE VEHICLE TO MYSQL
            // ====================================

            String vehicleSql =
                "INSERT INTO vehicles " +
                "(vehicle_number, owner_name, vehicle_type) " +
                "VALUES (?, ?, ?)";

            try (
                Connection con =
                    DBConnection.getConnection();

                PreparedStatement ps =
                    con.prepareStatement(vehicleSql)
            ) {

                ps.setString(
                    1,
                    vehicle.getVehicleNumber()
                );

                ps.setString(
                    2,
                    vehicle.getOwnerName()
                );

                ps.setString(
                    3,
                    vehicle.getVehicleType()
                );

                ps.executeUpdate();
            }

            // Park vehicle in Java

            slot.parkVehicle(vehicle);

            // Create parking record

            ParkingRecord record =
                new ParkingRecord(
                    vehicle,
                    slot
                );

            records.add(record);

            // ====================================
            // SAVE PARKING RECORD TO MYSQL
            // ====================================

            String recordSql =
                "INSERT INTO parking_records " +
                "(vehicle_number, slot_number, " +
                "entry_time, parking_fee) " +
                "VALUES (?, ?, ?, ?)";

            try (
                Connection con =
                    DBConnection.getConnection();

                PreparedStatement ps =
                    con.prepareStatement(recordSql)
            ) {

                ps.setString(
                    1,
                    vehicle.getVehicleNumber()
                );

                ps.setString(
                    2,
                    slot.getSlotId()
                );

                ps.setObject(
                    3,
                    record.getEntryTime()
                );

                ps.setDouble(
                    4,
                    0
                );

                ps.executeUpdate();
            }

            // Update slot status

            updateSlotStatus(
                slot.getSlotId(),
                true
            );

            System.out.println(
                "\nVehicle parked successfully!"
            );

            System.out.println(
                "Vehicle Number : " +
                vehicle.getVehicleNumber()
            );

            System.out.println(
                "Owner Name     : " +
                vehicle.getOwnerName()
            );

            System.out.println(
                "Slot Assigned  : " +
                slot.getSlotId()
            );

        } catch (Exception e) {

            System.out.println(
                "\nError parking vehicle."
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // UPDATE SLOT STATUS IN MYSQL
    // ============================================

    private void updateSlotStatus(
        String slotNumber,
        boolean occupied
    ) {

        String sql =
            "UPDATE parking_slots " +
            "SET is_occupied = ? " +
            "WHERE slot_number = ?";

        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql)
        ) {

            ps.setBoolean(
                1,
                occupied
            );

            ps.setString(
                2,
                slotNumber
            );

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println(
                "Error updating slot status."
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // VEHICLE EXIT
    // ============================================

    public void vehicleExit(
        String vehicleNumber
    ) {

        for (ParkingRecord record : records) {

            Vehicle vehicle =
                record.getVehicle();

            if (vehicle.getVehicleNumber()
                    .equalsIgnoreCase(vehicleNumber)
                && record.getExitTime() == null) {

                LocalDateTime exitTime =
                    LocalDateTime.now();

                record.setExitTime(
                    exitTime
                );

                Duration duration =
                    Duration.between(
                        record.getEntryTime(),
                        exitTime
                    );

                long hours =
                    duration.toHours();

                double fee =
                    feeCalculator.calculateFee(
                        vehicle.getVehicleType(),
                        hours
                    );

                record.setParkingFee(
                    fee
                );

                // Make slot available

                record.getSlot()
                      .removeVehicle();

                // Update database

                updateParkingRecord(
                    vehicleNumber,
                    exitTime,
                    fee
                );

                updateSlotStatus(
                    record.getSlot().getSlotId(),
                    false
                );

                System.out.println(
                    "\n===== VEHICLE EXIT ====="
                );

                System.out.println(
                    "Vehicle Number : " +
                    vehicleNumber
                );

                System.out.println(
                    "Slot           : " +
                    record.getSlot().getSlotId()
                );

                System.out.println(
                    "Entry Time     : " +
                    record.getEntryTime()
                );

                System.out.println(
                    "Exit Time      : " +
                    record.getExitTime()
                );

                System.out.println(
                    "Parking Hours  : " +
                    hours
                );

                System.out.println(
                    "Parking Fee    : Rs." +
                    fee
                );

                System.out.println(
                    "Slot is now available."
                );

                return;
            }
        }

        System.out.println(
            "Vehicle not found or already exited."
        );
    }

    // ============================================
    // UPDATE PARKING RECORD IN MYSQL
    // ============================================

    private void updateParkingRecord(
        String vehicleNumber,
        LocalDateTime exitTime,
        double fee
    ) {

        String sql =
            "UPDATE parking_records " +
            "SET exit_time = ?, parking_fee = ? " +
            "WHERE vehicle_number = ? " +
            "AND exit_time IS NULL";

        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql)
        ) {

            ps.setObject(
                1,
                exitTime
            );

            ps.setDouble(
                2,
                fee
            );

            ps.setString(
                3,
                vehicleNumber
            );

            ps.executeUpdate();

        } catch (Exception e) {

            System.out.println(
                "Error updating parking record."
            );

            e.printStackTrace();
        }
    }

    // ============================================
    // DISPLAY PARKING RECORDS
    // ============================================
public void displayRecords() {

    System.out.println(
        "\n===== PARKING RECORDS ====="
    );

    if (records.isEmpty()) {

        System.out.println(
            "No parking records found."
        );

        return;
    }

    for (ParkingRecord record : records) {

        record.displayRecord();
    }
}
// ============================================
// PARKING DASHBOARD
// ============================================

public void displayDashboard() {

    int totalSlots = slots.size();

    int occupiedSlots = 0;

    int carsParked = 0;
    int bikesParked = 0;
    int evsParked = 0;

    int totalCars = 0;
    int totalBikes = 0;
    int totalEVs = 0;

    double carRevenue = 0;
    double bikeRevenue = 0;
    double evRevenue = 0;

    // Count occupied slots
    for (ParkingSlot slot : slots) {

        if (slot.isOccupied()) {
            occupiedSlots++;
        }
    }

    // Analyze parking records
    for (ParkingRecord record : records) {

        String type =
            record.getVehicle().getVehicleType();

        // Currently parked vehicles
        if (record.getExitTime() == null) {

            if (type.equalsIgnoreCase("Car")) {
                carsParked++;

            } else if (type.equalsIgnoreCase("Bike")) {
                bikesParked++;

            } else if (type.equalsIgnoreCase("EV")) {
                evsParked++;
            }
        }

        // Total vehicle statistics and revenue
        if (type.equalsIgnoreCase("Car")) {

            totalCars++;

            if (record.getExitTime() != null) {
                carRevenue += record.getParkingFee();
            }

        } else if (type.equalsIgnoreCase("Bike")) {

            totalBikes++;

            if (record.getExitTime() != null) {
                bikeRevenue += record.getParkingFee();
            }

        } else if (type.equalsIgnoreCase("EV")) {

            totalEVs++;

            if (record.getExitTime() != null) {
                evRevenue += record.getParkingFee();
            }
        }
    }

    double totalRevenue =
        carRevenue + bikeRevenue + evRevenue;

    int availableSlots =
        totalSlots - occupiedSlots;

    System.out.println(
        "\n================================"
    );

    System.out.println(
        "       PARKING DASHBOARD"
    );

    System.out.println(
        "================================"
    );

    System.out.println(
        "Total Slots     : " +
        totalSlots
    );

    System.out.println(
        "Occupied Slots  : " +
        occupiedSlots
    );

    System.out.println(
        "Available Slots : " +
        availableSlots
    );

    System.out.println();

    System.out.println(
        "Currently Parked"
    );

    System.out.println(
        "----------------------------"
    );

    System.out.println(
        "Cars Parked     : " +
        carsParked
    );

    System.out.println(
        "Bikes Parked    : " +
        bikesParked
    );

    System.out.println(
        "EVs Parked      : " +
        evsParked
    );

    System.out.println();

    System.out.println(
        "Vehicle Statistics"
    );

    System.out.println(
        "----------------------------"
    );

    System.out.println(
        "Total Cars      : " +
        totalCars
    );

    System.out.println(
        "Total Bikes     : " +
        totalBikes
    );

    System.out.println(
        "Total EVs       : " +
        totalEVs
    );

    System.out.println();

    System.out.println(
        "Revenue by Vehicle Type"
    );

    System.out.println(
        "----------------------------"
    );

    System.out.println(
        "Car Revenue     : Rs." +
        carRevenue
    );

    System.out.println(
        "Bike Revenue    : Rs." +
        bikeRevenue
    );

    System.out.println(
        "EV Revenue      : Rs." +
        evRevenue
    );

    System.out.println(
        "Total Revenue   : Rs." +
        totalRevenue
    );

    System.out.println(
        "================================"
    );
}
        // ============================================
    // SEARCH PARKING RECORD
    // ============================================

    public void searchParkingRecord(String vehicleNumber) {

        for (ParkingRecord record : records) {

            if (record.getVehicle()
                    .getVehicleNumber()
                    .equalsIgnoreCase(vehicleNumber)) {

                System.out.println();
                System.out.println("===== PARKING RECORD FOUND =====");

                System.out.println(
                    "Vehicle Number : " +
                    record.getVehicle().getVehicleNumber()
                );

                System.out.println(
                    "Owner Name     : " +
                    record.getVehicle().getOwnerName()
                );

                System.out.println(
                    "Vehicle Type   : " +
                    record.getVehicle().getVehicleType()
                );

                System.out.println(
                    "Slot           : " +
                    record.getSlot().getSlotId()
                );

                System.out.println(
                    "Entry Time     : " +
                    record.getEntryTime()
                );

                if (record.getExitTime() == null) {
                    System.out.println(
                        "Exit Time      : Still Parked"
                    );
                } else {
                    System.out.println(
                        "Exit Time      : " +
                        record.getExitTime()
                    );
                }

                System.out.println(
                    "Parking Fee    : Rs." +
                    record.getParkingFee()
                );

                System.out.println(
                    "================================"
                );

                return;
            }
        }

        System.out.println(
            "No parking record found for vehicle: " +
            vehicleNumber
        );
    }
    public void searchParkingHistoryByDate(String date) {

        String sql =
            "SELECT vehicle_number, slot_number, " +
            "entry_time, exit_time, parking_fee " +
            "FROM parking_records " +
            "WHERE DATE(entry_time) = ?";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, date);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                System.out.println();
                System.out.println(
                    "===== PARKING HISTORY FOR " + date + " ====="
                );

                while (rs.next()) {

                    found = true;

                    System.out.println(
                        "Vehicle Number : " +
                        rs.getString("vehicle_number")
                    );

                    System.out.println(
                        "Slot           : " +
                        rs.getString("slot_number")
                    );

                    System.out.println(
                        "Entry Time     : " +
                        rs.getTimestamp("entry_time")
                    );

                    System.out.println(
                        "Exit Time      : " +
                        rs.getTimestamp("exit_time")
                    );

                    System.out.println(
                        "Parking Fee    : Rs." +
                        rs.getDouble("parking_fee")
                    );

                    System.out.println(
                        "--------------------------------"
                    );
                }

                if (!found) {
                    System.out.println(
                        "No parking history found for this date."
                    );
                }

                System.out.println(
                    "================================"
                );
            }

        } catch (Exception e) {

            System.out.println(
                "Error searching parking history."
            );

            e.printStackTrace();
        }
    }
}
               

        