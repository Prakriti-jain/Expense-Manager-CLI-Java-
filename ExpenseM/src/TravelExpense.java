import java.time.LocalDateTime;

non-sealed class TravelExpense extends Expense{
    public TravelExpense(String id, String category, double amount, LocalDateTime dateTime) {
        super(id, category, amount, dateTime);
    }
}
