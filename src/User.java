import java.io.*;
import java.util.Scanner;

public class User {
    private double monthlyIncome;
    private static final String FILE_NAME = "user.txt";

    public User(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public void saveUserInfo() {
        try (PrintWriter write = new PrintWriter(new FileWriter(FILE_NAME))) {
            write.println(monthlyIncome);
            System.out.println("User info saved.");
        } catch (IOException e) {
            System.out.println("Error saving user info.");
        }
    }

    public static User loadUserInfo() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line = read.readLine();
            double income = Double.parseDouble(line);
            return new User(income);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading user info.");
            return null;
        }
    }

    public void updateIncome(Scanner sc) {
        System.out.println("Enter your new monthly Income: $");
        try {
            double newIncome = Double.parseDouble(sc.nextLine());
            if (newIncome <= 0) {
                System.out.println("Invalid monthly Income!");
                return;
            }
            setMonthlyIncome(newIncome);
            saveUserInfo();
            System.out.printf("New monthly income: $%,.2f",  newIncome);
        } catch (NumberFormatException e) {
            System.out.println("Invalid monthly Income!");
        }

    }

}
