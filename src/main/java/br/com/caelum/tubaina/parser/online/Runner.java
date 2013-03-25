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
	private final File afcPath;
	private final String courseCode;
	private final RemoteServer gnarus;
	private final String ignore;
	private boolean splitSections;

	public Runner(RemoteServer gnarus, AfcReader reader, File afcPath, String courseCode, String ignore, boolean splitSections) {
		this.gnarus = gnarus;
		this.reader = reader;
		this.afcPath = afcPath;
		this.courseCode = courseCode;
		this.ignore = ignore;
		this.splitSections = splitSections;
		
		ResourceLocator.initialize(afcPath);
	}
	
	public void start() throws IOException {
		List<GnarusSection> sections = new ArrayList<GnarusSection>();
		
		for(File file : new AfcDirectory(afcPath).getChildren(ignore, splitSections)) {
			Afc afc = reader.read(file);
			
			GnarusSectionConverter parser = new GnarusSectionConverter(courseCode);
			sections.add(parser.convert(afc));
		}
		
		gnarus.sync(new GnarusCourse(courseCode, sections));
	}
	
	public static void main(String[] args) throws IOException {
		Parameters params = new Parameters(args);
		if(params.get("code").equals("")) {
			System.out.println("Usage: java -cp runner.jar br.com.caelum.tubaina.parser.online.Runner -server=online.uri.com.br -extraParameter=extra_parameter -path=path_to_all_afc -code=FJ-XX -ignore=PATTERN -splitPerSection=true|false");
			System.err.println("Did not set the course code");
			System.exit(1);
		}
		parseAndUpload(params.get("server"), params.get("extraParameter"), params.getFile("path"), params.get("code"), params.get("ignore"), params.getBoolean("splitPerSection"));
		LOG.info("FINISH!");
	}

	private static void parseAndUpload(String server, String extraParameter,
			File path, String code, String ignore, boolean splitSections) throws IOException {
		Runner runner = new Runner(
				new RemoteServer(server, extraParameter),
				new AfcReader(),
				path, code, ignore, splitSections
				);
		
		runner.start();
	}

}
