package tran.example.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import tran.example.model.Role;

public class RoleMapper implements RowMapper<Role> {
	public Role mapRow(ResultSet rs, int rowNum)  {
		Role userRole = new Role();
		try {
			userRole.setName(rs.getString("role"));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return userRole;
	}
}
