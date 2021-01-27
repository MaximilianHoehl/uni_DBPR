package de.unidue.inf.is.domain;

public final class Course {
	
	private int id;
	private String title;
	private String description;
	private String key;
	private int capacity;
	private int creator;
	
	
	public Course(short kid, String title, String description, String key, short capacity, short creator) {
		
		this.id = kid;
		this.title = title;
		this.creator = creator;
		this.capacity = capacity;
		this.description = description;
		this.key = key;
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
	
    public int getCreator() {
        return creator;
    }
    
   
}
