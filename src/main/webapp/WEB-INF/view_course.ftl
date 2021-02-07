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
		
    	<form  name="form_deleteCourse" action="/view_course?action=deleteCourse" method="POST">
		<input type="submit" value="Kurs löschen" />
		</form>
		
	<form  name="form_submission" action="/new_assignment" method="get">
		<h2>Liste der Aufgaben</h2>
		<table>
			<tr>
				<th><h4>Aufgabe</h4></th>
				<th><h4>Meine Abgabe</h4></th>
				<th><h4>Bewertun gsnote</h4></th>
			</tr>
			<tr>
				<td>
					<#list tasks as task>
						<input action="test" style="background-color: Transparent; display: block; margin: 18px; border: none;" value="${task.id}-${task.name}" name="clickedTaskText" type="submit"></input>
						
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
					</#list>
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