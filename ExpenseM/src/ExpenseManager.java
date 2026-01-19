import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ExpenseManager implements ExpenseOperations{
    String username;
    String userFile;

    public ExpenseManager(String username, String userFile) throws FileNotFoundException {
        this.username = username;
        this.userFile = userFile;

    }

    public String getId(){
        String dateId = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmssSS"));
        return "EXP_"+dateId;
    }

    public List<Expense> readExp() throws FileNotFoundException {
        List<Expense> list = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
//            boolean header = true;

            while((line = br.readLine())!=null) {
//                if (header) {
//                    header = false;
//                    continue;
//                }
                if (line.trim().isBlank()) continue;
                    
                String[] parts = line.trim().split(", ");
                Expense e = getExpense(parts);

                list.add(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private Expense getExpense(String[] parts) {
        String expId = parts[0];
        String category = parts[1];
        String amt = parts[2];
        String date = parts[3];

        Expense e = null;

        switch (category.toUpperCase()){
            case "FOOD" :
                e = new FoodExpense(expId, category, Double.parseDouble(amt), LocalDateTime.parse(date)); 
            case "TRAVEL" :
                e = new TravelExpense(expId, category, Double.parseDouble(amt), LocalDateTime.parse(date));
            case "BILLS" :
                e = new BillExpense(expId, category, Double.parseDouble(amt), LocalDateTime.parse(date));
            case "MISC" :
                e = new MiscExpense(expId, category, Double.parseDouble(amt), LocalDateTime.parse(date));
            default:
                //fallback
                e = new FoodExpense(expId, category, Double.parseDouble(amt), LocalDateTime.parse(date));
        }
        return e;
    }

    @Override
    public void addExpense() throws IOException, InvalidInputException {
        Scanner sc = new Scanner(System.in);

        LocalDateTime date;
        //input amount
        System.out.println("Input Amount - ");
        double amt = sc.nextDouble();
        if(amt <= 0) {
            throw new InvalidInputException("Invalid amount!!");
        }

        //input category
        System.out.println("Input Category (FOOD, BILLS, TRAVEL, MISC) - ");
        String cat = sc.next().toUpperCase().trim();
        if(cat.isBlank()) {
            throw new InvalidInputException("Category cannot be empty!");
        }

        System.out.println("1. Enter Date Manually\n 2. Take Current Date Time");
        int ch1 = sc.nextInt();
        if (ch1 == 1) {
            System.out.println("Enter date (yyyy-mm-dd) - ");
            LocalDate inputDate = LocalDate.parse(sc.next().trim());
            date = inputDate.atStartOfDay();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmmss");

            date = LocalDateTime.parse(
                    LocalDateTime.now().format(formatter),
                    formatter
            );
        }

        //exp id
        String expId = getId();

        Expense e = null;

        if (cat.equals("FOOD")) {
            e = new FoodExpense(expId, cat, amt, date);

        } else if (cat.equals("TRAVEL")) {
            e = new TravelExpense(expId, cat, amt, date);

        } else if (cat.equals("BILLS")) {
            e = new BillExpense(expId, cat, amt, date);

        } else if (cat.equals("MISC")) {
            e = new MiscExpense(expId, cat, amt, date);
        } else {
            System.out.println("Invalid Category!");
        }

        String toCsv = expId + ", " + e.getCategory() + ", " + amt + ", " + date;
        //write it in file
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write(toCsv);
            bw.newLine();
        }

        System.out.println("Added : " + toCsv);
    }

    @Override
    public void updateExpense() throws IOException, InvalidInputException, ExpenseNotFoundException {
        Scanner sc = new Scanner(System.in);
        List<Expense> list = readExp();

        if (list.isEmpty()) System.out.println("no expenses!!");

        System.out.println(list);
        System.out.println("Enter Expense ID - ");
        String expId = sc.next().trim();

        System.out.println("1. Update Amount\n 2. Update Category");
        int ch = sc.nextInt();

        if (ch == 1) {
            System.out.println("Enter updated amount - ");
            double newAmt = sc.nextDouble();

            if(newAmt <= 0) {
                throw new InvalidInputException("Invalid amount!!");
            }
            List<String[]> rows = new ArrayList<>();

            // read the file and go to the line where the exp id
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                boolean found = false;
                String line;
//                boolean header = true;

                while ((line = br.readLine()) != null) {
//                    if (header) {
//                        header = false;
//                        continue;
//                    }
                    if (line.trim().isBlank()) continue;

                    String[] parts = line.trim().split(", ");
                    String id = parts[0];
                    if (id.equals(expId)) {
                        parts[2] = String.valueOf(newAmt);
                        found = true;
                    }

                    rows.add(parts);
                }

                if (!found) {
                    throw new ExpenseNotFoundException("No such Expense ID exists");
                }
                else {
                    // write back the updated csv
                    try(BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))){
                        for(String[] row :rows) {
                            bw.write(String.join(", ", row));
                            bw.newLine();
                        }
                    }
                    System.out.println("Successfully updated the amount!");
                }
            } catch (ExpenseNotFoundException e) {
                throw new ExpenseNotFoundException("Expense not found! " + e);
            }


        } else if (ch == 2) {
            System.out.println("Enter updated category - ");
            String cat = sc.next().trim();
            if(cat.isBlank()) {
                throw new InvalidInputException("Category cannot be empty!");
            }
            List<String[]> rows = new ArrayList<>();

            // read the file and go to the line where the exp id
            try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                boolean found = false;
                String line;
//                boolean header = true;

                while ((line = br.readLine()) != null) {
//                    if (header) {
//                        header = false;
//                        continue;
//                    }
                    if (line.trim().isBlank()) continue;

                    String[] parts = line.trim().split(", ");
                    String id = parts[0];
                    if (id.equals(expId)) {
                        parts[3] = cat;
                        found = true;
                    }

                    rows.add(parts);
                }

                if (!found) throw new ExpenseNotFoundException("No such Expense ID exists");
                else {
                    // write back the updated csv
                    try(BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))){
                        for(String[] row :rows) {
                            bw.write(String.join(", ", row));
                            bw.newLine();
                        }
                    }
                    System.out.println("Successfully updated the category!");
                }
            }
        }
    }

    @Override
    public void viewAll() throws FileNotFoundException {
        List<Expense> list = readExp();
        if (list.isEmpty()) System.out.println("no expenses!!");
        System.out.println(list);
    }

    @Override
    public void viewByCategory() throws FileNotFoundException, ExpenseNotFoundException {
        Scanner sc = new Scanner(System.in);
        List<Expense> list = readExp();
        if (list.isEmpty()) {
            throw new ExpenseNotFoundException("No Expenses found");
        }
        System.out.println();

        System.out.println("1. FOOD\n 2. BILLS\n 3. TRAVEL\n 4. MISC");
        int ans = sc.nextInt();
        String cat;
        if(ans == 1) {
            cat = "FOOD";
        } else if(ans==2) {
            cat = "BILLS";
        } else if(ans == 3) {
            cat = "TRAVEL";
        } else if(ans == 4) {
            cat = "MISC";
        } else {
            cat = "";
            System.out.println("Invalid input!");
        }

        List<Expense> filtered = list.stream()
                .filter(e->e.getCategory().equals(cat))
                .toList();

        if(filtered.isEmpty()) System.out.println("No Expenses!");
        else {
            System.out.println(filtered);
        }
    }

    @Override
    public void viewByDate() throws FileNotFoundException {
        //will put two options - 1. at a specific date  2. between two dates
        Scanner sc = new Scanner(System.in);
        List<Expense> list = readExp();
        if (list.isEmpty()) System.out.println("no expenses!!");

        System.out.println("1. View at a certain date\n 2. View between two dates");
        int ch = sc.nextInt();
        if(ch==1) {
            System.out.println("Enter date (YYYY-mm-dd) - ");
            LocalDate date = LocalDate.parse(sc.next().trim());
            List<Expense> filtered = list.stream()
                    .filter(e->e.getDateTime().toLocalDate().equals(date))
                    .toList();

            if(filtered.isEmpty()) System.out.println("No Expenses!");
            else {
                System.out.println(filtered);
            }

        } else if (ch==2) {
            System.out.println("Enter start date (YYYY-mm-dd) - ");
            LocalDate date1 = LocalDate.parse(sc.next().trim());
            System.out.println("Enter end date (YYYY-mm-dd) - ");
            LocalDate date2 = LocalDate.parse(sc.next().trim());

            List<Expense> filtered = list.stream()
                    .filter(e->(!e.getDateTime().toLocalDate().isBefore(date1)) && (!e.getDateTime().toLocalDate().isAfter(date2)))
                    .toList();

            if(filtered.isEmpty()) System.out.println("No Expenses!");
            else {
                System.out.println(filtered);
            }

        } else {
            System.out.println("Invalid input!");
        }

    }

    @Override
    public void export() throws IOException, InvalidInputException {
        List<Expense> list = readExp();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the name of the new file - ");
        String name = sc.next().trim();
        if(name.isBlank()) {
            throw new InvalidInputException("File name cannot be empty!");
        }
        else {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(name))){
                for(Expense e : list) {
                    String toCsv = e.getId() + ", " + e.getCategory() + ", " + e.getAmount() + ", " + e.getDateTime();
                    bw.write(toCsv);
                    bw.newLine();
                }
            }
        }

        System.out.println("Exported to the new file!");

    }
}
