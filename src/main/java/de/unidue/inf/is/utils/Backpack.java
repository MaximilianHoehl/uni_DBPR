package de.unidue.inf.is.utils;

import java.util.ArrayList;

public class Backpack {
	
	public static int number;
	private static ArrayList<String> courseTitles;
	private static ArrayList<Integer> courseIDs;
	private static String courseName;
	
	public static void pack(int c) {
		number = c;
	}
	
	public static int yield() {
		if(number == -1) {
			System.out.println("BACKPACK EMPTY!!");
		}
		
		int l = number;
		number = -1;
		return l;
	}

	public static ArrayList<String> getCourseTitles() {
		return courseTitles;
	}

	public static void setCourseTitles(ArrayList<String> courseTitles) {
		Backpack.courseTitles = courseTitles;
	}

	public static ArrayList<Integer> getCourseIDs() {
		return courseIDs;
	}

	public static void setCourseIDs(ArrayList<Integer> courseIDs) {
		Backpack.courseIDs = courseIDs;
	}
	
	public static void clear() {
		number = -1;
		courseTitles = null;
		courseIDs = null;
		courseName = null;
	}

	public static String getCourseName() {
		return courseName;
	}

	public static void setCourseName(String courseName) {
		Backpack.courseName = courseName;
	}
	
}
