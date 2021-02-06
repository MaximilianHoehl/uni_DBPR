package de.unidue.inf.is;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Task;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.stores.TaskStore;


public final class NewAssignmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    
	int selectedTaskID;
	
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	TaskStore ts = new TaskStore();
    	CourseStore cs = new CourseStore();
    	
    	System.out.println("New Assignment Servlet triggered. TaskID: " + request.getParameter("clickedTaskText").split("-")[0]);
    	selectedTaskID = Integer.valueOf(request.getParameter("clickedTaskText").split("-")[0]);
    	
    	Task selectedTask = ts.getTaskById(selectedTaskID);
    	Course courseOfSelectedTask = cs.getCourseByID(selectedTask.getCourseID());
    	
    	
    	ts.complete();
    	cs.complete();
    	ts.close();
    	cs.close();
    	//ADD TO PAGE
    	
    	request.setAttribute("selectedTaskID", selectedTaskID);
    	request.setAttribute("courseName", courseOfSelectedTask.getTitle());
    	request.setAttribute("taskName", selectedTask.getName());
    	request.setAttribute("taskDesc", selectedTask.getDescription());
    	
    	
    	request.getRequestDispatcher("/new_assignment.ftl").forward(request, response);
    	
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	TaskStore ts = new TaskStore();
    	//CourseStore cs = new CourseStore();
    	
    	Boolean userHasSubmission = ts.userHasSubmission(User.getCurrentUserId(), selectedTaskID);
    	if(userHasSubmission) {
    		request.setAttribute("message", "Du hast diese Aufgabe bereits bearbeitet!");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "/"); //this navigates to view_main
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    		ts.complete();
        	ts.close();
    		return;
    	}
    	Task selectedTask = ts.getTaskById(selectedTaskID);
    	String submissionText = request.getParameter("submissionText");
    	try {
    		ts.addUserSubmission(submissionText, selectedTaskID, User.getCurrentUserId(), selectedTask.getCourseID());
    		ts.complete();
        	ts.close();
    	}catch(Exception e) {
    		ts.close();
    		e.printStackTrace();
    	}
    	
    	request.setAttribute("message", "Abgabe erfolgreich eingereicht!");
		request.setAttribute("color", "color: green;");
		request.setAttribute("targetAction", "/"); //this navigates to view_main
		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
    	
    }
}