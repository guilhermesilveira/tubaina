package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Afc {

	private final StringBuilder text = new StringBuilder();
	private final List<String> exercises = new ArrayList<String>();
	private final int number;

	public Afc(int number) {
		this.number = number;
	}

	public void addText(String text) {
		this.text.append(text);
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

	public static List<File> allIn(String path, String ignoreExpr) {
		File origin = new File(path);
		if(!origin.isDirectory()) {
			throw new RuntimeException(path + " is not a directory");
		}
		List<File> files = new ArrayList<File>();
		for (File f : origin.listFiles()) {
			File absolute = f.getAbsoluteFile();
			String fileName = absolute.getName();
			if (origin.isFile() && fileName.endsWith(".afc")
					&& !ignore(fileName, ignoreExpr)) {
				files.add(absolute);
			}
		}			
		return files;
	}

	private static boolean ignore(String path, String ignoreExpr) {
		for (String regex : ignoreExpr.split(",")) {
			if (path.startsWith(regex))
				return true;
		}

		return false;
	}
}
