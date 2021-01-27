package de.unidue.inf.is;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.UserStore;



/**
 * Einfaches Beispiel, das die Verwendung des {@link UserStore}s zeigt.
 */
public final class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
    IOException {
    	System.out.println("Correct Scpoe");
        // mach was
        //User userToAdd = new User("Helikopter", "Mann", "spin-it-baby@hooooootmail.cum");

        try (UserStore userStore = new UserStore()) {
            //userStore.getUsers();
        	//userStore.addUser(userToAdd);
            // userStore.somethingElse();
            userStore.complete();
            
        }

        // mach noch mehr
        System.out.println("Went well");
    }

}
