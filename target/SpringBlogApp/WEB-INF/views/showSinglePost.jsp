<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="tran.example.presentation.model.Blog"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
	<title>Your selected post</title>
</head>
<body>
	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
			<a class="blog-nav-item active" href="<c:url value="/"/>">Home!</a>
			<a class="blog-nav-item active" href="<c:url value="/showPosts"/>">View All Posts!</a>
			<c:if test="${loggedInName != null}">
				<a class="blog-nav-item active" href="<c:url value="/addPost"/>">Add A Post!</a>
			</c:if>
		</nav>
      </div>
    </div>
    
   	<c:choose>
		<c:when test="${loggedInName != null}">
			<div class = "userName">
				Hello, ${loggedInName} <a href = "<c:url value="/logout"/>">(logout)</a>
			</div>
		</c:when>
		<c:otherwise>
			<div class = "userName">
	    		<a href = "<c:url value="/signin"/>">Login</a>
	    		|
	    		<a href = "<c:url value="/register"/>">Register</a>
    		</div>	
		</c:otherwise>
	</c:choose>

	<div class = "container">
		<div class="blog-header">
			<div class="row">
				<div class="col-sm-8 blog-main">
					<div class="blog-post">
						<c:choose>
							<c:when test="${postContents != null}">
								<h2 class="blog-post-title">${postContents.getTitle()}</h2>
								<p class="blog-post-meta"> Written by ${postContents.getAuthor()} on ${postContents.getDateCreated()} </p>
								<c:if test="${postContents.hasPostBeenModified()}">
									<p class="blog-post-meta"> Last modified by ${postContents.getAuthor()} on ${postContents.getDateModified()} </p>
								</c:if>
								<pre><code>${postContents.getContent()}</code></pre>
								<c:if test="${isAuthorOfPost != null && isAuthorOfPost}">
									<h2><a class="extra-options" href = "<c:url value="/deleteSinglePost?blogID=${postContents.getPostID()}"/>">Delete This Post</a></h2>
									<h2><a class="extra-options-two" href = "<c:url value="/showEditPost?blogID=${postContents.getPostID()}"/>">Edit This Post</a></h2> <br/>
								</c:if>
								<c:if test="${errorMessage != null}">
									<p><h2 class="extra-options" >${errorMessage}</h2></p>
								</c:if>
							</c:when>
							<c:otherwise>
								<br/>
								<div class="row">
									<div class="col-sm-8 blog-main">
										<pre><code>Post doesn't exist!</code></pre>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
	
		
    
</body>
</html>