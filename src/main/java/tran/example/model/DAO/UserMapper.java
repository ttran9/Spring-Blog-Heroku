package tran.example.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import tran.example.model.User;

public class UserMapper implements RowMapper<User> {
   public User mapRow(ResultSet rs, int rowNum)  {
	   User user = new User();
	   try {
		   user.setUserName(rs.getString("user_Name"));
		   user.setPassword(rs.getString("user_Password"));
		   user.setEnabled(rs.getBoolean("enabled"));
		   user.setUserId(rs.getInt("user_ID"));
		   user.setLastPostedTime(rs.getTimestamp("last_posted_time").toLocalDateTime());
	   }
	   catch(SQLException e) {
		   e.printStackTrace();
	   }
	   return user;
   }
}
