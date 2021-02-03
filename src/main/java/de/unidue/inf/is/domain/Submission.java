package de.unidue.inf.is.domain;

public class Submission {

		private int id;
		private String text;
		private float averageMark;
		private String avgMarkText;
		
		public Submission() {
			id = 0;
			text = "Keine Abgabe";
			averageMark = 0;
			avgMarkText = averageMark!=0?String.valueOf(averageMark):"Noch keine Bewertung";
		}
		
		public Submission(int id, String text) {
			this.id = id;
			this.text = text;
			avgMarkText = averageMark!=0?String.valueOf(averageMark):"Noch keine Bewertung";
		}
		
		public void setAvgMark(float avgMark) {
			
			this.averageMark = avgMark;
			avgMarkText = averageMark!=0?String.valueOf(averageMark):"Noch keine Bewertung";
		}

		public float getAverageMark() {
			return averageMark;
		}
		
		public String getavgMarkText() {
			return avgMarkText;
		}

		public String getText() {
			return text;
		}

		public int getId() {
			return id;
		}
}
