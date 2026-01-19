import java.time.LocalDateTime;

non-sealed class FoodExpense extends Expense {

    public FoodExpense(String id, String category, double amount, LocalDateTime dateTime) {
        super(id, category, amount, dateTime);
    }
}
