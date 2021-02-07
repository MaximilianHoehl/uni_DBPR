package de.unidue.inf.is;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.utils.Backpack;

public class EnrollCourseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	Course selectedCourse;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		
		
		
		//-------------------------------------
		ArrayList<String> mainPageCourseTitles = Backpack.getCourseTitles();
		ArrayList<Integer> mainPageCourseKIDs = Backpack.getCourseIDs();
		//-------------------------------------
		
		String selectedCourseName = request.getParameter("test");
		System.out.println("Clicked Course Name: " + selectedCourseName);
		
		//-------------------------------------
		int i = mainPageCourseTitles.indexOf(selectedCourseName);
		int backpackedKid = mainPageCourseKIDs.get(i);
		//-------------------------------------
				
				
		
		//Init CourseStore
		CourseStore cs = new CourseStore();
		try {
			
			
			selectedCourse = cs.getCourseByID(backpackedKid);
			//selectedCourse = cs.getCourseByName(selectedCourseName);
			cs.complete();
			cs.close();
			
			//Check existence of course key and manipulate UI
			if((selectedCourse.getKey() == null) | (selectedCourse.getKey().equals(""))) {
				request.setAttribute("display", "none;");
			}else {
				request.setAttribute("display", "block;");
			}
			System.out.println("TITLE: " + selectedCourse.getTitle());
			System.out.println("Key of clicked course: " + selectedCourse.getKey());
			
			//Check if course is full
			if(selectedCourse.getCapacity() <= 0) {	//Will never evaluate to true since those courses are not shown up and there are no other users in showcase
				request.setAttribute("message", "Der Kurs ist leider voll belegt.");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "/"); //this navigates to view_main
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        		return;
			}
			
			request.setAttribute("name", selectedCourse.getTitle());
		} catch (IOException e) {
			
			e.printStackTrace();
			request.setAttribute("message", "Servererror");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "view_main");
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    		return;
		}
		
		
    	request.getRequestDispatcher("new_enroll.ftl").forward(request, response);
		 
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	//Check if course has key. If so: evaluate key
    	if(selectedCourse.getKey() != null) {
    		String keyInput = request.getParameter("key");
    		Boolean keyCorrect = selectedCourse.evaluateKey(keyInput);
    		System.out.println("Evaluate KEY: " + selectedCourse.evaluateKey(keyInput));
    		if(!keyCorrect) {
    			request.setAttribute("message", "Invalid key! No access.");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "view_main");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        		return;
    		}
    	}
    	System.out.println("Authorization passed!");
    	
    	//Setup DB connection
    	CourseStore cs = new CourseStore();
    	//Enroll user in course
    	cs.enrollUserInCourse(User.getCurrentUserId(), selectedCourse.getId());
    	//Capacity - 1
    	cs.setCapacity(selectedCourse.getId(), selectedCourse.getCapacity()-1);
    	cs.complete();
    	cs.close();
    	
    	//Backpack.pack(selectedCourse.getId());
    	
		
    	request.setAttribute("message", "Einschreiben erfolgreich!");
		request.setAttribute("color", "color: green;");
		request.setAttribute("targetAction", "view_main");//!!Transfer ID if change targetAction to view_corse!!
		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    }

}
