package de.unidue.inf.is;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;



/**
 * Das könnte die Eintrittsseite sein.
 */
public final class OnlineLearnerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    //START LAB
    private static List<User> userList = new ArrayList<>();

    // Just prepare static data to display on screen
    /*static {
        userList.add(new User("Bill", "Gates", "spin-it-baby@hooooootmail.cum"));
        userList.add(new User("Steve", "Jobs", "spin-it-baby@hooooootmail.cum"));
        userList.add(new User("Larry", "Page", "spin-it-baby@hooooootmail.cum"));
        userList.add(new User("Sergey", "Brin", "spin-it-baby@hooooootmail.cum"));
        userList.add(new User("Larry", "Ellison", "spin-it-baby@hooooootmail.cum"));
    }*/
    //END LAB


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
  
        boolean databaseExists = DBUtil.checkDatabaseExistsExternal();
        
        
        if (!databaseExists) {
            request.setAttribute("db2exists", "nicht vorhanden :-(");
            request.getRequestDispatcher("onlineLearner_start.ftl").forward(request, response);
            return;
        }
        request.setAttribute("db2exists", "vorhanden! Supi!");
        request.setAttribute("users", userList);
        request.getRequestDispatcher("index.ftl").forward(request, response);
    }

}