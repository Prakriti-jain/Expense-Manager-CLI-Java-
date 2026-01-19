import java.time.LocalDateTime;

sealed class Expense permits FoodExpense, TravelExpense, BillExpense, MiscExpense {
    String id;
    String category;
    double amount;
    LocalDateTime dateTime;

    public Expense(String id, String category, double amount, LocalDateTime dateTime) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return
                "Expense ID = " + id + '\t' +
                "Category = " + category + '\t' +
                "Amount = " + amount + '\t'+
                "DateTime = " + dateTime +'\n' ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
