<%@ page import="tran.example.presentation.model.Blog"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file = "styleInclude.jsp"%>
<title>Edit A Post</title>
</head>
<body>
	<c:choose>
		<c:when test="${postToEdit != null && loggedInName != null}">
			<div class="blog-masthead">
		      <div class="container">
		        <nav class="blog-nav">
					<a class="blog-nav-item active" href="<c:url value="/"/>">Home!</a>
					<a class="blog-nav-item active" href="<c:url value="/showPosts"/>">View All Posts!</a>
					<a class="blog-nav-item active" href="<c:url value="/showSinglePost?blogID=${postToEdit.getPostID()}"/>">Go back to this post!</a>
		        </nav>
		      </div>
		    </div>
		    
	    	<div class = "userName">
	    		Hello, ${loggedInName} <a href = "<c:url value="/logout"/>">(logout)</a>
	    	</div>

		    <div class="container">
				<form class="form-signin" action = "<c:url value="/processEditPost"/>" method="post">
					<c:if test = "${errorMessage!= null}" >
						<p class="lead blog-description">${errorMessage}</p>
					</c:if>
					<h2 class="form-signin-heading">Edit Your Post</h2><br/>
					<label for="title">Post Title:</label>
					<input type="text" id="title" name="title" class="form-control" maxlength="50" size="50" value="${postToEdit.getTitle()}" readonly>
					<label for="content">Content:</label><br/>
					<textarea class="form-control" rows="8" cols="80" id="content" name="content">${postToEdit.getContent()}</textarea> <br/> <br/>
					<button class="btn btn-lg btn-primary btn-block" type="submit">Edit Post</button>
					<input type="hidden" name="blogID" value="${postToEdit.getPostID()}"/>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				</form>
		    </div><!-- /.container -->
	    </c:when>
	    
	    <c:otherwise>
	    	<%
	    		String redirectURL = response.encodeURL(request.getContextPath());
	    		response.sendRedirect(redirectURL); 
	    	%>
	    </c:otherwise>
	</c:choose>
</body>
</html>