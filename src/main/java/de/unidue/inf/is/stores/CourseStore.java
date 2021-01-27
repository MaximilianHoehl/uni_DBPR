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
	
    public ArrayList<Course> getAvailableCourses(){
    	
    	try {
    		
    		ArrayList<Course> res = new ArrayList<Course>();
    		String query = "SELECT * FROM dbp079.kurs WHERE kid IN"
    				+ " (SELECT DISTINCT k.kid FROM dbp079.kurs k"
    				+ " JOIN dbp079.einschreiben e ON (k.kid=e.kid)"
    				+ " WHERE k.freieplaetze > 0"
    				+ " AND e.bnummer<>" + String.valueOf(User.getCurrentUserId()) + ")";
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
    
    public void addCourse(Course courseToAdd) throws StoreException {	//add a user to instance (add to commit)
        try {
            PreparedStatement preparedStatement = connection
                            .prepareStatement("insert into dbp079.kurs (name, beschreibungstext, einschreibeschluessel, freieplaetze, ersteller) values (?, ?, ?, ?, ?)");
            
            preparedStatement.setString(1, courseToAdd.getTitle());
            preparedStatement.setString(2, courseToAdd.getDescription());
            preparedStatement.setString(3,  courseToAdd.getKey());
            preparedStatement.setShort(4,  (short)courseToAdd.getCapacity());
            preparedStatement.setShort(5,  (short)courseToAdd.getCreator());
            
            preparedStatement.executeUpdate();
            
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
	
    public void complete() {
        complete = true;
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
