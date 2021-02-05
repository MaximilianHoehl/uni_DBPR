
package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Submission;
import de.unidue.inf.is.domain.Task;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.stores.TaskStore;

public class ViewCourseServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	Course selectedCourse;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("CourseID: " + request.getParameter("clickedCourseID"));
		String clickedCourseName = request.getParameter("clickedCourseName");
		CourseStore cs = new CourseStore();
		//Try get course by name
		try {
			selectedCourse = cs.getCourseByName(clickedCourseName);
			
		}catch(Exception e) {
			request.setAttribute("message", "Serverinternal Error - Course not found :/");
    		request.setAttribute("color", "color: red;");
    		request.setAttribute("targetAction", "/"); //this navigates to view_main
    		request.getRequestDispatcher("view_dialogue.ftl").forward(request, response);
			e.printStackTrace();
		}
		
		//Check if already signed in to set navtype
		if(cs.checkIfUserEnrolledByID(selectedCourse.getId(), User.getCurrentUserId())) {	
			request.setAttribute("navtype", "enrolled");
			System.out.println("SELECTED ENROLLED COURSE");
			
			//Fetch Tasks from course
			TaskStore ts = new TaskStore();
			ArrayList<Task> fetchedTasks = ts.getTasksFromCourse(selectedCourse.getId());
			request.setAttribute("tasks", fetchedTasks);
			ArrayList<Submission> submissions = new ArrayList<Submission>();
			for(Task task : fetchedTasks) {
				Submission sb = ts.getUserSubmission(User.getCurrentUserId(), task.getId());
				if(sb != null) {
					submissions.add(sb);
				}else {
					submissions.add(new Submission());
				}
			}
			request.setAttribute("submissions", submissions);
			ts.complete();
			ts.close();	
		}else {
			request.setAttribute("navtype", "notEnrolled");
		}
		cs.complete();
		cs.close();
		//Setup courseinformation in UI
		request.setAttribute("title", selectedCourse.getTitle());
		request.setAttribute("creator", selectedCourse.getCreatorName());
		request.setAttribute("description", selectedCourse.getDescription());
		request.setAttribute("capacity", selectedCourse.getCapacity());
		
    	request.getRequestDispatcher("view_course.ftl").forward(request, response);
		 
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	request.getRequestDispatcher("view_course.ftl").forward(request, response);
    }

}