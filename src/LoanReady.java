import java.util.Scanner;
import java.util.List;
import java.lang.Math;

public class LoanReady {
    private String name;
    private double principal;
    private double interestRate;
    private double monthlyPayment;


    public LoanReady(String name, double principal, double interestRate) {
        this.name = name;
        this.principal = principal;
        this.interestRate = interestRate;
        this.monthlyPayment = 0;
    }

    public String getName() {
        return name;
    }

    public double getPrincipal() {
        return principal;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }


    public void setMonthlyPayment(double  monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public double calculateTotalWithInterest() {
        return principal + (principal * (interestRate/100));
    }

    public static void createLoan(List<LoanReady> loans, Scanner sc) {

        double amount = 0;
        double rate = 0;
        double monthlyPayment = 0;

        System.out.print("\nEnter the name of the loan: ");
        String name = sc.nextLine().trim();

        while(true) {
            System.out.print("Enter loan amount: $");
            if (sc.hasNextDouble()) {
                amount = sc.nextDouble();
                if (amount > 0) {
                    break;
                } else {
                    System.out.println("\nAmount must be greater than 0");
                }
            } else {
                System.out.println("Please enter a valid number.");
                sc.next();
            }
        }

        while (true) {
            System.out.print("Enter interest rate (%): ");
            if (sc.hasNextDouble()) {
                rate = sc.nextDouble();
                if (rate >= 0) {
                    break;
                } else {
                    System.out.println("\nInterest rate must be greater than 0.");
                }
            } else {
                System.out.println("Please enter a valid rate.");
                sc.next();
            }
        }

        while(true) {
            System.out.print("Enter monthly payment for this loan: $");
            if (sc.hasNextDouble()) {
                monthlyPayment = sc.nextDouble();
                if (monthlyPayment > 0) {
                    break;
                } else {
                    System.out.println("\nMonthly payment must be greater than 0.");
                }
            } else {
                System.out.println("Please enter a valid monthly payment.");
                sc.next();
            }
        }

        sc.nextLine();

        LoanReady newLoan = new LoanReady(name, amount, rate);
        newLoan.setMonthlyPayment(monthlyPayment);
        loans.add(newLoan);

        InfoStore.saveLoans(loans);
        System.out.println("Loan added successfully!");
    }

    public static void destroyLoan(List<LoanReady> loans, Scanner sc) {
        if (loans.isEmpty()) {
            System.out.println("\nNo loans to remove.");
            return;
        }

        System.out.print("\nEnter the name of the loan you want to remove: ");
        String toRemove = sc.nextLine().trim();

        LoanReady removeLoan = null;
        for (LoanReady loan : loans) {
            if (loan.getName().equals(toRemove)) {
                removeLoan = loan;
                break;
            }
        }

        if (removeLoan != null) {
            loans.remove(removeLoan);
            InfoStore.saveLoans(loans);
            System.out.printf("%s removed successfully.%n", removeLoan.getName());
        } else {
            System.out.printf("%s not found.%n", toRemove);
        }
    }

    public void displayLoan() {
        System.out.printf("Loan: %s%n"
                + "Amount: $%,.2f%n"
                + "Interest Rate: %.1f%%%n"
                + "total Payment: $%,.2f%n"
                + "Monthly Payment Plan: $%,.2f%n%n",
                name, principal, interestRate, calculateTotalWithInterest(), monthlyPayment);
    }

    public static void totalDebt(List<LoanReady> loans) {
        double totalDebt = 0;
        for (LoanReady loan : loans) {
            totalDebt += loan.calculateTotalWithInterest();
        }
        System.out.printf("%nTotal debt (with interest): $%,.2f%n", totalDebt);
    }

    // Parse a line from the file to create a LoanReady object
    public static LoanReady fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length != 4) return null;

        try {
            LoanReady loan = new LoanReady(parts[0], Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
            loan.setMonthlyPayment(Double.parseDouble(parts[3]));
            return loan;
        } catch (NumberFormatException e) {
            return null;
        }
    }


    public static void payoffTime(List<LoanReady> loans, User user) {
        if (loans.isEmpty()) {
            System.out.println("No loans to pay off.");
            return;
        }

        System.out.println("\nPayoff Estimates");

        double totalMonthlyPayments = 0;
        double totalDebt = 0;
        double longestMonths = 0;

        for (LoanReady loan : loans) {
            double total = loan.calculateTotalWithInterest();
            double monthlyPayment = loan.getMonthlyPayment();

            if (monthlyPayment <= 0) {
                System.out.printf("%s: Monthly payment not set. Skipping...%n", loan.getName());
                continue;
            }

            double months = total / monthlyPayment;
            int totalMonths = (int) Math.ceil(months);
            int years = totalMonths / 12;
            int remainingMonths = totalMonths % 12;

            totalMonthlyPayments += monthlyPayment;
            totalDebt += total;
            longestMonths = Math.max(longestMonths, totalMonths);

            if (years == 0) {
                System.out.printf("%s ($%,.2f): %d months at $%,.2f per month%n",
                        loan.getName(), total, remainingMonths, monthlyPayment);
            } else {
                System.out.printf("%s ($%,.2f): %d years %d months at $%,.2f per month%n",
                        loan.getName(), total, years, remainingMonths, monthlyPayment);
            }
        }

        // Show traditional longest-loan summary
        int totalYears = (int) (longestMonths / 12);
        int totalRemainingMonths = (int) (longestMonths % 12);

        System.out.printf(
                "%nEstimated payoff for all loans (based on longest loan): %d years %d months at a combined monthly payment of $%,.2f%n",
                totalYears, totalRemainingMonths, totalMonthlyPayments);

        // Now show pooled payoff (total debt divided by combined monthly payments)
        double pooledMonths = totalDebt / totalMonthlyPayments;
        int pooledTotalMonths = (int) Math.ceil(pooledMonths);
        int pooledYears = pooledTotalMonths / 12;
        int pooledRemainingMonths = pooledTotalMonths % 12;

        System.out.printf(
                "Estimated payoff for all loans (if pooled): %d years %d months at a combined monthly payment of $%,.2f%n",
                pooledYears, pooledRemainingMonths, totalMonthlyPayments);

        // Show total debt summary
        System.out.printf("Total amount owed (with interest): $%,.2f%n", totalDebt);
    }


    public String toFileString() {
        return name + "," + principal + "," + interestRate + "," + monthlyPayment;
    }

    @Override
    public String toString() {
        return String.format("%s - $%.2f at %.2f%% interest", name, principal, interestRate);
    }
}
