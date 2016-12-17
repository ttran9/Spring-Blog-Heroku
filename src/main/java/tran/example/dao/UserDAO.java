package tran.example.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import tran.example.presentation.model.CustomUser;
import tran.example.presentation.model.Role;
import tran.example.presentation.model.User;

import javax.sql.DataSource;
import java.time.LocalDateTime;

public class UserDAO {

	private final static String INSERT_INTO_USERS = "insert into Users (user_Name, user_Password, enabled) values (?, ?, ?)";
	private final static String USER_INSERTION_SUCCESS = "user was successfully created, go to the login page to log in!";
	private final static String USER_INSERTION_ERROR = "user already exists, try another user name.";
	private final static String INSERT_INTO_USER_ROLES = "insert into User_Roles (username, role) values (?, ?)";
	
	private final static String DELETE_FROM_USERS = "delete FROM users WHERE user_Name = ?";
	private final static String DELETE_USER_ROLE = "delete FROM User_Roles WHERE username = ?";
	private final static String USER_DELETION_SUCCESS = "user successfully deleted";
	private final static String USER_DELETION_ERROR =  "could not delete the user";
	
	private final static String GET_USER = "SELECT * FROM Users WHERE user_Name = ?";
	private final static String GET_USER_ROLE = "SELECT role FROM User_Roles WHERE username = ?";
	
	private final static String CHECK_FOR_USERNAME = "SELECT user_Name FROM Users where user_Name = ?";
	private final static String UPDATE_LAST_POSTED_TIME = "UPDATE Users SET last_posted_time = ? WHERE user_Name = ?";


	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private PlatformTransactionManager transactionManager;
	   
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(this.dataSource);
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public String create(String name, String password, Boolean isEnabled, String userRole) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = transactionManager.getTransaction(def);
	    try {  
	    	jdbcTemplateObject.update(INSERT_INTO_USERS, name, password, isEnabled);
	    	createUserRole(name, userRole);
	    	transactionManager.commit(status);
	    	return USER_INSERTION_SUCCESS; // success
	    }
	    catch(DataAccessException e) {
	    	transactionManager.rollback(status);
	    	return USER_INSERTION_ERROR;
	    }
	}
	
	public int createUserRole(String name, String userRole) throws DataAccessException {
	    return jdbcTemplateObject.update(INSERT_INTO_USER_ROLES, name, userRole);
	}
	
	// these delete methods are not used the moment but not implemented. they will have to be tested when the time comes.
	public String delete(String name) {
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = transactionManager.getTransaction(def);
		try {
			jdbcTemplateObject.update(DELETE_FROM_USERS, name);
			deleteUserRole(name);
			transactionManager.commit(status);
			return USER_DELETION_SUCCESS;
		}
		catch(DataAccessException e) {
			transactionManager.rollback(status);
			return USER_DELETION_ERROR;
		}
		
	}
	
	public int deleteUserRole(String name) throws DataAccessException {
		return jdbcTemplateObject.update(DELETE_USER_ROLE, name);
	}
	

	// this is to get detailed user information
	public CustomUser getUser(String userName) {
		try {
			return jdbcTemplateObject.queryForObject(GET_USER, new Object[]{userName}, new CustomUserMapper());
		}
		catch(DataAccessException e) {
			return new CustomUser();
		}
	}	
	
	// this is for authentication.
	public Role getUserRole(String userName) {
		try {
			return jdbcTemplateObject.queryForObject(GET_USER_ROLE, new Object[]{userName}, new RoleMapper());
		}
		catch(DataAccessException e) {
			return new Role();
		}
	}
	
	// check if a user name exists.
	public int checkForUserName(String userName) {
		try {
			return jdbcTemplateObject.queryForObject(CHECK_FOR_USERNAME, new Object[]{userName}, new UserNameMapper());
		}
		catch(DataAccessException e) {
			return 1; // user name couldn't be retrieved, does not exist.
		}
	}

	// update the time a user has last posted.
	public int updateLastPostedTimeForUser(LocalDateTime current_time, String userName){
		try {
			return jdbcTemplateObject.update(UPDATE_LAST_POSTED_TIME, current_time, userName);
		}
		catch(DataAccessException e) {
			return -1; // couldn't update the last posted time.
		}
	}

	// check if a user can post
	public boolean canUserPost(String userName) {
		boolean canPost = false;
		try {
			User user = jdbcTemplateObject.queryForObject(GET_USER, new Object[]{userName}, new UserMapper());
			if(user != null &&  user.getLastPostedTime() == null) {
				canPost = true;
			}
			else if (user != null && user.getLastPostedTime() != null) {
				canPost = user.canUserPost(user.getLastPostedTime());
			}
		}
		catch(DataAccessException e) {
			System.out.println("retrieval related error");
		}
		return canPost;
	}
}
