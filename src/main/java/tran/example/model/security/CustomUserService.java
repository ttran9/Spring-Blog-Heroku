package tran.example.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tran.example.model.CustomUser;
 
@Service
public class CustomUserService implements UserDetailsService {
 
    @Autowired
    private UserDAOImpl userDao;
     
    public CustomUser loadUserByUsername(String username) {
    	try {
    		return userDao.loadUserByUsername(username);
    	}
    	catch(UsernameNotFoundException e) {
    		return new CustomUser();
    	}
    }
 
}