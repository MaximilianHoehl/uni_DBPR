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
	
	public static String createInputfiled(String name, String Label) {
		
		return Label + " <input name=" + name + " type\"text\"><br/>";
	}
	
	public static String createMsg(String msg, int type) {
		
		String color;
		switch(type) {
		case 0:
			color = "red;";
			break;
		case 1:
			color = "green;";
			break;
		default:
			color = null;
			System.out.println("Invalid Messagetype at HTMLUtil:createMsg");
		}
		
		String res = "<div style='"
				+ "border-style: solid;"
				+ "position: absolute"
				+ "top: 50%;"
				+ "left: 50%"
				+ "width: 300px;"
				+ "height: 200px;"
				+ "color: " + color
				+ "transform: translation(-50%,-50%);"
				+ "border-width: 5px;"
				+ "display: inline-block;"
				+ "border-radius: 5px;"
				+ "padding: 5px;"
				+ "margin: 5px;'>"
				+ msg
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
