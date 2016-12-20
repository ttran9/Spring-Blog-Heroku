package tran.example.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

public class UserService {

    private String userName;

    private String message;

    private String password;

    private String validatePassword;

    public UserService() {}

    public UserService(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserService(String userName, String password, String validatePassword) {
        this.userName = userName;
        this.password = password;
        this.validatePassword = validatePassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidatePassword() {
        return validatePassword;
    }

    public void setValidatePassword(String validatePassword) {
        this.validatePassword = validatePassword;
    }

    public boolean validate() {
        String userNameRegex = "^[a-z0-9_-]{6,35}$";
        String passwordRegex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})";

        if(!userName.matches(userNameRegex)) {
            message = "The user name must be at least 6 characters long and up to 35 characters.\nOnly lower case letters, numbers, an underscore , or hyphen are allowed!";
            return false;
        }

        if(!password.matches(passwordRegex)) {
            message = "The password must have at least one number, one lower and upper case letter, and one of the special symbols: '@', '#', '$', '%'.\nThe length must be between 6 to 20 characters.";
            return false;
        }
        if(!password.equals(validatePassword)) {
            message = "The entered passwords must match, try again!";
            return false;
        }

        return true;
    }

    public boolean validatePasswords() {
        String passwordRegex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})";

        if(!password.matches(passwordRegex)) {
            message = "The password must have at least one number, one lower and upper case letter, and one of the special symbols: '@', '#', '$', '%'.\nThe length must be between 6 to 20 characters.";
            return false;
        }
        if(!password.equals(validatePassword)) {
            message = "The entered passwords must match, try again!";
            return false;
        }

        return true;
    }

    public boolean checkUserName(String entered_user_name) {
        String userNameRegex = "^[a-z0-9_-]{6,35}$";
        if(!entered_user_name.matches(userNameRegex)) {
            message = "The user name must be 6 to 35 characters.\nOnly lower case letters, numbers, an underscore , or hyphen are allowed!";
            return false;
        }
        return true;
    }

    public boolean validateLogin() {
        String userNameRegex = "^[a-z0-9_-]{6,35}$";
        String passwordRegex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})";

        if(!userName.matches(userNameRegex)) {
            message = "The user name must be at least 6 characters long and up to 35 characters.\nOnly lower case letters, numbers, an underscore , or hyphen are allowed!";
            return false;
        }

        if(!password.matches(passwordRegex)) {
            message = "The password must have at least one number, one lower and upper case letter, and one of the special symbols: '@', '#', '$', '%'.\nThe length must be between 6 to 20 characters.";
            return false;
        }

        return true;
    }

    public boolean canUserPost(LocalDateTime lastPostedTime) {
        LocalDateTime current_time = LocalDateTime.from(Timestamp.from(Instant.now()).toLocalDateTime());
        LocalDateTime user_last_posted_time = LocalDateTime.from(lastPostedTime);
        Duration difference = Duration.between(user_last_posted_time, current_time);

        return difference.getSeconds() >= 30;
    }
}
