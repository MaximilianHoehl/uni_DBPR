package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.db2.jcc.am.SqlException;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.*;

public class NewCourseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	 @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	System.out.println("arrivedOn_NewCourseServlet_GET");
    	
    	
    	
    	request.getRequestDispatcher("new_course.ftl").forward(request, response);
		 
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	//Validate input
    	try {
    		if(request.getParameter("name").length()>50) {	//Check if nameLength is greater than 50
        		request.setAttribute("message", "Dein Name darf nicht mehr als 50 Zeichen enthalten!");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "new_course");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        	}
        	if(request.getParameter("name").length()==0) {
        		request.setAttribute("message", "Dein Name muss mindestens ein Zeichen enthalten!");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "new_course");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        	}
        	if(!(request.getParameter("capacity").matches("-?(0|[1-9]\\d*)"))) {
        		request.setAttribute("message", "Die Eingabe für 'Freie Plätze' muss eine ganzzahl sein!");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "new_course");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        	}
        	if(Integer.valueOf(request.getParameter("capacity"))>100) {
        		request.setAttribute("message", "Dein Kurs darf maximal 100 Plätze anbieten!");
        		request.setAttribute("color", "color: red;");
        		request.setAttribute("targetAction", "new_course");
        		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
        	}
    		
    	}catch(NumberFormatException e) {
    		
    		e.printStackTrace();
    		request.setAttribute("message", "Servererror");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "view_main");
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    	}
    	
    	//Create CourseObject from inputData (This point will only be reached if request passes the validation)
    	Course courseToAdd = new Course(
    			(String)request.getParameter("name"), 
    			(String)request.getParameter("description"),
    			(String)request.getParameter("key"),
    			Short.valueOf(request.getParameter("capacity")),
    			User.getCurrentUserId()
    			);  	
    	
    	//pass created courseObject to DBAdapter
    	try {
    		CourseStore courseStore = new CourseStore();
        	courseStore.addCourse(courseToAdd);
        	courseStore.complete();
        	courseStore.close();
    		
    	}catch(StoreException e) {
    		request.setAttribute("message", "Servererror :(");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "view_main");
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    		e.printStackTrace();
    	}
    	
    	request.setAttribute("message", "Dein Kurs wurde erstellt! <br/> Du findest ihn in der Sektion 'Meine Kurse'");
		request.setAttribute("color", "color: green;");
		request.setAttribute("targetAction", "view_main");
    	request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    }

}
