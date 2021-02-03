<html>
<head>
	<title>View Course</title>
</head>
 
<body>

	<h1>Informationen</h1>
	<h2>${title}</h2>
	<h4>Ersteller: ${creator}</h4>
	<h4>${description}</h4>
	<h2>Anz. freier Plätze: ${capacity}</h2>
	<br>
	<#if navtype == "enrolled">
			
    	<form  name="form_deleteCourse" action="/view_main" method="get">
		<input type="submit" value="Kurs löschen" />
		
		<h2>Liste der Aufgaben</h2>
		<table>
			<tr>
				<th><h4>Aufgabe</h4></th>
				<th><h4>Meine Abhabe</h4></th>
				<th><h4>Bewertungsnote</h4></th>
			</tr>
			<tr>
				<td>
					<#list tasks as task>
						<p>${task.name}</p>
					</#list>
				</td>
				<td>
					<#list submissions as sub>
						<p>${sub.text}</p>
					</#list>
				</td>
				<td>
					<#list submissions as sub>
						<p>${sub.avgMarkText}</p>
					</#list >
				</td>
			</tr>
		</table>

		</form>
	<#else>
    	<form name="form_enrollToCourse" action="/new_enroll" method="get">
    	<input type="text" value="${title}" name="test" style="display: none;" />
		<input type="submit" value="Einschreiben" />
		</form>
	</#if>
	
</body>
</html>