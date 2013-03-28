package br.com.caelum.tubaina.parser.online;

import br.com.caelum.tubaina.parser.Parser;

public class ExerciseWithoutAnswerParser extends DelegateParser {
	
	ExerciseWithoutAnswerParser(Parser p) {
		super(p);
	}
	
	@Override
	public String parseAnswer(String text, int id) {
		return "";
	}

}
