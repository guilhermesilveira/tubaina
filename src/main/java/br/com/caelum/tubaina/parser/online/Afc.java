package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Afc {

	private StringBuilder text;
	private List<String> exercises;
	private final int number;
	
	public Afc(int number) {
		this.number = number;
		
		text = new StringBuilder();
		exercises = new ArrayList<String>();
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
		for(int i = 0; i < questions.length; i++) {
			String question = questions[i];
			if(question.replaceAll("\\n", "").replaceAll("\\t", "").length() > 0) {
				exercises.add(question.replace("[/question]", ""));
			}
			
		}
	}
	
	public int getNumber() {
		return number;
	}
	
	public static List<String> allIn(String path, String ignoreExpr) {
		List<String> files = new ArrayList<String>();
		for(File f : new File(path).listFiles()) {
			String fileName = f.getAbsoluteFile().getName();
			if(f.isFile() && fileName.endsWith(".afc") && !ignore(fileName, ignoreExpr)) {
				files.add(f.getAbsolutePath());
			}
		}
		
		return files;
	}

	private static boolean ignore(String path, String ignoreExpr) {
		for(String regex : ignoreExpr.split(",")) {
			if(path.startsWith(regex)) return true;
		}
		
		return false;
	}
}
