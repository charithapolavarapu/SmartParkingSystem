import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean loggedIn = AdminLogin.login();

        if (!loggedIn) {
            System.out.println("Access denied.");
            System.out.println("Exiting Smart Parking System.");
            return;
        }

        ParkingManager manager = new ParkingManager();

        int choice;

        do {

            System.out.println("\n================================");
            System.out.println("       SMART PARKING SYSTEM");
            System.out.println("================================");
            System.out.println("1. View Parking Slots");
            System.out.println("2. Park Vehicle");
            System.out.println("3. Vehicle Exit");
            System.out.println("4. View Parking Records");
            System.out.println("5. Parking Dashboard");
            System.out.println("6. Exit");
            System.out.println("7. Search Parking Record");
            System.out.println("8. Parking History by Date");

            System.out.print("\nEnter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    manager.displaySlots();
                    break;

                case 2:
                    System.out.print("Enter Vehicle Number: ");
                    String vehicleNumber = sc.nextLine();

                    System.out.print("Enter Owner Name: ");
                    String ownerName = sc.nextLine();

                    System.out.print("Enter Vehicle Type (Car/Bike/EV): ");
                    String vehicleType = sc.nextLine();

                    Vehicle vehicle = new Vehicle(
                        vehicleNumber,
                        ownerName,
                        vehicleType
                    );

                    manager.parkVehicle(vehicle);
                    break;

                case 3:
                    System.out.print("Enter Vehicle Number: ");
                    String exitVehicle = sc.nextLine();

                    manager.vehicleExit(exitVehicle);
                    break;

                case 4:
                    manager.displayRecords();
                    break;

                case 5:
                    manager.displayDashboard();
                    break;

                case 6:
                    System.out.println(
                        "\nThank you for using Smart Parking System!"
                    );
                    break;

                case 7:
                    System.out.print("Enter Vehicle Number: ");
                    String searchNumber = sc.nextLine();

                    manager.searchParkingRecord(searchNumber);
                    break;

                case 8:
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = sc.nextLine();

                    manager.searchParkingHistoryByDate(date);
                    break;

                default:
                    System.out.println(
                        "\nInvalid choice. Please try again."
                    );
            }

        } while (choice != 6);

        sc.close();
    }
}