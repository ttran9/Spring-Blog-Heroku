package tran.example.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BlogService {

    private LocalDateTime fullDateCreated;

    private String dateModified;

    private LocalDateTime fullDateModified;

    public BlogService() {}

    public BlogService(LocalDateTime fullDateCreated, LocalDateTime fullDateModified) {
        this.fullDateCreated = fullDateCreated;
        this.fullDateModified = fullDateModified;
    }

    public void setFullDateCreated(LocalDateTime fullDateCreated) {
        this.fullDateCreated = fullDateCreated;
    }

    public void setFullDateModified(LocalDateTime fullDateModified) {
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
