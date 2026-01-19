import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ExpenseNotFoundException, InvalidInputException, UserAuthenticationException {
        {
            System.out.println("---------------- Expense Manager -----------------");
            while (true) {
                System.out.println(" 1. Login\n 2. Register\n 3. Exit");
                Scanner sc = new Scanner(System.in);
                UserOperations op = new UserOperations();
                String file_username = "users.csv";

                try {
                    int val = sc.nextInt();
                    if (val == 1) {
                        System.out.println("Enter your username - ");
                        String username = sc.next().trim();
                        System.out.println("Enter your password - ");
                        String pass = sc.next().trim();

                        if (op.login(username, pass)) {

                            // opens the main options
                            // find the expense file corresponding to the user

                            String expenseFile = getString(file_username, username, pass);
                            ExpenseManager exp = new ExpenseManager(username, expenseFile);

                            while (true) {
                                System.out.println("1. Add Expense\n 2. Update Expense\n 3. View All Expenses\n 4. View Expense by Category\n 5. View Expense of a certain date\n 6. Export Expense to a new file\n 7. Exit");

                                try {
                                    int ch = sc.nextInt();

                                    if (ch == 1) {
                                        exp.addExpense();

                                    } else if (ch == 2) {
                                        exp.updateExpense();

                                    } else if (ch == 3) {
                                        exp.viewAll();

                                    } else if (ch == 4) {
                                        exp.viewByCategory();

                                    } else if (ch == 5) {
                                        exp.viewByDate();

                                    } else if (ch == 6) {
                                        exp.export();

                                    } else if (ch == 7) {
                                        break;
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid input");
                                } catch (InvalidInputException e) {
                                    throw new InvalidInputException("Invalid Input " + e);
                                } catch (ExpenseNotFoundException e) {
                                    throw new ExpenseNotFoundException("Expense not found! " + e);
                                }


                            }
                        }
                    } else if (val == 2) {
                        System.out.println("Enter your username - ");
                        String username = sc.next().trim();
                        System.out.println("Enter your password - ");
                        String pass = sc.next().trim();

                        //store in the json these credentials
                        op.register(username, pass);

                        //go back to log in

                    } else if (val == 3) {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Enter correct input");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (UserAuthenticationException e) {
                    throw new UserAuthenticationException("Authentication Error " + e);
                }catch (InvalidInputException e) {
                    throw new InvalidInputException("Invalid Input " + e);
                }catch (ExpenseNotFoundException e) {
                    throw new ExpenseNotFoundException("Expense not found! " + e);
                }

            }

        }
    }

    private static String getString(String file_username, String username, String pass) throws FileNotFoundException {
        String expenseFile = "";
        //read the csv to know the expense file name
        try(BufferedReader br = new BufferedReader(new FileReader(file_username))) {
            String line;

            while((line=br.readLine()) != null) {
                if(line.trim().isBlank()) continue;
                String[] parts = line.split(", ");
                String user = parts[0];
                String password = parts[1];
                String filename = parts[2];

                if(user.equals(username) && password.equals(pass)) {
                    System.out.println(filename);
                    expenseFile = filename;
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return expenseFile;
    }
}