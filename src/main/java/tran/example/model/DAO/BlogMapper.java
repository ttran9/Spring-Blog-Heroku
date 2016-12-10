package tran.example.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;
import tran.example.model.Blog;

public class BlogMapper implements RowMapper<Blog> {

	LocalDateTime current_time;
	public BlogMapper(LocalDateTime current_time) {
		this.current_time = current_time;
	}

	public Blog mapRow(ResultSet rs, int rowNum)  {
		Blog blogPost = new Blog();
		try {
			blogPost.setPostID(rs.getInt("blog_ID"));
			blogPost.setTitle(rs.getString("blog_title"));
			blogPost.setContent(rs.getString("blog_content"));
			blogPost.setAuthor(rs.getString("blog_author"));
			blogPost.setFullDatedCreated(rs.getTimestamp("blog_dateCreated").toLocalDateTime());
			blogPost.setDateCreated(blogPost.convertDateForDisplay(blogPost.getFullDateCreated()) +
					blogPost.getTimeSincePost(blogPost.getFullDateCreated(), this.current_time));
			blogPost.setFullDatedModified(rs.getTimestamp("blog_dateModified").toLocalDateTime());
			blogPost.setDateModified(blogPost.convertDateForDisplay(blogPost.getFullDateModified()) +
					blogPost.getTimeSincePost(blogPost.getFullDateModified(), this.current_time));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return blogPost;
	}
}