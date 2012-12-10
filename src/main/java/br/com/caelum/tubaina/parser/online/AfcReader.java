package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AfcReader {

	public Afc read(String file) {
		
		try {
			File source = new File(file);
			
			String content = "";
			Scanner sc = new Scanner(source);
			while(sc.hasNextLine()) {
				content += sc.nextLine() + "\n";
			}
			
			Afc afc = new Afc(getSectionNumber(source));
			String[] textAndExercises = content.split("\\[exercise\\]");
			afc.addText(textAndExercises[0]);
			
			for(int i = 1; i < textAndExercises.length; i++) {
				String[] moreText = textAndExercises[i].split("\\[/exercise\\]");
				afc.addExercises(moreText[0]);
				if(moreText.length > 1) afc.addText(moreText[1]);
			}
			
			return afc;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private int getSectionNumber(File source) {
		int number = Integer.parseInt(source.getAbsoluteFile().getName().split("-")[0]);
		return number;
	}
}
