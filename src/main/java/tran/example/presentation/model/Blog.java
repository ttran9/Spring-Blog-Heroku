package tran.example.presentation.model;

import tran.example.service.BlogService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class Blog {

	private String title;

	private String content;

	private int postID;

	private String author;

	private String dateCreated;

	private LocalDateTime fullDateCreated;

	private String dateModified;

	private LocalDateTime fullDateModified;

	public Blog() {}

	// constructor getting the posts.
	public Blog(int postID, String title, String content, String author, String dateCreated,
				LocalDateTime fullDateCreated, String dateModified, LocalDateTime fullDateModified) {
		this.postID = postID;
		this.title = title;
		this.content = content;
		this.author = author;
		this.dateCreated = dateCreated;
		this.fullDateCreated = fullDateCreated;
		this.dateModified = dateModified;
		this.fullDateModified = fullDateModified;
	}

	// constructor for inserting from a form.
	public Blog(String title, String content, String author) {
		LocalDateTime current_time = setDate();
		BlogService blogService = new BlogService();
		String convertedDate = blogService.convertDateForDisplay(current_time);
		this.title = title;
		this.content = content;
		this.author = author;
		this.dateCreated = convertedDate;
		this.fullDateCreated = current_time;
		this.dateModified = convertedDate;
		this.fullDateModified = current_time;
		blogService = null;
	}

	// constructor for testing the MySQL connection.
	public Blog(String title, String content, int postID, String author) {
		LocalDateTime current_time = setDate();
		BlogService blogService = new BlogService();
		String convertedDate = blogService.convertDateForDisplay(current_time);
		this.title = title;
		this.content = content;
		this.postID = postID;
		this.author = author;
		this.dateCreated = convertedDate;
		this.fullDateCreated = current_time;
		this.dateModified = convertedDate;
		this.fullDateModified = current_time;
		blogService = null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPostID() {
		return postID;
	}

	public void setPostID(int postID) {
		this.postID = postID;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getFullDateCreated() {
		return this.fullDateCreated;
	}

	public void setFullDatedCreated(LocalDateTime fullDateCreated) {
		this.fullDateCreated = fullDateCreated;
	}

	public LocalDateTime setDate() {
		return Timestamp.from(Instant.now()).toLocalDateTime();
	}

	public String getDateModified() {
		return dateModified;
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	public LocalDateTime getFullDateModified() {
		return this.fullDateModified;
	}

	public void setFullDatedModified(LocalDateTime fullDateModified) {
		this.fullDateModified = fullDateModified;
	}

	public boolean hasPostBeenModified() {
		BlogService blogService = new BlogService(this.fullDateCreated, this.fullDateModified);
		return blogService.hasPostBeenModified();
	}
}