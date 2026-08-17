import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/smart_parking";

    private static final String USER = "root";
private static final String PASSWORD =
        System.getenv("SMART_PARKING_DB_PASSWORD");
    public static Connection getConnection() {

        try {

            Connection connection =
                    DriverManager.getConnection(
                            URL,
                            USER,
                            PASSWORD
                    );

            System.out.println("MySQL Connected Successfully!");

            return connection;

        } catch (Exception e) {

            System.out.println("Database Connection Failed!");

            e.printStackTrace();

            return null;
        }
    }
}