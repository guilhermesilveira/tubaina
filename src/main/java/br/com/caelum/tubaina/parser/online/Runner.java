package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.caelum.tubaina.builder.BookBuilder;
import br.com.caelum.tubaina.resources.ResourceLocator;

public class Runner {

    private static final Logger LOG = Logger.getLogger(BookBuilder.class);
	private final AfcReader reader;
	private final String afcPath;
	private final String courseCode;
	private final RemoteServer gnarus;
	private final String ignore;
	private boolean splitSections;

	public Runner(RemoteServer gnarus, AfcReader reader, String afcPath, String courseCode, String ignore, boolean splitSections) {
		this.gnarus = gnarus;
		this.reader = reader;
		this.afcPath = afcPath;
		this.courseCode = courseCode;
		this.ignore = ignore;
		this.splitSections = splitSections;
		
		ResourceLocator.initialize(new File(afcPath));
	}
	
	public void start() throws IOException {
		List<GnarusSection> sections = new ArrayList<GnarusSection>();
		
		for(File file : Afc.allIn(afcPath, ignore, splitSections)) {
			Afc afc = reader.read(file);
			
			GnarusSectionConverter parser = new GnarusSectionConverter(courseCode);
			sections.add(parser.convert(afc));
		}
		
		gnarus.sync(new GnarusCourse(courseCode, sections));
	}
	
	public static void main(String[] args) throws IOException {
		
		String server = "localhost:8080/gnarus";
		String extraParameter = "";

		String path = ".";
		String code = "";
		String ignore = "00";
		boolean splitSections = false;
		for(String arg  : args) {
			if(arg.startsWith("-s=")) {
				server = arg.substring(3, arg.length());
			} else if(arg.startsWith("-e=")) {
				extraParameter = arg.substring(3, arg.length());
			} else if(arg.startsWith("-path=")) {
				path = arg.substring(6, arg.length());
			} else if(arg.startsWith("-code=")) {
				code = arg.substring(6, arg.length());
			} else if(arg.startsWith("-ignore=")) {
				ignore = arg.substring(8, arg.length());
			} else if(arg.startsWith("-split=")) {
				splitSections= Boolean.valueOf(arg.substring(7, arg.length()));
			} else {
				System.err.println("Unknown arg: " + arg);
			}
		}
		if(code.equals("")) {
			System.out.println("Usage: java -cp runner.jar br.com.caelum.tubaina.parser.online.Runner -s=online.uri.com.br -e=extra_parameter -path=path_to_all_afc -code=FJ-XX -ignore=PATTERN");
			System.err.println("Did not set the course code");
			System.exit(1);
		}
		parseAndUpload(server, extraParameter, path, code, ignore, splitSections);
		LOG.info("FINISH!");
	}

	private static void parseAndUpload(String server, String extraParameter,
			String path, String code, String ignore, boolean splitSections) throws IOException {
		Runner runner = new Runner(
				new RemoteServer(server, extraParameter),
				new AfcReader(),
				path, code, ignore, splitSections
				);
		
		runner.start();
	}

}
