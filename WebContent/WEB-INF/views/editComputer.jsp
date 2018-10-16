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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand"
				href="Dashboard?pageNumber=1&nbComputersByPage=${nbComputersByPage}">
				Application - Computer Database </a>
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
					<div class="label label-default pull-right">id: ${computerId}</div>
					<h1>Edit Computer  dateIntroduced=${dateIntroduced}</h1>

					<form
						action="EditComputer?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
						method="POST">
						<input type="hidden" value="${computerId}" id="computerId" name="computerId" />

						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" id="computerName" name="computerName" 
									value="${computerName}" required>
							</div>
							<div class="form-group">
								<label for="dateIntroduced">Introduced date</label> <input
									type="date" class="form-control" id="dateIntroduced" name="dateIntroduced" 
									value="${dateIntroduced}">
							</div>
							<div class="form-group">
								<label for="dateDiscontinued">Discontinued date</label> <input
									type="date" class="form-control" id="dateDiscontinued" name="dateDiscontinued" 
									value="${dateDiscontinued}">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId"  name="companyId">
									<option value="">-</option>
									<c:forEach var="company" items="${listCompanies}">
										<option value="${company.id}" 
											<c:if test="${company.name == companyName}"> selected="selected"</c:if>>
											${company.name}</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Edit" class="btn btn-primary">
							or <a
								href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
								class="btn btn-default">Cancel</a>
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