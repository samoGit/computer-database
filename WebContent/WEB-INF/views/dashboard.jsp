<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
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
			<h1 id="homeTitle">${nbComputers} computers found</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" value="${search}" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer"
						href="AddComputer?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}">Add
						Computer</a> <a class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">Edit</a>
				</div>
			</div>
		</div>

		<form id="deleteForm"
			action="DeleteComputerServelet?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}"
			method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><a href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=Name"
								<c:if test="${orderBy eq 'Name'}"> style="text-decoration: underline"</c:if>>
								Computer name
						</a></th>
						<th><a href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=Introduced"
								<c:if test="${orderBy eq 'Introduced'}"> style="text-decoration: underline"</c:if>>
								Introduced date
						</a></th>
						<th><a href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=Discontinued"
								<c:if test="${orderBy eq 'Discontinued'}"> style="text-decoration: underline"</c:if>>
								Discontinued date
						</a></th>
						<th><a href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=Company"
								<c:if test="${orderBy eq 'Company'}">style="text-decoration: underline;"</c:if> >
								Company
						</a></th>
					</tr>
				</thead>

				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="computer" items="${listComputerDtos}">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${computer.id}"></td>
							<td><a
								href="EditComputer?computerId=${computer.id}
									&computerName=${computer.name}
									&dateDiscontinued=${computer.dateIntroduced}
									&dateIntroduced=${computer.dateDiscontinued}
									&companyName=${computer.companyName}
									&pageNumber=${pageNumber}
									&nbComputersByPage=${nbComputersByPage}"
								onclick=""> <c:out value="${computer.name}" />
							</a></td>
							<td><c:out value="${computer.dateIntroduced}" /></td>
							<td><c:out value="${computer.dateDiscontinued}" /></td>
							<td><c:out value="${computer.companyName}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<li><a
					href="Dashboard?pageNumber=${pageNumber-1}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>

				<c:if test="${pageNumber>3}">
					<li><a
						href="Dashboard?pageNumber=1&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">1</a></li>
					<li><a href="#">...</a></li>
				</c:if>

				<c:if test="${pageNumber>2}">
					<li><a
						href="Dashboard?pageNumber=${pageNumber-2}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">${pageNumber-2}</a></li>
				</c:if>
				<c:if test="${pageNumber>1}">
					<li><a
						href="Dashboard?pageNumber=${pageNumber-1}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">${pageNumber-1}</a></li>
				</c:if>
				<li><a
					href="Dashboard?pageNumber=${pageNumber}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}"><b>${pageNumber}</b></a></li>
				<c:if test="${pageNumber<=nbPage-1}">
					<li><a
						href="Dashboard?pageNumber=${pageNumber+1}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">${pageNumber+1}</a></li>
				</c:if>
				<c:if test="${pageNumber<=nbPage-2}">
					<li><a
						href="Dashboard?pageNumber=${pageNumber+2}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">${pageNumber+2}</a></li>
				</c:if>

				<c:if test="${pageNumber<nbPage-2}">
					<li><a href="#">...</a></li>
					<li><a
						href="Dashboard?pageNumber=${nbPage}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}">${nbPage}</a></li>
				</c:if>

				<li><a
					href="Dashboard?pageNumber=${pageNumber+1}&nbComputersByPage=${nbComputersByPage}&search=${search}&orderBy=${orderBy}"
					aria-label="Next"> <span aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<button type="button"
					class="btn btn-default <c:if test="${nbComputersByPage == 10}">btn-primary</c:if>"
					onclick="location.href='Dashboard?pageNumber=1&nbComputersByPage=10&search=${search}&orderBy=${orderBy}'">10
				</button>
				<button type="button"
					class="btn btn-default <c:if test="${nbComputersByPage == 50}">btn-primary</c:if>"
					onclick="location.href='Dashboard?pageNumber=1&nbComputersByPage=50&search=${search}&orderBy=${orderBy}'">50
				</button>
				<button type="button"
					class="btn btn-default <c:if test="${nbComputersByPage == 100}">btn-primary</c:if>"
					onclick="location.href='Dashboard?pageNumber=1&nbComputersByPage=100&search=${search}&orderBy=${orderBy}'">100
				</button>
			</div>
		</div>
	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>