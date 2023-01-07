import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class AdminPanel {
    public static void functions() throws SQLException {
        Connection cn = DatabaseConnection.getConnection();


        option(cn);



    }
    private  static void option(Connection cn) throws SQLException{

        Statement stat = cn.createStatement();
        Scanner sc = new Scanner(System.in);
        System.out.println("Options:");
        System.out.println("Enter 1 to add user");
        System.out.println("Enter 2 to delete user");

        int choice = Integer.parseInt(sc.nextLine());

        switch (choice){
            case 1:
                addUser(stat,sc);
                break;
            case 2:
                deleteUser(stat,sc);
                break;
            case 3:
                System.exit(1);
                break;
            default:
                System.out.println("Please select a number between 1 to 3:");
                option(cn);
        }
    }

    private static void deleteUser(Statement stat, Scanner sc) {
        System.out.println("Enter the user_id of the user you want to delete:");
        int id  = Integer.parseInt(sc.nextLine());

        String deleteQuery = "delete from user_info where id = "+id;
        try {
            int rowCount = stat.executeUpdate(deleteQuery);
            System.out.println(rowCount+ " row(s) deleted");
            System.exit(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void addUser(Statement stat, Scanner sc) throws SQLException {


        System.out.println("Enter user name:");
        String userName = sc.nextLine();

        double cardNumber = random(stat);

        System.out.println("Enter pin number:");
        int pinNumber = Integer.parseInt(sc.nextLine());
        System.out.println("Enter bank branch");
        String bankBranch = sc.nextLine();
        System.out.println("Enter your initial balance:");
        int balance = Integer.parseInt(sc.nextLine());

        String addQuery = "INSERT INTO `user_info` (`id`, `card_number`, `pin`, `bank_branch`, `name`, `balance`) VALUES (NULL, '"+cardNumber+"', '"+pinNumber+"', '"+bankBranch+"', '"+userName+"', '"+balance+"');";
        int rowsAffected = stat.executeUpdate(addQuery);

        String userIDQuery = "select * from user_info where card_number = "+cardNumber;
        ResultSet rs = stat.executeQuery(userIDQuery);
        rs.next();
        int userID = rs.getInt(1);

        System.out.println("Data successfully updated.");
        System.out.println("With user ID: " +userID);
        System.out.println("With card number: "+cardNumber);
        System.out.println("With pin number: "+pinNumber);
        System.out.println("Available balance:"+balance);
        System.exit(1);

    }
    private static int random(Statement stat) throws SQLException{
        Random random = new Random();

        int randomCardNumber = 1000000 + random.nextInt(9000000);

        String sql = "select * from user_info where card_number= "+randomCardNumber;
        ResultSet rs = stat.executeQuery(sql);
        rs.next();
        


        if(!rs.next()){
            return randomCardNumber;
        }
        else {
            return random(stat);
        }

    }

}
