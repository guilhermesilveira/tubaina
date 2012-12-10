package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.tubaina.resources.ResourceLocator;

public class Runner {

	private final AfcReader reader;
	private final String afcPath;
	private final String courseCode;
	private final Gnarus gnarus;

	public Runner(Gnarus gnarus, AfcReader reader, String afcPath, String courseCode) {
		this.gnarus = gnarus;
		this.reader = reader;
		this.afcPath = afcPath;
		this.courseCode = courseCode;
		
		ResourceLocator.initialize(new File(afcPath));
	}
	
	public void start() {
		List<GnarusSection> sections = new ArrayList<GnarusSection>();
		
		for(String file : Afc.allIn(afcPath)) {
			Afc afc = reader.read(file);
			
			GnarusSectionConverter parser = new GnarusSectionConverter(courseCode);
			sections.add(parser.convert(afc));
		}
		
		gnarus.sync(new GnarusCourse(courseCode, sections));
	}
	
	public static void main(String[] args) throws IOException {

		Runner runner = new Runner(
				new Gnarus(),
				new AfcReader(), 
				"/Users/mauricioaniche/textos/apostilas-novas/FJ-11/", 
				"FJ-11");
		
		runner.start();
		System.out.println("FINISH!");
	}


}
