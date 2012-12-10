package br.com.caelum.tubaina.parser.online;

import java.util.ArrayList;
import java.util.List;

public class GnarusSection {

	private final int number;
	private final String title;
	private final String text;
	private final List<GnarusExercise> exercises;
	
	public GnarusSection(int number, String title, String text) {
		this.number = number;
		this.title = title;
		this.text = text;
		
		exercises = new ArrayList<GnarusExercise>();
	}
	
	public String getTitle() {
		return title;
	}
	public String getText() {
		return text;
	}
	public List<GnarusExercise> getExercises() {
		return exercises;
	}
	public void addExercise(GnarusExercise ex) {
		exercises.add(ex);
	}
	public int getNumber() {
		return number;
	}
	
}
