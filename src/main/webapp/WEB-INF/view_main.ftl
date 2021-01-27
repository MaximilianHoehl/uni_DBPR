<html>
<head>
	<title>Online Learner</title>
	<style>
		.courseContainer{
			border-style: solid;
			border-width: 5px;
			display: inline-block;
			border-radius: 5px;
			padding: 5px;
			margin: 5px;
		}
	</style>	
</head>
 
<body>

	<h2>Wilkommen, ${username}!</h2>
  	<br>
  	
  	<h4>Meine Kurse</h4>
  		<#list courses as course>
  			<div class="courseContainer">
  				<a href="/view_course">${course.title}</a>
  				<p>Ersteller: ${course.creator}</p>
  				<p>Freie Plätze: ${course.capacity}</p>
  			</div>
  		</#list>
  	<br>
  	
  	<h4>Verfügbare Kurse</h4>
  		<#list avCourses as avCourse>
  			<div class="courseContainer">
  				<a href="http://localhost:9079/onlineLearner">${avCourse.title}</a>
  				<p>Ersteller: ${avCourse.creator}</p>
  				<p>Freie Plätze: ${avCourse.capacity}</p>
  			</div>
  		</#list>
  	<br>
  	
  	<form action="new_course" method="get">
  		<input type="submit" value="Erstellen" />
  	</form>
  		
</body>
</html>