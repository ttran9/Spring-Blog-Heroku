package tran.example.data;

import org.springframework.jdbc.core.RowMapper;
import tran.example.presentation.model.Blog;
import tran.example.service.BlogService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class BlogMapper implements RowMapper<Blog> {

	LocalDateTime current_time;
	public BlogMapper(LocalDateTime current_time) {
		this.current_time = current_time;
	}

	public Blog mapRow(ResultSet rs, int rowNum)  {
		Blog blogPost = new Blog();
		BlogService blogService = new BlogService();
		try {
			blogPost.setPostID(rs.getInt("blog_ID"));
			blogPost.setTitle(rs.getString("blog_title"));
			blogPost.setContent(rs.getString("blog_content"));
			blogPost.setAuthor(rs.getString("blog_author"));
			blogPost.setFullDatedCreated(rs.getTimestamp("blog_dateCreated").toLocalDateTime());
			blogPost.setDateCreated(blogService.convertDateForDisplay(blogPost.getFullDateCreated()) +
					blogService.getTimeSincePost(blogPost.getFullDateCreated(), this.current_time));
			blogPost.setFullDatedModified(rs.getTimestamp("blog_dateModified").toLocalDateTime());
			blogPost.setDateModified(blogService.convertDateForDisplay(blogPost.getFullDateModified()) +
					blogService.getTimeSincePost(blogPost.getFullDateModified(), this.current_time));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return blogPost;
	}
}