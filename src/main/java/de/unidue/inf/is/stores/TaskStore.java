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
