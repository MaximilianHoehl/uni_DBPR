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
		
		<h2>Liste der Aufgaben</h2>
		<table>
			<tr>
				<th><h4>Aufgabe</h4></th>
				<th><h4>Meine Abhabe</h4></th>
				<th><h4>Bewertungsnote</h4></th>
			</tr>
			<tr>
				<td><P>Aufgabencontent</p></td>
				<td><P>Abgabencontent</p></td>
				<td><P>Bewertungscontent</p></td>
			</tr>
		</table>
		
    	<form  name="form_deleteCourse" action="/view_main" method="get">
		<input type="submit" value="Kurs löschen" />
		</form>
	<#else>
    	<form name="form_enrollToCourse" action="/new_enroll" method="get">
    	<input type="text" value="${title}" name="test" style="display: none;" />
		<input type="submit" value="Einschreiben" />
		</form>
	</#if>
	
</body>
</html>