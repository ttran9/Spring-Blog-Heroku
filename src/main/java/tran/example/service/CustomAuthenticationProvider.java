package tran.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tran.example.presentation.model.CustomUser;

import java.util.Collection;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider, UserDetailsService{

    @Autowired
    private UserDAOImpl userDao;
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	String username = authentication.getName();
    	String password = (String) authentication.getCredentials();
     
        CustomUser user = loadUserByUsername(username);
        UserService userService = new UserService(username, password);

        // TODo: implement checking # of login attempts after finishing the add/delete/edit posts!!!!
        
        if(username.equals("") && password.equals("")) {
        	throw new BadCredentialsException("Input fields cannot be empty.");
        }
        else {
            if ((user == null) || user.getUsername() == null || !user.getUsername().equalsIgnoreCase(username) ||
                    !(BCrypt.checkpw(password, user.getPassword())) || !userService.validateLogin()) {
                throw new BadCredentialsException("Incorrect credentials");
            }
        }
      
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

    public CustomUser loadUserByUsername(String username) {
        try {
            return userDao.loadUserByUsername(username);
        }
        catch(UsernameNotFoundException e) {
            return null;
        }
    }

}
