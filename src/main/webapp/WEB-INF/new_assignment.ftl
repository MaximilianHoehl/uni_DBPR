<html>
<head>
	<title>Abgabe einreichen</title>
</head>
 
<body>
<form name="taskSubmission" action="/new_assignment" method="post" style="inline-block">	
	<h2>Kurs: <h2>
	<h4 style="display: inline;">${courseName}</h4>
	<h2>Aufgabe: <h2>
	<h4 style="display: inline;">${taskName}</h4>
	<h2>Beschreibung: <h2>
	<h4 style="display: inline;">${taskDesc}</h4>
	<h2>Abgabetext: <h2>
	<textarea rows="10" cols="100" name="submissionText" style="display: inline-block;"></textarea><br/>
		
	<input type="submit" value="Einreichen" />
</form>
</body>
</html>