import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class AdminPanel {
    public static void functions() throws SQLException {
        Connection cn = DatabaseConnection.getConnection();
        Statement stat = cn.createStatement();
        Scanner sc = new Scanner(System.in);

        System.out.println("Options:");
        System.out.println("Enter 1 to add user");
        System.out.println("Enter 2 to delete user");

        int choice = Integer.parseInt(sc.nextLine());

        switch (choice){
            case 1:
                addUser(stat,sc);
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

        System.out.println("Data successfully updated.");
        System.out.println("With card number: "+cardNumber);
        System.out.println("With pin number: "+pinNumber);
        System.out.println("Available balance:"+balance);

    }
    private static double random(Statement stat) throws SQLException{
        Random random = new Random();

        double randomCardNumber = random.nextDouble(1000000);

        String sql = "select * form user_info where card_number= "+randomCardNumber;
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
