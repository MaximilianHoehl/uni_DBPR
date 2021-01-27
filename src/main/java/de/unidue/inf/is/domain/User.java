package de.unidue.inf.is.domain;

public final class User {
	
	private static short currentUserId; 
	
	private short userId;
    private String userName;
    private String email;


    public User() {
    }


    public User(short userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }
    
    //static setter
    public static void setCurrentUser(short id) {
    	currentUserId = id;
    }
    
    //static getter
    public static short getCurrentUserId() {
    	return currentUserId;
    }
    
    //Getter
    public short getUserId() {
        return userId;
    }


    public String getUserName() {
        return userName;
    }
    
    public String getEmail() {
        return email;
    }

}