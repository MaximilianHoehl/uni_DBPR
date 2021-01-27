package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.sql.*;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;
import de.unidue.inf.is.domain.*;


public class CourseStore implements Closeable {

    private Connection connection;
    private boolean complete;

    public CourseStore() throws StoreException {	//create instance and open DB connection
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
            System.out.println("Setted up ext connection: " + connection);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    public ArrayList<Course> getCoursesByUID(int userId) {
    	
    	try {
    		
    		ArrayList<Course> res = new ArrayList<Course>();
    		String query = "SELECT k.kid, k.name, k.beschreibungstext, k.einschreibeschluessel, k.freieplaetze, k.ersteller"
    				+ " FROM dbp079.einschreiben e"
    				+ " JOIN dbp079.kurs k ON (e.kid = k.kid)"
    				+ " WHERE e.bnummer=" + userId;
    		PreparedStatement pst = connection.prepareStatement(query);
    		ResultSet resultSet = pst.executeQuery();
    		
    		while(resultSet.next()) {
    			res.add(new Course(
    					resultSet.getShort(1), 		//KID
    					resultSet.getString(2), 	//Name
    					resultSet.getString(3), 	//description
    					resultSet.getString(4), 	//key
    					resultSet.getShort(5), 		//capacity
    					resultSet.getShort(6)));	//creator(id)
    		}
    		
    		return res;
    		
    	}catch(SQLException e) {
    		
    		e.printStackTrace();
    		return null;
    	}
    }
	
	
	@Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                throw new StoreException(e);
            }
            finally {
                try {
                    connection.close();
                    System.out.println("Closed userStore instance");
                }
                catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}
