import javax.security.sasl.AuthenticationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserOperations {
    String file_username = "users.csv";

    public UserOperations(){

    }

    public void register(String username, String password) throws IOException, UserAuthenticationException {
        //first check if username and password are valid
        if (check(username, password)) {
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
            File ff = new File(userFile);
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(ff, true))) {

            }

            //create a file


            System.out.println("Successfully Registered!");
        } else System.out.println("Invalid password!");


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

    public boolean check(String username, String password) {
        if (password.length() < 8) {
            System.out.println("Password should be atleast 8 characters long!");
            return false;
        }
        boolean hasSpecial = false;
        boolean hasDigit = false;
        boolean hasLowercase = false;
        boolean hasUppercase = false;
        for(int i=0 ; i<password.length() ; i++) {
            char ch = password.charAt(i);
            if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (Character.isLetter(ch)) {
                if(Character.isLowerCase(ch)) hasLowercase = true;
                else hasUppercase = true;
            } else {
                hasSpecial = true;
            }
        }

        if(hasSpecial && hasDigit && hasLowercase && hasUppercase) {
            return true;
        }
        return false;
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
