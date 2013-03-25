package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AfcDirectory {

	private final File root;

	public AfcDirectory(File root) {
		this.root = root;
		if (!root.isDirectory()) {
			throw new RuntimeException(root.getName() + " is not a directory");
		}
	}

	public List<File> getChildren(String ignoreExpr, boolean splitSections)
			throws IOException {
		int lastNumber = 0;
		List<File> files = new ArrayList<File>();
		for (File child : root.listFiles()) {
			File absolute = child.getAbsoluteFile();
			String fileName = absolute.getName();
			if (child.isFile() && fileName.endsWith(".afc")
					&& !ignore(fileName, ignoreExpr)) {
				if (splitSections) {
					List<File> splited = new AfcSplitFiles().splitFileIntoFiles(child, lastNumber);
					lastNumber += splited.size();
					files.addAll(splited);
				} else {
					files.add(absolute);
				}
			}
		}
		return files;
	}

	private boolean ignore(String path, String ignoreExpr) {
		for (String regex : ignoreExpr.split(",")) {
			if (path.startsWith(regex))
				return true;
		}

		return false;
	}
}
