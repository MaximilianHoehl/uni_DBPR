package de.unidue.inf.is.utils;

import java.util.ArrayList;

public class HTMLUtil {
	
	private HTMLUtil() {}
	
	public static String createVPars(ArrayList<String> content) {
		
		String res = new String();
		for(String element : content) {
			res += "<p>" + element + "</p>";
		}
		return res;
	}

	public static String createParagraph(String element) {
		
		return "<p>" + element + "</p>";
	}
	
	public static String createBox(String content) {
		
		String res = "<div style='"
				+ "border-style: solid;"
				+ "border-width: 5px;"
				+ "display: inline-block;"
				+ "border-radius: 5px;"
				+ "padding: 5px;"
				+ "margin: 5px;'>"
				+ content
				+"</div>";
		return res;
	}
	
	public static String createForm(ArrayList<String> inputs, String buttonName) {
		
		String res = "<form>";
		for(String element : inputs) {
			res += "<label for='" + element + "'>" + element + ": " + "</label>";
			res += "<input id='" + element + "'>";
		}
		res += "<button type='submit'>" + buttonName + "</button>";
		res += "</form>";
		
		return res;
	}
}
