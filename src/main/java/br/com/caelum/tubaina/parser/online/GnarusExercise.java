package br.com.caelum.tubaina.parser.online;

public class GnarusExercise {

	private final int number;
	private final String text;
	private final String answer;
	
	public GnarusExercise(int number, String text, String answer) {
		this.number = number;
		this.text = text;
		this.answer = answer;
	}

	public int getNumber() {
		return number;
	}

	public String getText() {
		return text;
	}

	public String getAnswer() {
		return answer;
	}
}
