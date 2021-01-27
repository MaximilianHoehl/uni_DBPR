package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    	
    	//Create CourseObject from inputData
    	Course courseToAdd = new Course(
    			(String)request.getParameter("name"), 
    			(String)request.getParameter("key"),
    			(String)request.getParameter("description"),
    			Short.valueOf(request.getParameter("capacity")),
    			User.getCurrentUserId()
    			);
    	
    	//Validate new courseObject
    	if(courseToAdd.getTitle().length()>50?true:false) {	//Check if nameLength is greater than 50
    		request.getRequestDispatcher("new_course.ftl").forward(request, response);
    	}
    	//pass created courseObject to DBAdapter
    	CourseStore courseStore = new CourseStore();
    	courseStore.addCourse(courseToAdd);
    	courseStore.complete();
    	courseStore.close();
    	
    	request.getRequestDispatcher("new_course.ftl").forward(request, response);
    }

}
