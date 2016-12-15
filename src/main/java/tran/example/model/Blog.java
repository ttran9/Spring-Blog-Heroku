package tran.example.model;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.Duration;

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
	public Blog(int postID, String title, String content, String author, String dateCreated,LocalDateTime fullDateCreated, String dateModified, LocalDateTime fullDateModified) {
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
		String convertedDate = convertDateForDisplay(current_time);
		this.title = title;
		this.content = content;
		this.author = author;
		this.dateCreated = convertedDate;
		this.fullDateCreated = current_time;
		this.dateModified = convertedDate;
		this.fullDateModified = current_time;
	}

	// constructor for testing the MySQL connection.
	public Blog(String title, String content, int postID, String author) {
		LocalDateTime current_time = setDate();
		String convertedDate = convertDateForDisplay(current_time);
		this.title = title;
		this.content = content;
		this.postID = postID;
		this.author = author;
		this.dateCreated = convertedDate;
		this.fullDateCreated = current_time;
		this.dateModified = convertedDate;
		this.fullDateModified = current_time;
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

	public String convertDateForDisplay(LocalDateTime fullDate) {
		StringBuilder formatted_date = new StringBuilder();

		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("EEEE MMMM d, u");
		DateTimeFormatter dateformat2 = DateTimeFormatter.ofPattern("h:mm a");
		formatted_date.append(fullDate.format(dateformat));
		formatted_date.append(" at ");
		formatted_date.append(fullDate.format(dateformat2));

		return formatted_date.toString();
	}

	public String getTimeSincePost(LocalDateTime priorTime, LocalDateTime currentTime) {
		StringBuilder timeSinceLastPost = new StringBuilder();
		LocalDateTime current_time = LocalDateTime.from(currentTime);
		LocalDateTime tempDateTime = LocalDateTime.from(priorTime);

		long years = tempDateTime.until(current_time, ChronoUnit.YEARS);
		if(years > 0) {
			if(years == 1) {
				timeSinceLastPost.append("" + years + "yr, ");
			}
			else {
				timeSinceLastPost.append("" + years + "yrs, ");
			}
		}
		tempDateTime = tempDateTime.plusYears(years);

		long months = tempDateTime.until(current_time, ChronoUnit.MONTHS);
		if(months > 0) {
			if(months == 1) {
				timeSinceLastPost.append("" + months + "mth, ");
			}
			else {
				timeSinceLastPost.append("" + months + "mths, ");
			}
		}
		tempDateTime = tempDateTime.plusMonths(months);

		long weeks = tempDateTime.until(current_time,  ChronoUnit.WEEKS);
		if(weeks > 0) {
			if(weeks == 1) {
				timeSinceLastPost.append("" + weeks + "wk, ");
			}
			else {
				timeSinceLastPost.append("" + weeks + "wks, ");
			}
		}
		tempDateTime = tempDateTime.plusWeeks(weeks);

		long days = tempDateTime.until(current_time, ChronoUnit.DAYS);
		if(days > 0) {
			if(days == 1) {
				timeSinceLastPost.append("" + days + "day, ");
			}
			else {
				timeSinceLastPost.append("" + days + "days, ");
			}
		}
		tempDateTime = tempDateTime.plusDays(days);

		long hours = tempDateTime.until(current_time, ChronoUnit.HOURS);
		if(hours > 0) {
			if(hours == 1) {
				timeSinceLastPost.append("" + hours + "hr, ");
			}
			else {
				timeSinceLastPost.append("" + hours + "hrs, ");
			}
		}
		tempDateTime = tempDateTime.plusHours(hours);

		long minutes = tempDateTime.until(current_time, ChronoUnit.MINUTES);
		if(minutes > 0) {
			if(minutes == 1) {
				timeSinceLastPost.append("" + minutes + "min, ");
			}
			else {
				timeSinceLastPost.append("" + minutes + "mins, ");
			}
		}
		tempDateTime = tempDateTime.plusMinutes(minutes);

		long seconds = tempDateTime.until(current_time, ChronoUnit.SECONDS);
		if(seconds > 0) {
			timeSinceLastPost.append("" + seconds + "s, ");
		}

		if(timeSinceLastPost.length() > 0) {
			timeSinceLastPost.delete(timeSinceLastPost.length()-2, timeSinceLastPost.length());
			timeSinceLastPost.append(" ago");
			timeSinceLastPost.insert(0, " (");
			timeSinceLastPost.append(").");
			return timeSinceLastPost.toString();
		}
		else {
			return " (0s ago).";
		}
	}

	/**
	 * @return True if the blog post has been modified.
	 */
	public boolean hasPostBeenModified() {
		return !(this.fullDateCreated.equals(this.fullDateModified));
	}
}