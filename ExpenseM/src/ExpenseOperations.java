import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ExpenseOperations {
    void addExpense() throws IOException;
    void updateExpense() throws IOException;
    void viewAll() throws FileNotFoundException;
    void viewByCategory() throws FileNotFoundException;
    void viewByDate() throws FileNotFoundException;
    void export() throws IOException;
}
