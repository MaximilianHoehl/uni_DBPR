package de.unidue.inf.is.domain;

import java.io.IOException;

public final class Course {

	private int id;
	private String title;
	private String description;
	private String key;
	private int capacity;
	private int creatorId;
	private String creatorName;
	
	//Constructor for creating instance for UPDATING DB-DATA
	public Course(String title, String description, String key, short capacity, short creator) throws IOException {
			
		this.title = title;
		this.creatorId = creator;
		this.capacity = capacity;
		this.description = description;
		this.key = key;
	}
	//Constructor for creating instance from QUERIED DB-DATA
	public Course(short kid, String title, String description, String key, short capacity, short creator) throws IOException {
		
		this.id = kid;
		this.title = title;
		this.creatorId = creator;
		this.capacity = capacity;
		this.description = description;
		this.key = key;
		this.creatorName = creatorName;
	}
	//Constructor for creating instance from QUERIED DB-DATA with createName
	public Course(short kid, String title, String description, String key, short capacity, short creator, String creatorName) throws IOException {
		
		this.id = kid;
		this.title = title;
		this.creatorId = creator;
		this.capacity = capacity;
		this.description = description;
		this.key = key;
		this.creatorName = creatorName;
	}
	public Boolean evaluateKey(String inputKey) {
		
		return this.key.equals(inputKey);
	}
	
	
	//Getter
	public int getId() {
		return id;
	}
	
	public String getTitle() {
        return title;
    }
	
	public String getDescription() {
		return description;
	}
	
	public String getKey() {
		return key;
	}
	
	public int getCapacity() {
	    return capacity;
	}
	
    public int getCreatorId() {
        return creatorId;
    }
    public String getCreatorName() {
        return creatorName;
    }
   
}
