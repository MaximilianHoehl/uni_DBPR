package de.unidue.inf.is;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.utils.HTMLUtil;

public class EnrollCourseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	Course clickedCourse;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		String clickedCourseName = request.getParameter("clickedCourseName");
		System.out.println("Clicked Course Name: " + clickedCourseName);
		
		//Init CourseStore
		CourseStore cs = new CourseStore();
		try {
			
			//Course clickedCourse = cs.getCourseByID(clickedCourseID);
			clickedCourse = cs.getCourseByName(clickedCourseName);
			cs.complete();
			cs.close();
			
			//Check existence of course key and manipulate UI
			System.out.println("TITLE: " + clickedCourse.getTitle());
			System.out.println("Key of clicked course: " + clickedCourse.getKey());
			if(clickedCourse.getKey()!=null) {
				//request.setAttribute("value", "");
				request.setAttribute("display", "block;");
			}else {
				request.setAttribute("display", "none;");
				//request.setAttribute("value", "NPWR");
			}
			
			//Check if course is full
			if(clickedCourse.getCapacity() <= 0) {	//Will never evaluate to true since those courses are not shown up and there are no other users in showcase
				request.setAttribute("message", "Der Kurs ist leider voll belegt.");
        		request.setAttribute("color", "color: yellow;");
        		request.setAttribute("targetAction", "/"); //this navigates to view_main
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
			}
			//Check if already signed in
			if(clickedCourse.getCreatorId() == User.getCurrentUserId()) {	//Will never evaluate since your own courses are directly linked to the view_course page
				request.setAttribute("message", "Du bist bereits in diesen Kurs einschrieben! Klicke auf OK um zur Kursseite zu gelangen.");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "/view_course"); //this navigates to view_main
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
			}
			
			
			
			request.setAttribute("name", clickedCourse.getTitle());
		} catch (SQLException | IOException e) {
			
			e.printStackTrace();
			request.setAttribute("message", "Servererror");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "view_main");
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
		}
		
		
    	request.getRequestDispatcher("new_enroll.ftl").forward(request, response);
		 
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	if(clickedCourse.getKey() != null) {
    		String keyInput = request.getParameter("key");
    		Boolean keyCorrect = clickedCourse.evaluateKey(keyInput);
    		System.out.println("Evaluate KEY: " + clickedCourse.evaluateKey(keyInput));
    		if(!keyCorrect) {
    			request.setAttribute("message", "Invalid key! No access.");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "view_main");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    		}
    	}
    	System.out.println("Authorization passed!");
    	
    	//Setup DB connection
    	CourseStore cs = new CourseStore();
    	//Enroll user in course
    	cs.enrollUserInCourse(User.getCurrentUserId(), clickedCourse.getId());
    	//Capacity - 1
    	cs.setCapacity(clickedCourse.getId(), clickedCourse.getCapacity()-1);
    	cs.complete();
    	cs.close();
		
    	request.setAttribute("name", clickedCourse.getTitle());
    	request.getRequestDispatcher("view_course.ftl").forward(request, response);
    }

}
