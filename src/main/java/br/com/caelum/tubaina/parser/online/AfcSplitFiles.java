package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class AfcSplitFiles {

	static final String FILE_ENCODING = "UTF-8";

	List<File> splitFileIntoFiles(File file, int previousSectionNumber)
			throws IOException {
		String content = IOUtils.toString(new FileInputStream(file),
				FILE_ENCODING);
		List<String> contents = splitAfcIntoAfcs(content, previousSectionNumber);
		List<File> files = new ArrayList<>();
		for (String childContent : contents) {
			String name = (files.size() + 1) + "-" + file.getName()
					+ ".afc.tmp";
			File child = new File(file.getParentFile(), name);
			FileOutputStream output = new FileOutputStream(child);
			IOUtils.write(childContent, output, FILE_ENCODING);
			output.close();
			child.deleteOnExit();
			files.add(child);
		}
		return files;
	}

	List<String> splitAfcIntoAfcs(String content, int previousSectionNumber) {
		if (!content.contains("[section") || !content.contains("[chapter"))
			throw new IllegalArgumentException(
					"Must have at least one section and one chapter tag");

		LinkedList<String> all = new LinkedList<>();
		int currentSectionNumber = previousSectionNumber;
		int exerciseCount = 0;
		
		while (content.contains("[section")) {
			int position = content.indexOf("[section", 1);
			if (position == -1)
				position = content.length();
			String section = content.substring(0, position);

			boolean join = shouldJoin(previousSectionNumber,
					currentSectionNumber, exerciseCount);

			int currentExercises = section.split("\\[exercise").length - 1;
			if (join) {
				String last = all.removeLast();
				all.add(last + section);
				exerciseCount += currentExercises;
			} else {
				exerciseCount = currentExercises;
				section = section.replaceAll("\\[section ", "\\[chapter ");
				all.add(section);
				currentSectionNumber++;
			}

			content = content.substring(position, content.length());
		}
		return all;
	}

	private boolean shouldJoin(int previousSectionNumber,
			int currentSectionNumber, int exerciseCount) {
		return previousSectionNumber != currentSectionNumber
				&& exerciseCount == 0;
	}

}
