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
    
    public Course getCourseByID(int id) throws SQLException, IOException {
    	
    	Course course;
    	String query = "SELECT * FROM dbp079.kurs k WHERE k.kid = ?";
    	PreparedStatement pts = connection.prepareStatement(query);
    	pts.setInt(1, id);
    	ResultSet resultSet = pts.executeQuery();
    	while(resultSet.next()) {
    		course = new Course(
    				resultSet.getShort("KID"), 		//KID
					resultSet.getString("NAME"), 	//Name
					resultSet.getString("BESCHREIBUNGSTEXT"), 	//description
					resultSet.getString("EINSCHREIBESCHLUESSEL"), 	//key
					resultSet.getShort("FREIEPLAETZE"), 		//capacity
					resultSet.getShort("ERSTELLER"));		//creator(id)	
    		return course;
    	}
    	
    	System.out.println("CourseStore: getCourseByID: Nothing found!");
    	return null;
    }

    public Course getCourseByName(String name) throws SQLException, IOException {
    	
    	Course course;
    	String query = "SELECT * FROM dbp079.kurs k WHERE k.name = ?";
    	PreparedStatement pts = connection.prepareStatement(query);
    	pts.setString(1, name);
    	ResultSet resultSet = pts.executeQuery();
    	while(resultSet.next()) {
    		course = new Course(
    				resultSet.getShort("KID"), 		//KID
					resultSet.getString("NAME"), 	//Name
					resultSet.getString("BESCHREIBUNGSTEXT"), 	//description
					resultSet.getString("EINSCHREIBESCHLUESSEL"), 	//key
					resultSet.getShort("FREIEPLAETZE"), 		//capacity
					resultSet.getShort("ERSTELLER"));		//creator(id)	
    		return course;
    	}
    	
    	System.out.println("CourseStore: getCourseByName: Nothing found!");
    	return null;
    }
    
    public ArrayList<Course> getCoursesByUID(int userId) throws IOException {
    	
    	try {
    		
    		ArrayList<Course> res = new ArrayList<Course>();
    		String query = "SELECT k.kid, k.name, k.beschreibungstext, k.einschreibeschluessel, k.freieplaetze, k.ersteller, b.name as nutzername"
    				+ " FROM dbp079.einschreiben e"
    				+ " JOIN dbp079.kurs k ON (e.kid = k.kid)"
    				+ " JOIN dbp079.benutzer b ON (k.ersteller = b.bnummer)"
    				+ " WHERE e.bnummer = ?";
    		PreparedStatement pst = connection.prepareStatement(query);
    		pst.setInt(1, userId);
    		ResultSet resultSet = pst.executeQuery();
    		
    		while(resultSet.next()) {
    			res.add(new Course(
    					resultSet.getShort("KID"), 		//KID
    					resultSet.getString("NAME"), 	//Name
    					resultSet.getString("BESCHREIBUNGSTEXT"), 	//description
    					resultSet.getString("EINSCHREIBESCHLUESSEL"), 	//key
    					resultSet.getShort("FREIEPLAETZE"), 		//capacity
    					resultSet.getShort("ERSTELLER"),		//creator(id)
    					resultSet.getString("NUTZERNAME")));	
    		}
    		
    		return res;
    		
    	}catch(SQLException e) {
    		
    		e.printStackTrace();
    		return null;
    	}
    }
	
    public ArrayList<Course> getAvailableCourses() throws IOException{
    	
    	try {
    		
    		ArrayList<Course> res = new ArrayList<Course>();
    		String query = "SELECT * FROM dbp079.kurs k"
    				+ " WHERE k.freieplaetze > 0"
    				+ " AND k.kid NOT IN (SELECT e.kid FROM dbp079.einschreiben e WHERE e.bnummer="
    				+ String.valueOf(User.getCurrentUserId()) + ")";
    		PreparedStatement pst = connection.prepareStatement(query);
    		ResultSet resultSet = pst.executeQuery();
    		
    		while(resultSet.next()) {
    			res.add(new Course(
    					resultSet.getShort("KID"), 						//KID
    					resultSet.getString("NAME"), 					//Name
    					resultSet.getString("BESCHREIBUNGSTEXT"), 		//description
    					resultSet.getString("EINSCHREIBESCHLUESSEL"), 	//key
    					resultSet.getShort("FREIEPLAETZE"), 			//capacity
    					resultSet.getShort(6),							//creatorId
    					""));						//creatorName resultSet.getString(9)
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
            preparedStatement.setShort(5,  (short)courseToAdd.getCreatorId());
            
            preparedStatement.executeUpdate();
            
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
    public void enrollUserInCourse(int userID, int courseID) throws StoreException {
    	
    	try {
    		String sql = "INSERT INTO dbp079.einschreiben (bnummer, kid) VALUES (?, ?)";
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1, userID);
    		ps.setInt(2, courseID);
    		
    		ps.executeUpdate();
    	}catch (SQLException e) {
    		
    		throw new StoreException(e);
    	}
    }
    
    public void setCapacity(int courseID, int newCapacity) {
    	
    	try {
    		String sql = "UPDATE dbp079.kurs SET freieplaetze = ? WHERE kid = ?";
    		PreparedStatement ps = connection.prepareStatement(sql);
    		ps.setInt(1, newCapacity);
    		ps.setInt(2, courseID);
    		
    		ps.executeUpdate();
    	} catch (SQLException e) {
    		
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
