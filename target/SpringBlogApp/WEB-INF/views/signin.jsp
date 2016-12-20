<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head> 
	<title>Login Page</title>
	<%@ include file = "styleInclude.jsp"%>
	<%@ include file = "validationInclude.jsp"%>
</head>
<body class = "body-background">
	<div class="blog-masthead">
      <div class="container">
        <nav class="blog-nav">
			<a class="blog-nav-item active" href="<c:url value="/"/>">Home!</a>
			<a class="blog-nav-item active" href="<c:url value="/showPosts"/>">View All Posts!</a>
			<a class="blog-nav-item active" href="<c:url value="/register"/>">Create An Account!</a>
        </nav>
      </div>
    </div>
    
    <div class="container">
        <form class="form-signin" method="POST" action = "<c:url value="/login"/>" data-parsley-validate>
			<h2 class="form-signin-heading">Login Below</h2>
			<c:choose>
				<c:when test="${userNameEntered != null}">
					<input type="text" name="username" class="form-control" value = "${userNameEntered}" maxlength="16" size="16" data-parsley-usernamecheck="^[a-z0-9_-]{6,35}$" data-parsley-required novalidate/>
				</c:when>
				<c:otherwise>
					<input type="text" name="username" class="form-control" placeholder= "user name here" maxlength="16" size="16" data-parsley-usernamecheck="^[a-z0-9_-]{6,35}$" data-parsley-required novalidate/>				
				</c:otherwise>
			</c:choose>
			<input type="password" name="password" class="form-control" placeholder = "password here" maxlength = "16" size = "16" data-parsley-passwordcheck = "((?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?!.*\\s).{6,20})" 
    		data-parsley-required="true" novalidate>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Login!</button>
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			<c:if test="${error != null}">
				<p class="lead blog-description">${error}</p>
			</c:if>
		</form>
	</div>

</body>
</html>