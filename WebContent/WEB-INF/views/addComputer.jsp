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
					<h1>Add Computer</h1>
					<form
						action="AddComputer?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
						method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label> <input
									type="text" class="form-control" id="computerName"
									name="computerName" placeholder="Computer name" value="${computerName}">
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced"
									name="introduced" placeholder="Introduced date" value="${introduced}">
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinued" placeholder="Discontinued date" value="${discontinued}">
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="companyId">
									<option value="">-</option>
									<c:forEach var="company" items="${listCompanies}">
										<option 
											value="${company.id}"
											<c:if test="${company.id == companyId}"> selected="selected"</c:if>>
										>
											${company.name}
										</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" value="Add" class="btn btn-primary">
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

/*		// Get the modal
		var modal = document.getElementById('myModal');
		modal.style.display = "block";

		// When the user clicks on <span> (x), close the modal
		var span = document.getElementsByClassName("close")[0];
		span.onclick = function() {
			modal.style.display = "none";
		}

		// When the user clicks anywhere outside of the modal, close it
		window.onclick = function(event) {
			if (event.target == modal) {
				modal.style.display = "none";
			}
		}*/
	</script>

</body>
</html>