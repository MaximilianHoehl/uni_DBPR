package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.unidue.inf.is.domain.Submission;
import de.unidue.inf.is.domain.Task;
import de.unidue.inf.is.utils.DBUtil;

public class TaskStore implements Closeable{

	private Connection connection;
    private boolean complete;
    
	public TaskStore() throws StoreException {	//create instance and open DB connection
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
            System.out.println("Setted up ext connection: " + connection);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
	
	public ArrayList<Task> getTasksFromCourse(int courseID) {
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		String sql = "SELECT * FROM dbp079.aufgabe afg"	//JOIN EINREICHEN to get BNUMMER
				+ " WHERE afg.kid = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, courseID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				tasks.add(new Task(
					rs.getShort("ANUMMER"),
					rs.getShort("KID"),
					rs.getString("NAME"),
					rs.getString("BESCHREIBUNG")
					));
			}
			return tasks;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Task getTaskById(int id) {
		Task task = null;
		String sql = "SELECT * FROM dbp079.aufgabe afg"	//JOIN EINREICHEN to get BNUMMER
				+ " WHERE afg.anummer = ? ORDER BY afg.anummer ASC";
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				task = new Task(
					rs.getShort("ANUMMER"),
					rs.getShort("KID"),
					rs.getString("NAME"),
					rs.getString("BESCHREIBUNG")
					);
			}
			return task;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Submission getUserSubmission(int userID, int taskID) {
		
		Submission userSubmission = null;
		String sqlSubs = "SELECT * FROM dbp079.einreichen e"
				+ " JOIN dbp079.abgabe a ON (e.aid = a.aid)"
				+ " WHERE e.bnummer = ? AND e.anummer = ?";
		String sqlMarks = "SELECT AVG(Cast(note as Float)) FROM dbp079.bewerten WHERE aid=?";
		try {
			PreparedStatement ps = connection.prepareStatement(sqlSubs);
			ps.setInt(1, userID);
			ps.setInt(2, taskID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("FOUND USER SUBMISSION");
				userSubmission = new Submission(
					rs.getInt("AID"),
					rs.getString("ABGABETEXT")
					);
			}
			if(userSubmission != null) {
				PreparedStatement ps2 = connection.prepareStatement(sqlMarks);
				ps2.setInt(1, userSubmission.getId());
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()) {
					System.out.println("FETCHED USER SUBMISSION AVG MARK");
					userSubmission.setAvgMark(rs2.getFloat(1));
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return userSubmission;
	}
	
	public boolean userHasSubmission(int userID, int taskID) {
		
		String sqlSubs = "SELECT COUNT(*) FROM dbp079.einreichen e"
				+ " WHERE e.bnummer = ? AND e.anummer = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(sqlSubs);
			ps.setInt(1, userID);
			ps.setInt(2, taskID);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getInt(1)!=0) {
					System.out.println("-----MATCH BUT NOT NULL: " + userID + ", " + taskID + ": " + rs.getInt(1));
					return true;
				}else {
					System.out.println("-----NO MATCH AND NULL");
					return false;
				}
			}
			System.out.println("-----NO MATCH");
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public void addUserSubmission(String submission, int taskID, int userID, int courseID) throws SQLException {
		
		String sql = "INSERT INTO dbp079.abgabe (abgabetext) values ?";
		String sqlQueryID ="SELECT aid from dbp079.abgabe order by aid desc fetch first 1 rows only";
		String sql2 = "INSERT INTO dbp079.einreichen (bnummer, kid, anummer, aid)"
				+ " VALUES (?, ?, ?, ?)";
		
		
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, submission);
		ps.executeUpdate();
		PreparedStatement psQ = connection.prepareStatement(sqlQueryID);
		ResultSet rs = psQ.executeQuery();
		while(rs.next()) {
			PreparedStatement psQueryID = connection.prepareStatement(sql2);
			psQueryID.setInt(1, userID);
			psQueryID.setInt(2, courseID);
			psQueryID.setInt(3, taskID);
			System.out.println("QueryResult: " + rs.getInt(1));
			psQueryID.setInt(4, rs.getInt(1));
			psQueryID.executeUpdate();
			break;
		}
			
	}
	
	public void cleanUpRatings(int courseID) throws SQLException {
		
		String sqlRemoveUnconnectedRating = "DELETE FROM dbp079.bewerten b WHERE b.aid IN (SELECT aid FROM dbp079.einreichen WHERE kid=?)";
		PreparedStatement psDelRatings = connection.prepareStatement(sqlRemoveUnconnectedRating);
		psDelRatings.setInt(1, courseID);
		psDelRatings.executeUpdate();
		System.out.println("Success: cleanUpRatings");
		
		//Setup dynamic query-string depending on length of id-array
		//This will delete every rating where one of the taskIDs is inside of the "einreichen" relation
		/*String sqlRemoveRatings = "DELETE FROM dbp079.bewerten b WHERE b.aid IN"
				+ " (SELECT aid FROM dbp079.einreichen e WHERE e.anummer  (";//Bewerten doesnt contain taskIDs, so we have to subselect them
		for(int i=0; i<taskIDs.length; i++) {
			if(i==taskIDs.length-1) {
				sqlRemoveRatings += "?)";
			}else {
				sqlRemoveRatings += "?, ";
			}
		}
		
		try {
			PreparedStatement ps = connection.prepareStatement(sqlRemoveRatings);
			for(int i=1; i<taskIDs.length; i++) {
				ps.setInt(i, taskIDs[i]);
			}
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public void cleanUpSubmissions(ArrayList<Integer> aids) throws SQLException {
		
		String sql = "DELETE FROM dbp079.abgabe WHERE aid=?";
		PreparedStatement psDelSubs = connection.prepareStatement(sql);
		psDelSubs.setInt(1, aids.get(0));
		psDelSubs.executeUpdate();
		System.out.println("Deleted the firs aid for test");
		/*String sqlRemoveUnconnectedSubmissions = "DELETE FROM dbp079.abgabe WHERE aid IN (";
		
		for(int i=0; i<aids.size(); i++) {
			if(i==aids.size()-1) {
				sqlRemoveUnconnectedSubmissions += "?)";
			}else {
				sqlRemoveUnconnectedSubmissions += "?, ";
			}
		}
		
		System.out.println(sqlRemoveUnconnectedSubmissions);
		
		try {
			PreparedStatement ps = connection.prepareStatement(sqlRemoveUnconnectedSubmissions);
			for(int i=1; i<aids.size(); i++) {
				ps.setInt(i, aids.get(i));
			}
			ps.executeUpdate();
			System.out.println("hope this works..");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		PreparedStatement psDelSubs = connection.prepareStatement(sqlRemoveUnconnectedSubmissions);
		psDelSubs.executeUpdate();
		System.out.println("Success: cleanUpSubmissions");*/
		
	}
	
	public void cleanUpTasks(int courseID) throws SQLException {
		
		String sqlRemoveUnconnectedTasks = "DELETE FROM dbp079.aufgabe WHERE kid=?";
		
		PreparedStatement psDelTasks = connection.prepareStatement(sqlRemoveUnconnectedTasks);
		psDelTasks.setInt(1, courseID);
		psDelTasks.executeUpdate();
		System.out.println("Success: cleanUpTasks");
		
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
