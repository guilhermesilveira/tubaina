package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import br.com.caelum.tubaina.builder.BookBuilder;

public class AfcReader {
	
    private static final Logger LOG = Logger.getLogger(BookBuilder.class);

    public Afc read(String file) {
		return read(new File(file));
	}

	public Afc read(File source) {
		try {

			LOG.info("Parsing " + source.getName());
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
