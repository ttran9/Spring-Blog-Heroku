package tran.example.data;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
