<html>
<head>
	<title>New Course</title>
</head>
 
<body>

	<h4>Kurs erstellen</h4>
	
	<form name="form_new_course" action="/new_course" method="post">
		Name  <input type="text" name="name"><br/>
		Einschreibeschlüssel  <input type="text" name="key"><br/>
		Anzahl freier Plätze  <input type="number" name="capacity"><br/>
		Beschreibungstext  <textarea rows="10" cols="100" name="description"></textarea><br/>
		<input type="submit" value="Erstellen" />
	</form>

</body>
</html>