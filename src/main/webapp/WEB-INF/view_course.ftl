<html>
<head>
	<title>View Course</title>
</head>
 
<body>

	<h2>view_course</h2>
	<h1>Informationen</h1>
	<br>
	<h2>${title}</h2>
	<br>
	<h4>Ersteller: ${creator}</h4>
	<br>
	<h4>${description}</h4>
	<br>
	<h2>Anz. freier Plätze: ${capacity}</h2>
	<br>
	<#if navtype == "enrolled">
    	<form  name="form_deleteCourse" action="/view_main" method="get">
		<input type="submit" value="Kurs löschen" />
		</form>
	<#else>
    	<form name="form_enrollToCourse" action="/view_main" method="get">
		<input type="submit" value="Einschreiben" />
		</form>
	</#if>
	
</body>
</html>