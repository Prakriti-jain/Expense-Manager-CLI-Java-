import javax.security.sasl.AuthenticationException;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserOperations {
    String file_username = "users.csv";

    public UserOperations(){

    }

    public void register(String username, String password) throws IOException, UserAuthenticationException {
        //first check if username and password are valid
        check(username, password);

        //check is user already exists or not
        if (userExists(username, password)){
            throw new UserAuthenticationException("User Already Exists!");
        }

        String dateId = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmssSS"));
        String userFile = username+dateId;

        //write it in the csv
        File f = new File(file_username);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
            bw.write(username + ", " + password + ", " + userFile);
            bw.newLine();
        }

        //create a file for the specific user
//        File ff = new File(userFile);
//        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ff, true))) {
//            bw. write("Expense_ID, Date Time, Amount, Category");
//            bw.newLine();
//        }

        System.out.println("Successfully Registered!");
    }

    public boolean login(String username, String password) throws IOException {
        if(userExists(username, password)) {
            System.out.println("Successfully Login!");
            return true;
        } else {
            System.out.println("No user Exists, register first");
            return false;
        }
    }

    public void check(String username, String password){
    }

    public boolean userExists(String username, String password) throws IOException {
        File f = new File(file_username);
        if(!f.exists()) return false;

        try(BufferedReader br = new BufferedReader(new FileReader(file_username))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String name = parts[0].trim();
                String pass = parts[1].trim();

                if(parts.length > 0  && name.equals(username) && pass.equals(password)) {
                    return true;
                }
            }
        }


        return false;
    }
}
