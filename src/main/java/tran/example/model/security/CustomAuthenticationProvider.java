package tran.example.model.security;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tran.example.model.CustomUser;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider, UserDetailsService{

    @Autowired
    private UserDAOImpl userDao;
    
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
    	String username = authentication.getName();
    	String password = (String) authentication.getCredentials();
     
        CustomUser user = loadUserByUsername(username);
        
        // TODo: must also check for valid formatting of username and password..
        // TODo: implement checking # of login attempts after finishing the add/delete/edit posts!!!!
        
        if(username.equals("") && password.equals("")) {
        	throw new BadCredentialsException("Input fields cannot be empty.");
        }
        else {
        	// i am adding the user name into the message so the user doesn't need to type it again.
        	// This is a hack, though I'm not sure how reliable this is.
        	if (user.getUsername() == null || !user.getUsername().equalsIgnoreCase(username)) {
        		throw new BadCredentialsException(username + ":" + "Username not found.");
            }
        	
        	if(!(BCrypt.checkpw(password, user.getPassword()))) {
				throw new BadCredentialsException(username + ":" + "Wrong password.");
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
            return new CustomUser();
        }
    }

}
