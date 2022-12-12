import java.util.Scanner;

public class AtmSimulator {
    private static int balance = 17000;
    public static int pin = 0001;
    public static int providedpin = 0;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        double cardNumber = 0;


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
        }while(!checkCardNumber(cardNumber));

        boolean cardIsValid = checkCardNumber(cardNumber);

        //pin number verification
        if(cardIsValid) {
            int count = 0;
            do {
                System.out.println("Enter your pin:");
                try {
                    providedpin = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException ex) {
                    System.out.println("Please Enter valid pin code.");
                }
                if (!checkPinNumber(providedpin)) {
                    int i = 0;
                    while (i < 3) {
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
            } while (!checkPinNumber(providedpin));


        }

        boolean pinIsValid = checkPinNumber(providedpin);


        // when verification process is completed
        // showing options to the user
        if(pinIsValid && cardIsValid) {
            options();
        }
    }
    private static void options(){
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
                choice = sc.nextInt();
            } catch (NumberFormatException ex) {
                System.out.println("Please Enter a valid choice!!!");
            }

            switch (choice){
                case 1:
                    balance();
                    break;
                case 2:
                    miniStatement();
                    break;
                case 3:
                    withdrawal();
                    break;
                case 4:
                    changePIN();
                    break;
                case 5:
                    exit();
                    break;
                default:
                    System.out.println("Please enter a number between 1 to 5");
                    options();
            }
        } while (true);

    }

    private static void exit(){
        System.out.println("Thank you for using our Banking service.");
        System.exit(1);
    }

    private static void changePIN() {
        int oldPIN;
        int newPIN;
        int renewPIN;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your old PIN:");
        oldPIN = sc.nextInt();
        if (oldPIN == pin){
            System.out.println("Enter your new PIN");
            newPIN = sc.nextInt();
            System.out.println("Re-Enter your new PIN");
            renewPIN = sc.nextInt();

            if (newPIN == renewPIN){
                pin = newPIN;
                System.out.println("Your PIN has been successfully changed. Thank YOU!!");
            }else {
                System.out.println(" Your pin doesn't match." );
            }
        }else {
            System.out.println("Incorrect PIN");
        }

    }

    static void withdrawal() {
        Scanner sc = new Scanner(System.in);
        int amount;
        System.out.println("Enter the amount you want to withdraw:");
        amount = sc.nextInt();
        if(amount<balance){
            balance = balance - amount;
            System.out.println("Successfully withdrawn!!!");
        }else {
            System.out.println("Insufficient balance in your account!!!");
        }
    }

    static void miniStatement() {
        System.out.println("Here is your mini Statement:");
        System.out.println("---------------------------");
        System.out.println("Name \t \t \t Bank Branch \t \t Available Balance");
        System.out.println("Jasbin Balami \t Battisputali\t \t "+balance);
    }

    static void balance() {

        System.out.println("Your current balance is Rs." + balance);

    }

    private static boolean checkPinNumber(int providedpin) {
        
        if (providedpin == pin){
            System.out.println("PIN verified.");
            return true;
        }
        else {
            System.out.println("Incorrect PIN!!!");
            return false;
        }
    }

    private static boolean checkCardNumber(double cardNumber) {
        if (cardNumber == 980555){
            System.out.println("Card Verified.");
            return true;
        }else {
            System.out.println("Incorrect card number!!!");
            return false;
        }
    }
}