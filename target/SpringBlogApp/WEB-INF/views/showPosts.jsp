<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="tran.example.presentation.model.Blog"%>
<!DOCTYPE html>
<html>
<head>
	<title>Posts Written</title>
	<%@ include file = "styleInclude.jsp"%>
</head>
<body>
	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
			<a class="blog-nav-item active" href="<c:url value="/"/>">Home!</a>
			<c:if test="${loggedInName != null}">
				<a class="blog-nav-item active" href="<c:url value="/addPost"/>">Make A Post!</a>
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
	        <h1 class="blog-title">Todd's Spring Java MVC Blog.</h1>
	        <p class="lead blog-description">This uses bootstrap styling and Java as the server-side programming language. <br/> I have integrated Spring MVC and Spring Security into this version.</p>
         	<c:if test="${errorMessage != null}">
         		<br/>
    			<div class="lead blog-description" id = "infoField">${errorMessage}</div>
         	</c:if>
	      </div>
		
	     <div class="row">
         	<div class="col-sm-8 blog-main">
         	<c:choose>
         		<c:when test="${blogs != null}">
         			<c:forEach var="blogPost"  items="${blogs}">
       					<div class="blog-post">
	        				<h2 class="blog-post-title">
	        					<a href = "<c:url value="/showSinglePost?blogID=${blogPost.getPostID()}"/>">${blogPost.getTitle()}</a>
	        				</h2>
	        				<p class="blog-post-meta"> Written by ${blogPost.getAuthor()} on ${blogPost.getDateCreated()} </p>
       						<c:if test="${blogPost.hasPostBeenModified()}">
       							<p class="blog-post-meta"> Last modified by ${blogPost.getAuthor()} on ${blogPost.getDateModified()} </p>
       						</c:if>
       						<pre><code>${blogPost.getContent()}</code></pre>
       						<hr>
       					</div>
         			</c:forEach>
         		</c:when>
         		
         		<c:otherwise>
         			<br/>
         			<div class="row">
         				<div class="col-sm-8 blog-main">
         					<pre><code>Blog has no posts :(.</code></pre>
         				</div>
         			</div>
         		</c:otherwise>
         	</c:choose>
			</div> <!-- end blog-main -->
		</div> <!-- end row class -->	
	</div> <!-- end container -->

</body>
</html>
