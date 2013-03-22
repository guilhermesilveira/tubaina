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
	private final String ignore;

	public Runner(Gnarus gnarus, AfcReader reader, String afcPath, String courseCode, String ignore) {
		this.gnarus = gnarus;
		this.reader = reader;
		this.afcPath = afcPath;
		this.courseCode = courseCode;
		this.ignore = ignore;
		
		ResourceLocator.initialize(new File(afcPath));
	}
	
	public void start() {
		List<GnarusSection> sections = new ArrayList<GnarusSection>();
		
		for(String file : Afc.allIn(afcPath, ignore)) {
			Afc afc = reader.read(file);
			
			GnarusSectionConverter parser = new GnarusSectionConverter(courseCode);
			sections.add(parser.convert(afc));
		}
		
		gnarus.sync(new GnarusCourse(courseCode, sections));
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Usage: java -cp runner.jar br.com.caelum.tubaina.parser.online.Runner -s server_uri -e extra_parameter -path path -code FJ-XX -ignore PATTERN");

		String server = "localhost:8080/gnarus";
		String extraParameter = "";

		String path = ".";
		String code = "";
		String ignore = "00";
		for(String arg  : args) {
			if(arg.startsWith("-s ")) {
				server = arg.substring(3, arg.length());
			} else if(arg.startsWith("-e ")) {
				extraParameter = arg.substring(3, arg.length());
			} else if(arg.startsWith("-path ")) {
				extraParameter = arg.substring(6, arg.length());
			} else if(arg.startsWith("-code ")) {
				extraParameter = arg.substring(6, arg.length());
			} else if(arg.startsWith("-ignore ")) {
				extraParameter = arg.substring(8, arg.length());
			}
		}
		if(code.equals("")) {
			System.err.println("Did not set the course code");
			System.exit(1);
		}
		Runner runner = new Runner(
				new Gnarus(server, extraParameter),
				new AfcReader(),
				path, code, ignore
				);
		
		runner.start();
		System.out.println("FINISH!");
	}

}
