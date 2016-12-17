package tran.example.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import tran.example.presentation.model.CustomUser;

public class CustomUserMapper implements RowMapper<CustomUser> {
    public CustomUser mapRow(ResultSet rs, int rowNum)  {
        CustomUser user = new CustomUser();
        try {
            user.setUsername(rs.getString("user_Name"));
            user.setPassword(rs.getString("user_Password"));
            user.setEnabled(rs.getBoolean("enabled"));
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}