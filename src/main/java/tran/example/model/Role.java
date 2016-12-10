package tran.example.model;

import org.springframework.security.core.GrantedAuthority;
 
public class Role implements GrantedAuthority{

	private static final long serialVersionUID = 2L;
	
	private String name; // this is actually the role itself, such as "ROLE_USER"

    public void setName(String name) {
        this.name = name;
    }
 
    // this is defined in the grantedauthority interface.
    public String getAuthority() {
        return this.name;
    }
}