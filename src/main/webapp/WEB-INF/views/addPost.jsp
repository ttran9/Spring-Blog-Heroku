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
		<form class="form-signin" action = "<c:url value="/processAddPost"/>" method="post">
			<c:if test = "${errorMessage!= null}" >
				<p class="lead blog-description">${errorMessage}</p>
			</c:if>
			<h1 class="blog-title">Create Post</h1>
			<label for="title">Subject/Title:</label> <br/>
			<c:choose>
				<c:when test="${entered_title != null}">
					<input class="form-control" type="text" id="title" name="title" maxlength="50" size="50" value="${entered_title}"required/> <br/> <br/>
				</c:when>

				<c:otherwise>
					<input class="form-control" type="text" id="title" name="title" maxlength="50" size="50" required/> <br/> <br/>
				</c:otherwise>
			</c:choose>

			<label>Post Content:</label> <br/>
			<c:choose>
				<c:when test="${entered_content != null}">
					<textarea class="form-control" rows="8" cols="80" id="content" name="content">${entered_content}</textarea> <br/> <br/>
				</c:when>

				<c:otherwise>
					<textarea class="form-control" rows="8" cols="80" id="content" name="content"> </textarea> <br/><br/>
				</c:otherwise>
			</c:choose>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Create Post</button>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>

	</div><!-- /.container -->

</body>
</html>
