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
			String content = IOUtils.toString(new FileInputStream(source), AfcSplitFiles.FILE_ENCODING);

			Afc afc = new Afc(getSectionNumber(source));
			String[] textAndExercises = content.split("\\[exercise\\]");
			String text = textAndExercises[0];
			afc.addText(text);

			for (int i = 1; i < textAndExercises.length; i++) {
				String[] moreText = textAndExercises[i]
						.split("\\[/exercise\\]");
				String exercises = moreText[0];
				afc.addExercises(exercises);
				if (moreText.length > 1) {
					String extraContent = moreText[1];
					afc.addText(extraContent);
				}
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
