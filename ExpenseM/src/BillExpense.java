import java.time.LocalDateTime;

non-sealed class BillExpense extends Expense{
    public BillExpense(String id, String category, double amount, LocalDateTime dateTime) {
        super(id, category, amount, dateTime);
    }
}
