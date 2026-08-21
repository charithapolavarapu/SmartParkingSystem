import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AdminLogin {

    public static boolean login() {

        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("================================");
        System.out.println("         ADMIN LOGIN");
        System.out.println("================================");

        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println();
                System.out.println("Login successful!");
                System.out.println("Welcome, " + username + "!");
                return true;
            } else {
                System.out.println();
                System.out.println("Invalid username or password.");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Login failed.");
            e.printStackTrace();
            return false;
        }
    }
}