import java.util.List;
import java.util.Scanner;

public class LoanReadyMain {
    public static void main(String[] args) {
        System.out.println("Are you LoanReady?");
        Scanner sc = new Scanner(System.in);
        boolean menu = true;
        List<LoanReady> loans = InfoStore.loadLoans();
        User user = User.loadUserInfo();

        if (user == null) {
            System.out.print("Enter your monthly income: $");
            double income = Double.parseDouble(sc.nextLine());
            user = new User(income);
            user.saveUserInfo();
        } else {
            System.out.printf("Welcome back. Your monthly income is: $%,.2f", user.getMonthlyIncome());
        }

        while(menu) {
            System.out.print("""
                    
                    Menu
                    1. Add a loan
                    2. Remove a loan
                    3. View all loans
                    4. View total debt
                    5. Update income
                    6. View payoff time
                    7. Exit
                    Choose an option:\s""");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    LoanReady.createLoan(loans, sc);
                    InfoStore.saveLoans(loans);
                    break;
                case 2:
                    LoanReady.destroyLoan(loans, sc);
                    InfoStore.saveLoans(loans);
                    break;
                case 3:
                    if (loans.isEmpty()) {
                        System.out.println("\nNo loans to show");
                    }
                    else {
                        System.out.println("\nYour loans\n");
                        for (LoanReady loan : loans) {
                            loan.displayLoan();
                        }
                    }
                    break;
                case 4:
                    LoanReady.totalDebt(loans);
                    break;
                case 5:
                    user.updateIncome(sc);
                    break;
                case 6:
                    LoanReady.payoffTime(loans, user);
                    break;
                case 7:
                    InfoStore.saveLoans(loans);
                    menu = false;
                    break;
                default:
                    return;
            }
        }
    }
}