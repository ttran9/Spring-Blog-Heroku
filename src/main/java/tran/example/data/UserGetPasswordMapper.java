package tran.example.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserGetPasswordMapper implements RowMapper<String> {

	public String mapRow(ResultSet rs, int rowNum) {
		// TODO Auto-generated method stub
		try {
			return rs.getString("user_Password");
		}
		catch(SQLException e)
		{
			return "";
		}
	}

}
