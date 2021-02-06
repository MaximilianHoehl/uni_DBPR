package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.*;

public final class EntryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	//LOGIN - fetch userData
    	UserStore userStore = new UserStore();
    	User me = userStore.getUserById(6);
    	userStore.complete();
    	userStore.close();
    	String myName = me.getUserName();
    	User.setCurrentUser(me.getUserId());
    	request.setAttribute("username", myName);
    	
    	//Initialize - fetch userCourses
    	CourseStore courseStore = new CourseStore();
    	ArrayList<Course> myCourses = courseStore.getCoursesByUID(me.getUserId());
    	
    	//Initialize - fetch availableCourses
    	ArrayList<Course> avCourses = courseStore.getAvailableCourses();
    	courseStore.complete();
    	courseStore.close();
    	
    	request.setAttribute("courses", myCourses);
    	request.setAttribute("avCourses", avCourses);
    	
    	request.getRequestDispatcher("view_main.ftl").forward(request, response);
    	
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	
    }
}