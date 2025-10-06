import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class InfoStore {
    private static final String FILE_NAME = "loans.txt";

    //save loan info in a file
    public static void saveLoans(List<LoanReady> loans) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            for (LoanReady loan : loans) {
                writer.write(loan.toFileString() + "\n");
            }
            System.out.println("Successfully saved " + loans.size() + " loans.");
        } catch (IOException e) {
            System.out.println("Error writing loans.");
        }
    }

    public static List<LoanReady> loadLoans() {
        List<LoanReady> loans = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return loans;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LoanReady loan = LoanReady.fromFileString(line);
                if (loan != null) {
                    loans.add(loan);
                }
            }
            //System.out.println("Loans loaded from " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error reading loans.");
        }
        return loans;
    }
}

