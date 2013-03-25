package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class AfcReader {

	public Afc read(String file) {
		
		File source = new File(file);
		return read(source);
	}

	public Afc read(File source) {
		try {

			String content = IOUtils.toString(new FileInputStream(source));

			Afc afc = new Afc(getSectionNumber(source));
			String[] textAndExercises = content.split("\\[exercise\\]");
			afc.addText(textAndExercises[0]);
			
			for(int i = 1; i < textAndExercises.length; i++) {
				String[] moreText = textAndExercises[i].split("\\[/exercise\\]");
				afc.addExercises(moreText[0]);
				if(moreText.length > 1) afc.addText(moreText[1]);
			}
			
			return afc;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int getSectionNumber(File source) {
		String fileName = source.getAbsoluteFile().getName();
		int number = Integer.parseInt(fileName.split("-")[0]);
		return number;
	}
}
