import java.sql.*;
import java.util.Scanner;

public class AtmSimulator {
    public static int pin = 0;
    public static int providedpin = 0;

    public static double cardNumber = 0;


    public static void main(String[] args) throws SQLException {
        Connection cn = DatabaseConnection.getConnection();
        Statement stat = cn.createStatement();

        Scanner sc = new Scanner(System.in);




        System.out.println("Welcome to our ATM service.");
        System.out.println("----------------------------");
        //Verification process starts
        //card verification
        do {
            System.out.println("Please Enter your card number:");
            try {
                cardNumber = Double.parseDouble(sc.nextLine()) ;
            }catch (NumberFormatException ex){
                System.out.println("Please Enter a valid card number");
            }
        }while(!checkCardNumber(stat, cardNumber));

        boolean cardIsValid = checkCardNumber(stat ,cardNumber);

        //pin number verification
        if(cardIsValid) {
            int count = 0;
            System.out.println("Card Verified.");
            do {

                System.out.println("Enter your pin:");
                try {
                    providedpin = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Please Enter valid pin code.");
                }
                if (!checkPinNumber(stat,providedpin)) {
                    System.out.println("Incorrect pin number!!!");
                    int i = 0;
                    while (true) {
                        System.out.println("You have " +(3-count)+ " attempts left");
                        count++;
                        i++;
                        break;
                    }
                    if (count == 4){
                        System.out.println("Your account has been blocked. Please visit your nearest Branch");
                        System.exit(1);
                    }

                }

            } while (!checkPinNumber(stat,providedpin));


        }

        boolean pinIsValid = checkPinNumber(stat,providedpin);


        // when verification process is completed
        // showing options to the user
        if(pinIsValid && cardIsValid) {
            System.out.println("PIN verified.");
            options(cn,stat);
        }
    }
    private static void options(Connection cn,Statement stat) throws SQLException {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        do {
            System.out.println("Options:");
            System.out.println("Enter 1 for Balance Enquiry");
            System.out.println("Enter 2 for Mini Statement");
            System.out.println("Enter 3 for Withdrawal");
            System.out.println("Enter 4 for Changing PIN");
            System.out.println("Enter 5 to END!");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Please Enter a valid choice!!!");
            }

            switch (choice){
                case 1:
                    balance(stat,cardNumber);
                    break;
                case 2:
                    miniStatement(stat,cardNumber);
                    break;
                case 3:
                    withdrawal(stat ,cn, cardNumber);
                    break;
                case 4:
                    changePIN(stat,cn,cardNumber);
                    break;
                case 5:
                    exit();
                    break;
                default:
                    System.out.println("Please enter a number between 1 to 5");
                    options(cn,stat);
            }
        } while (true);

    }

    private static void exit(){
        System.out.println("Thank you for using our Banking service.");
        System.exit(1);
    }

    private static void changePIN(Statement stat,Connection cn, double cardNumber) throws SQLException {
        int oldPIN;
        int newPIN;
        int renewPIN;
        Scanner sc = new Scanner(System.in);
        String sql = "select * from user_info where pin =" +providedpin;
        ResultSet rs = stat.executeQuery(sql);
        rs.next();
        pin = rs.getInt(3);

        System.out.println("Enter your old PIN:");
        oldPIN = Integer.parseInt(sc.nextLine());
        if (oldPIN == pin){
            System.out.println("Enter your new PIN");
            newPIN = Integer.parseInt(sc.nextLine());
            System.out.println("Re-Enter your new PIN");
            renewPIN = Integer.parseInt(sc.nextLine());

            if (newPIN == renewPIN){
                String updatePin = "update user_info set pin = ? where card_number = ?";
                PreparedStatement statement = cn.prepareStatement(updatePin);

                statement.setInt(1, newPIN);
                statement.setDouble(2, cardNumber);

                int rowCount = statement.executeUpdate();

                System.out.println("Your PIN has been successfully changed. Thank YOU!!");
            }else {
                System.out.println(" Your pin doesn't match." );
            }
        }else {
            System.out.println("Incorrect PIN");
        }

    }

    static void withdrawal(Statement stat,Connection cn, double cardNumber) throws SQLException{
        Scanner sc = new Scanner(System.in);
        String sql = "select balance from user_info where card_number = " +cardNumber;
        ResultSet rs = stat.executeQuery(sql);
        rs.next();

        int amount;
        int actual_amount= rs.getInt(1);

        System.out.println("Enter the amount you want to withdraw:");
        amount = Integer.parseInt(sc.nextLine());

        if(amount<actual_amount){
            String updatesql ="UPDATE user_info SET balance = ? WHERE card_number = ?";;
            PreparedStatement statement = cn.prepareStatement(updatesql);

            int balance = actual_amount - amount;
            statement.setInt(1 , balance);
            statement.setDouble(2, cardNumber);

            int count = statement.executeUpdate();

            System.out.println("Amount withdrawn Rs " +amount );

        }else {
            System.out.println("Insufficient balance in your account!!!");
        }
    }

    static void miniStatement(Statement stat,double cardNumber) throws SQLException {

        String sql = "select * from user_info where card_number =" +cardNumber;
        ResultSet rs = stat.executeQuery(sql);
        rs.next();

        System.out.println("Here is your mini Statement:");
        System.out.println("---------------------------");
        System.out.println("Name \t \t \t Bank Branch \t \t Available Balance");
        System.out.println(rs.getString(5)+"\t\t" +rs.getString(4)+"\t\t\t" +rs.getInt(6));
    }

    static void balance(Statement stat, double cardNumber) throws SQLException {
        String sql = "select balance from user_info where card_number = " +cardNumber;
        ResultSet rs = stat.executeQuery(sql);
        rs.next();
        System.out.println("Your current balance is Rs." + rs.getInt(1));

    }

    private static boolean checkPinNumber(Statement stat,int providedpin) throws SQLException {
        String sql = "select * from user_info where pin=" +providedpin;
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()){
            return true;
        }else {
            return false;
        }
    }

    private static boolean checkCardNumber(Statement stat,double cardNumber) throws SQLException {
        if (cardNumber == 1111111){
            AdminPanel.functions();
        }
        String sql = "select * from user_info where card_number=" +cardNumber;
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()){
            return true;
        }else {
            System.out.println("Incorrect card number!!!");
            return false;
        }
    }
}