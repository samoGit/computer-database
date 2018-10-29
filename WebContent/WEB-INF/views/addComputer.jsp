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

<style>
	#errorMessage {
		background-color: #ccc;
		color: red;
		font-weight: bold;
		padding: 10px;
		display: none;
	}
</style>


</head>

<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand"
				href="Dashboard?pageNumber=1&nbComputersByPage=${nbComputersByPage}">
				<fmt:message key="label.subTitle" />
			</a>
            <div class="pull-right btnLang" style="margin-top: 7.5px;">
            	<a class="btn btn-default <c:if test="${lang eq 'fr'}">btn-primary</c:if>" href="?lang=fr&pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}">FR</a>
            	<a class="btn btn-default <c:if test="${lang eq 'en'}">btn-primary</c:if>" href="?lang=en&pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}">EN</a>
			</div>			
		</div>
	</header>

	<section id="main">
		<div class="container">
		
			<c:if test="${not empty errorMsg}">
				<div class="row">
					<div class="col-xs-8 col-xs-offset-2 box">
						<div id="errorMessage">
						${errorMsg}
						</div>
					</div>
				</div>
			</c:if>

			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1><fmt:message key="label.addComputer" /></h1>
					<form
						action="AddComputer?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
						method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName"><fmt:message key="label.computerName" /></label> <input
									type="text" class="form-control" id="computerName" 
									name="computerName" placeholder="Computer name" value="${computerName}" required>
							</div>
							<div class="form-group">
								<label for="dateIntroduced"><fmt:message key="label.introducedDate" /></label> <input
									type="date" class="form-control" id="dateIntroduced"
									name="dateIntroduced" placeholder="Introduced date" value="${dateIntroduced}">
							</div>
							<div class="form-group">
								<label for="dateDiscontinued"><fmt:message key="label.discontinuedDate" /></label> <input
									type="date" class="form-control" id="dateDiscontinued"
									name="dateDiscontinued" placeholder="Discontinued date" value="${dateDiscontinued}">
							</div>
							<div class="form-group">
								<label for="companyId"><fmt:message key="label.company" /></label> <select
									class="form-control" id="companyId" name="companyId">
									<option value="">-</option>
									<c:forEach var="company" items="${listCompanies}">
										<option 
											value="${company.id}"
											<c:if test="${company.id == companyId}"> selected="selected"</c:if>>
											${company.name}
										</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value='<fmt:message key="label.add" />' class="btn btn-primary">
							or <a
								href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
								class="btn btn-default"><fmt:message key="label.cancel" /></a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script>
		$(function() {
			$("#errorMessage").animate({
				left: "+=50",
				height: "toggle"
			});
		});
	</script>

</body>
</html>