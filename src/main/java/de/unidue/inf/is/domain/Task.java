package de.unidue.inf.is.domain;

public class Task {
	
	private int id;
	private int courseID;
	private String name;
	private String description;
	private Submission userSubmission;
	
	public Task(int id, int courseID, String name, String description) {
		this.id = id;
		this.courseID = courseID;
		this.name = name;
		this.description = description;
	}
	
	public void setUserSubmission(Submission userSubmission) {
		
		this.userSubmission = userSubmission;
	}

	public int getId() {
		return id;
	}

	public int getCourseID() {
		return courseID;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Submission getUserSubmission() {
		return userSubmission;
	}

}
