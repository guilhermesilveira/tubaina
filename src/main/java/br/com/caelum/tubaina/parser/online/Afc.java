package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Afc {

	static final String FILE_ENCODING = "UTF-8";
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

	public static List<File> allIn(String path, String ignoreExpr,
			boolean splitSections) throws IOException {
		File origin = new File(path);
		if (!origin.isDirectory()) {
			throw new RuntimeException(path + " is not a directory");
		}
		int lastNumber = 0;
		List<File> files = new ArrayList<File>();
		for (File f : origin.listFiles()) {
			File absolute = f.getAbsoluteFile();
			String fileName = absolute.getName();
			if (f.isFile() && fileName.endsWith(".afc")
					&& !ignore(fileName, ignoreExpr)) {
				if (splitSections) {
					List<File> splited = splitAll(f, lastNumber);
					lastNumber += splited.size();
					files.addAll(splited);
				} else {
					files.add(absolute);
				}
			}
		}
		return files;
	}

	private static List<File> splitAll(File file, int previousSectionNumber)
			throws IOException {
		List<File> all = new ArrayList<>();
		String content = IOUtils.toString(new FileInputStream(file), FILE_ENCODING);
		int currentSectionNumber = previousSectionNumber;
		String lastContent = null;
		int exerciseCount = 0;
		while (content.contains("[section")) {
			int position = content.indexOf("[section", 1);
			if (position == -1)
				position = content.length();
			String section = content.substring(0, position);

			boolean join = shouldJoin(previousSectionNumber, currentSectionNumber, exerciseCount);

			int currentExercises = section.split("\\[exercise").length - 1;
			if (join) {
				section = lastContent + section;
				exerciseCount += currentExercises;
			} else {
				exerciseCount = currentExercises;
				section = section.replaceAll("\\[section ", "\\[chapter ");
				lastContent = section;
				currentSectionNumber++;
			}

			File tempFile = new File(file.getParentFile(), currentSectionNumber + "-"
					+ file.getName() + ".tmp");
			FileOutputStream output = new FileOutputStream(tempFile);
			IOUtils.write(section, output, FILE_ENCODING);
			output.close();

			if (!join) {
				//tempFile.deleteOnExit();
				all.add(tempFile);
			}

			content = content.substring(position, content.length());
		}
		return all;
	}

	private static boolean shouldJoin(int previousSectionNumber, int currentSectionNumber, int exerciseCount) {
		return previousSectionNumber!=currentSectionNumber && exerciseCount == 0;
	}

	private static boolean ignore(String path, String ignoreExpr) {
		for (String regex : ignoreExpr.split(",")) {
			if (path.startsWith(regex))
				return true;
		}

		return false;
	}
}
