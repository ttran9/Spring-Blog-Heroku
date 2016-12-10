<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
	<title>Add A Post</title>
</head>
<body>
	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
			<a class="blog-nav-item active" href="<c:url value="/"/>">Home!</a>
			<a class="blog-nav-item active" href="<c:url value="/showPosts"/>">View All Posts!</a>
        </nav>
      </div>
    </div>

	<c:if test="${loggedInName != null}">
		<div class = "userName">
			Hello, ${loggedInName} <a href = "<c:url value="/logout"/>">(logout)</a>
		</div>
	</c:if>
	
	<div class="container">
		<div class="blog-header">
			<h1 class="blog-title">Create Post</h1>
			<p class="lead blog-description">Fill in the information below to create your post!</p>
			<c:if test = "${errorMessage!= null}" >
				<p class="lead blog-description">${errorMessage}</p>
			</c:if>
		</div>
		
		<div class="row">
			<div class="col-sm-8 blog-main">
				<div class="blog-post">
					<form action = "<c:url value="/processAddPost"/>" method="post">
						<label>Subject/Title:</label> <br/>
						<c:choose>
							<c:when test="${entered_title != null}">
								<input type = "text" name = "title" maxlength = "50" size = "50" value="${entered_title}"required/> <br/> <br/>
							</c:when>

							<c:otherwise>
								<input type = "text" name = "title" maxlength = "50" size = "50" required/> <br/> <br/>
							</c:otherwise>
						</c:choose>

						<label>Post Content:</label> <br/>
						<c:choose>
							<c:when test="${entered_content}">
								<textarea rows = "8" cols = "80" name = "content">${entered_content}</textarea> <br/> <br/>
							</c:when>

							<c:otherwise>
								<textarea rows = "8" cols = "80" name = "content"> </textarea> <br/> <br/>
							</c:otherwise>
						</c:choose>
						<button type="submit">Create Post</button>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>
				</div><!-- /.blog-post -->
			</div><!-- /.blog-main -->
		</div><!-- /.row -->	
	</div><!-- /.container -->

</body>
</html>
