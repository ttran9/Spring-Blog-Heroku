package tran.example.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserNameMapper implements RowMapper<Integer> {
	public Integer mapRow(ResultSet rs, int rowNum)  {
		Integer returnCode = 1;
		try {
			if(rs.getString("user_Name") != null) 
				returnCode = -1; // this indicates the user name already exists. so display an error.
		}
		catch(SQLException e) {
			return -2; // some type of syntactical error.
		}
		return returnCode;
	}
}
