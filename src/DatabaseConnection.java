import java.sql.Connection;
import java.sql.DriverManager;
public class DatabaseConnection {
    public static Connection getConnection() {
        Connection cn = null;
        try {
            // loading/register the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Creating connection
            String url = "jdbc:mysql://localhost:3306/atm_data";
            String username = "root";
            String pw = "";
            cn = DriverManager.getConnection(url, username, pw);
            System.out.println("Database Connection Success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cn;
    }

//    public static void main(String[] args) {
//        getConnection();
//    }
}
