<%@ page import="tran.example.model.Blog"%>
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
				<div class="blog-header">
			        <h1 class="blog-title">Edit Post</h1>
			        <p class="lead blog-description">Fill in the information below to edit your post!</p>
				</div>
		
				<div class="row">
					<div class="col-sm-8 blog-main">
						<div class="blog-post">
							<form action = "<c:url value="/processEditPost"/>" method="post">
								<h2 class="blog-post-title">${postToEdit.getTitle()}</h2><br/>
								<label>Content:</label><br/>
								<textarea rows = "8" cols = "80" name = "content">${postToEdit.getContent()}</textarea> <br/> <br/>
								<button type="submit">Edit Post</button>
								<input type="hidden" name="blogID" value="${postToEdit.getPostID()}"/>
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							</form>
		          		</div><!-- /.blog-post -->
		        	</div><!-- /.blog-main -->
		      	</div><!-- /.row -->
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