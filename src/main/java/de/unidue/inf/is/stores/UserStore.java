package de.unidue.inf.is.stores;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;



public final class UserStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public UserStore() throws StoreException {	//create instance and open DB connection
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
            System.out.println("Setted up ext connection: " + connection);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }
    
	public User getUserById(int id) {
			
			try {
				
				User res;
				String query = "SELECT * FROM dbp079.benutzer b WHERE b.bnummer = " + String.valueOf(id);
				PreparedStatement st = connection.prepareStatement(query);
				ResultSet resultset = st.executeQuery();
				
				ResultSetMetaData rsmd = resultset.getMetaData();
				int colNum = rsmd.getColumnCount();
				System.out.println("hereFromUserStore");
				int counter = 0;
				
				if(resultset.next()) {
				res = new User(Integer.valueOf(
						resultset.getShort(1)), 
						resultset.getString(3), 
						resultset.getString(2));
				return res;
				}
				
				
				return null;
				
				
			}catch(SQLException e) {
				
				e.printStackTrace();
				return null;
			}	
		}

    public void addUser(User userToAdd) throws StoreException {	//add a user to instance (add to commit)
        try {
            PreparedStatement preparedStatement = connection
                            .prepareStatement("insert into dbp079.benutzer (name, email) values (?, ?)");
            short id = 7;
            System.out.println("0");
            //preparedStatement.setShort(1, id);
            System.out.println("1");
            preparedStatement.setString(1, userToAdd.getUserName());
            System.out.println("2");
            preparedStatement.setString(2, userToAdd.getEmail());
            System.out.println("3");
            preparedStatement.executeUpdate();
            System.out.println("4");
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
