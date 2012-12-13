package br.com.caelum.tubaina.parser.online;

import java.io.IOException;
import java.util.List;

import br.com.caelum.tubaina.Book;
import br.com.caelum.tubaina.Chapter;
import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.Section;
import br.com.caelum.tubaina.builder.BookBuilder;
import br.com.caelum.tubaina.parser.RegexConfigurator;
import br.com.caelum.tubaina.parser.Tag;

public class GnarusSectionConverter {

	private static final String IMG_URL = "http://s3.amazonaws.com/caelum-online-public/";
	private static RegexConfigurator conf;
	private static List<Tag> tags;
	private GnarusParser parser;
	private final String courseCode;

	static {
		try {
			conf = new RegexConfigurator();
			tags = conf.read("/regex.properties", "/html.properties");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public GnarusSectionConverter(String courseCode) {
		this.courseCode = courseCode;
		parser = new GnarusParser(tags, false, false);
	}

	public GnarusSection convert(Afc afc) {

		Chapter chapter = chapterFrom(afc.getText());
		String text = replaceImgs(extractFromChunks(chapter));
        String title = chapter.getTitle();

        GnarusSection convertedSection = new GnarusSection(afc.getNumber(), title, text);
		
        int exNumber = 1;
		for(String sb : afc.getExercises()) {
			Chapter exercise = exerciseFrom(sb);
			String question = extractFromChunks(exercise);
			
			convertedSection.addExercise(new GnarusExercise(exNumber++, question, defaultAnswer()));
		}
		
		return convertedSection;
	}

	private String defaultAnswer() {
		return "Excelente! Se você teve alguma dificuldade, não hesite em abrir uma dúvida, ok!?";
	}

	private String replaceImgs(String extractFromChunks) {
		return extractFromChunks.replace("$$RELATIVE$$", IMG_URL + courseCode);
	}

	private Chapter chapterFrom(String originalContent) {
		BookBuilder builder = new BookBuilder("Caelum Online");
		builder.addReaderFromString(originalContent);
		
        Book book = builder.build();
		Chapter uniqueChapter = book.getChapters().get(0);
        return uniqueChapter;
	}
	
	private Chapter exerciseFrom(String originalContent) {
		return chapterFrom("[chapter x][section y][exercise][question]" + originalContent + "[/question][/exercise]");
	}

	private String extractFromChunks(Chapter c) {
		StringBuilder result = new StringBuilder();
        
        for(Section s : c.getSections()) {
        	for(Chunk chunk : s.getChunks()) {
        		String html = chunk.getContent(parser);
        		result.append(html);
        	}
        }
		return result.toString();
	}
}
