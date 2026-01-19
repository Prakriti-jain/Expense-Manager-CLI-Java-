import java.time.LocalDateTime;

non-sealed class MiscExpense extends Expense{
    public MiscExpense(String id, String category, double amount, LocalDateTime dateTime) {
        super(id, category, amount, dateTime);
    }
}
