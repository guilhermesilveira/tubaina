package br.com.caelum.tubaina.parser.online;

import java.util.ArrayList;
import java.util.List;

public class Afc {

	private final StringBuilder text = new StringBuilder();
	private final List<String> exercises = new ArrayList<String>();
	private final int number;

	public Afc(int number) {
		this.number = number;
	}

	public Afc addText(String text) {
		this.text.append(removeIgnored(text));
		return this;
	}

	private String removeIgnored(String text) {
		return text.replaceAll("\\[quote[^\\]]*\\]", "");
	}

	public String getText() {
		return text.toString();
	}

	public List<String> getExercises() {
		return exercises;
	}

	public void addExercises(String exercisesText) {
		String[] questions = exercisesText.split("\\[question\\]");
		for (int i = 0; i < questions.length; i++) {
			String question = questions[i];
			if (question.replaceAll("\\n", "").replaceAll("\\t", "").length() > 0) {
				exercises.add(question.replace("[/question]", ""));
			}
		}
	}

	public int getNumber() {
		return number;
	}

}
