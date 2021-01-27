package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import de.unidue.inf.is.utils.*;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.*;

public final class EntryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	ArrayList<Course> avCourses = new ArrayList<>();
    	
    	//LOGIN - fetch userData
    	UserStore userStore = new UserStore();
    	User me = userStore.getUserById(2);
    	userStore.close();
    	String myName = me.getUserName();
    	request.setAttribute("username", myName);
    	
    	//Initialize - fetch userCourses
    	CourseStore courseStore = new CourseStore();
    	ArrayList<Course> myCourses = courseStore.getCoursesByUID(me.getUserId());
    	
    	request.setAttribute("courses", myCourses);
    	request.setAttribute("avCourses", avCourses);
    	
    	request.getRequestDispatcher("view_main.ftl").forward(request, response);
    	
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String action = request.getAttribute("action") == null ? "entry" : (String) request.getAttribute("action");
    	
    }
}