package tran.example.data;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import tran.example.presentation.model.Blog;

public class BlogDAO {

	private final static String GET_BLOG_POSTS = "SELECT * FROM Blogs ORDER BY blog_dateModified DESC";
	private final static String GET_BLOG_POST = "SELECT * FROM Blogs WHERE blog_ID = ?";
	private final static String INSERT_POST = "INSERT into Blogs(blog_title, blog_Content, blog_author, blog_dateCreated, blog_dateModified) VALUES (?, ?, ?, ?, ?)";
	private final static String GET_RECENT_INSERTED_POST = "SELECT * FROM Blogs WHERE blog_author = ? ORDER BY blog_ID DESC LIMIT 1";
	
	private final static String DELETE_POST = "DELETE FROM Blogs WHERE blog_ID = ? AND blog_author = ?";
	private final static String DELETE_POST_SUCCESS = "post successfully deleted!";
	private final static String DELETE_POST_NOT_AUTHOR = "post was not deleted";
	private final static String DELETE_POST_ERROR = "an error occured when deleting the post";
	
	private final static String EDIT_POST = "UPDATE Blogs SET blog_Content = ?, blog_dateModified = ? WHERE blog_ID = ?";
	private final static String EDIT_POST_SUCCESS = "post successfully edited!";
	private final static String EDIT_POST_NOT_CHANGED = "post has not been edited!";
	private final static String EDIT_POST_ERROR = "error while editing the post.";
	
		
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	private PlatformTransactionManager transactionManager;
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(this.dataSource);
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public List<Blog> getBlogs() {
		List<Blog> blogsList = new ArrayList<Blog>();
		try {
			LocalDateTime current_time = setDate();
			blogsList = jdbcTemplateObject.query(GET_BLOG_POSTS, new BlogMapper(current_time));
		}
		catch(DataAccessException e) {
			e.printStackTrace();
		}
		return blogsList;
	}
	
	// get a single Blog using the ID.
	public Blog getaBlog(int blogID) {
		Blog singlePost;
		try {
			LocalDateTime current_time = setDate();
			singlePost = jdbcTemplateObject.queryForObject(GET_BLOG_POST, new Object[]{blogID}, new BlogMapper(current_time));
		}
		catch(DataAccessException e) {
			e.printStackTrace();
			singlePost = null;
		}
		return singlePost;
	}
	
	// helper method to determine if the user can view certain options.
	public boolean isAuthorOfPost(String author, int blogID) {
		Blog getPost = getaBlog(blogID);
		if(getPost.getAuthor().equals(author))
			return true;
		else 
			return false;
	}
	
	// add a post.
	public int addBlog(String title, String content, String author) {
	    int createdBlogID = -1;
		TransactionDefinition def = new DefaultTransactionDefinition();
	    TransactionStatus status = transactionManager.getTransaction(def);
		try {
			LocalDateTime dateOfPost = setDate();
			jdbcTemplateObject.update(INSERT_POST, title, content, author, dateOfPost, dateOfPost);
			transactionManager.commit(status);
			createdBlogID = getCreatedBlogID(author);
			return createdBlogID;
		}
		catch(DataAccessException e) {
			transactionManager.rollback(status);
			return createdBlogID;
		}
	}
	
	// returns the most recently created blog (with the most recent ID)
	public int getCreatedBlogID(String author) {
		try {
			LocalDateTime current_time = setDate();
			Blog singlePost = jdbcTemplateObject.queryForObject(GET_RECENT_INSERTED_POST, new Object[]{author}, new BlogMapper(current_time));
			if(singlePost != null) {
				if((Integer) singlePost.getPostID() != null)
					return singlePost.getPostID();
				else
					return -1;
			}
			else
				return -1;
		}
		catch(DataAccessException e) {
			return -1;
		}
	}

	// helper method to get the current time.
	public LocalDateTime setDate() {
		return Timestamp.from(Instant.now()).toLocalDateTime();
	}
	
	public String removePost(int blogID, String userName) {
		try {
			if(isAuthorOfPost(userName, blogID)) {
				jdbcTemplateObject.update(DELETE_POST, blogID, userName);
				return DELETE_POST_SUCCESS;
			}
			else {
				return DELETE_POST_NOT_AUTHOR;
			}
		}
		catch(DataAccessException e) {
			return DELETE_POST_ERROR;
		}
	}
	
	public String updatePost(int blogID, String newContent) {
		// first check if there were any changes from the original post, if there isn't editing isn't necesary.
		Blog oldContent = new Blog();
		oldContent = getaBlog(blogID);
		
		//String delimitedContent = newContent.replaceAll("'", "''"); 
		if(newContent.equals(oldContent.getContent())) {
			return EDIT_POST_NOT_CHANGED;
		}
		else {
			try {
				jdbcTemplateObject.update(EDIT_POST, newContent, setDate(), blogID);
				return EDIT_POST_SUCCESS;
			}
			catch(DataAccessException e) {
				return EDIT_POST_ERROR;
			}
		}
	}
}
