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
  	<form action ="view_course" method="get">
  	<h4>Meine Kurse</h4>
  		<#list courses as course>
  			<div class="courseContainer">
  				
  				<input name="clickedCourseID" value="${course.id}" style="visibility: hidden; display: none;"></input>
  				<input id="CourseFieldset" action="test" style="background-color: Transparent; font-size: 20px; background-repeat: no-repeat; border: none; overflow: hidden;" value="${course.title}" name="clickedCourseName" type="submit"></input>
  				
  				<p>Ersteller: ${course.creatorName}</p>
  				<p>Freie Plätze: ${course.capacity}</p>
  			</div>
  		</#list>
  	</form>
  	<br>
	<form action ="view_course" method="get">
  	<h4>Verfügbare Kurse</h4>
  		<#list avCourses as avCourse>
  			<div class="courseContainer">
  				<input name="clickedCourseID" value="${avCourse.id}" style="visibility: hidden; display: none;"></input>
  				<input action="test" style="background-color: Transparent; font-size: 20px; background-repeat: no-repeat; border: none; overflow: hidden;" value="${avCourse.title}" name="clickedCourseName" type="submit"></input>
  				<p>Ersteller: ${avCourse.creatorName}</p>
  				<p>Freie Plätze: ${avCourse.capacity}</p>
  			</div>
  		</#list>
  	<br/>
  	<br/>
	</form>
  	
  	<form action="new_course" method="get">
  		<input type="submit" value="Neuen Kurs erstellen" />
  	</form>
  		
</body>
</html>