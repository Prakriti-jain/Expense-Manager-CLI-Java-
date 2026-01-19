import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ExpenseOperations {
    void addExpense() throws IOException, InvalidInputException;
    void updateExpense() throws IOException, InvalidInputException, ExpenseNotFoundException;
    void viewAll() throws FileNotFoundException;
    void viewByCategory() throws FileNotFoundException, ExpenseNotFoundException;
    void viewByDate() throws FileNotFoundException;
    void export() throws IOException, InvalidInputException;
}
