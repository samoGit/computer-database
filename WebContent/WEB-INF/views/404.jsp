<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand"
				href="Dashboard?pageNumber=1&nbComputersByPage=${nbComputersByPage}">
				<fmt:message key="label.subTitle" />
			</a>
            <div class="pull-right btnLang" style="margin-top: 7.5px;">
            	<a	class="btn btn-default <c:if test="${pageContext.response.locale eq 'fr'}">btn-primary</c:if>" 
            		href="?lang=fr">FR</a>
            	<a	class="btn btn-default <c:if test="${pageContext.response.locale eq 'en'}">btn-primary</c:if>" 
            		href="?lang=en">EN</a>
			</div>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				<fmt:message key="label.error404" /> <br />
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>