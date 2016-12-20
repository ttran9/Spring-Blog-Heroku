package tran.example.presentation.model;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDateTime;

/**
 * This class will help with when a user is attempting to be created.
 * @author Todd
 */
public class User {
	
	/* 
	 * for now this class will be separate from the CustomuUser class which is used to help with authentication when a user attempts to log in.
	 * note: in the future I may combine the classes, for now (and for convenience) iIwill separate them
	 */
	// by default these fields are all empty.
	private String userName = "";
	private String password = "";
	private String validatePassword = "";
	private Boolean enabled;
	private String userRole;
	private LocalDateTime last_posted_time;
	
	private String message = "";
	private int user_id;

	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String password, String validatePassword) {
		this.password = password;
		this.validatePassword = validatePassword;
	}
	
	public User(String userName, String password, String validatePassword) {
		super();
		this.userName = userName;
		this.password = password;
		this.validatePassword = validatePassword;
	}
	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMessage() {
		return message;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public LocalDateTime getLastPostedTime() {return last_posted_time;}
	public void setLastPostedTime(LocalDateTime last_posted_time) {this.last_posted_time = last_posted_time;}

	public int getUserId() { return user_id;}
	public void setUserId(int user_id) {this.user_id = user_id;}

	public String encryptUserPassword() {
		return BCrypt.hashpw(this.password, BCrypt.gensalt(14));
	}

}
