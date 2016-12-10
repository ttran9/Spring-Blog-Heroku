package tran.example.model.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import tran.example.model.CustomUser;
import tran.example.model.Role;
import tran.example.model.DAO.UserDAO;
 
@Repository
public class UserDAOImpl {
	
	public CustomUser loadUserByUsername(final String username) {
		CustomUser user = new CustomUser();
		
		ApplicationContext appContext =  new ClassPathXmlApplicationContext("spring/database/Datasource.xml");
		UserDAO getUserInfo = (UserDAO)appContext.getBean("userDS");
		//UserRolesDAO getRole = (UserRolesDAO)appContext.getBean("userRoleDS");
		
		user = getUserInfo.getUser(username);
		
		// get the proper fields.
		          
		// query for the user permission.
		Role userRole = new Role();
		userRole = getUserInfo.getUserRole(username);
		
		List<Role> roles = new ArrayList<Role>();
		roles.add(userRole);
		user.setAuthorities(roles);
		
		((ConfigurableApplicationContext)appContext).close();
		return user;

     }
 
}