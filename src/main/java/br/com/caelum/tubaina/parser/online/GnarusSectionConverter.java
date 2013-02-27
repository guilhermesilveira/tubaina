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
			try {
				Chapter questionChap = exerciseFrom(sb);
				String question = extractFromChunks(questionChap);
				String answer = answerFrom(sb);
				
				convertedSection.addExercise(new GnarusExercise(exNumber++, question, defaultAnswer(answer)));
			} catch(Exception e) {
				System.out.println("Exercicio " + exNumber + " no cap " + title + " nao rolou");
			}
		}
		
		return convertedSection;
	}

	private String answerFrom(String sb) {
		String[] originalContent = sb.split("\\[answer\\]");
		if(originalContent.length <= 1) return null;
		
		String theAnswer = originalContent[1].split("\\[/answer\\]")[0];
		String fakeChapter = "[chapter x][section y]" + theAnswer;
		return extractFromChunks(chapterFrom(fakeChapter));
	}

	private String defaultAnswer(String answer) {
		if(answer!=null && answer.length()>0) return answer;
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
		String fakeChapter = "[chapter x][section y][exercise][question]" + originalContent + "[/question][/exercise]";
		return chapterFrom(fakeChapter);
	}

	private String extractFromChunks(Chapter c) {
		StringBuilder result = new StringBuilder();

		Section s = c.getSections().get(0);
    	for(Chunk chunk : s.getChunks()) {
    		String html = chunk.getContent(parser);
    		result.append(html);
    	}
		return result.toString();
	}
}
