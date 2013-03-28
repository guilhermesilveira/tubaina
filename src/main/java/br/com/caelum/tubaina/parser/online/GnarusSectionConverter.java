package br.com.caelum.tubaina.parser.online;

import java.io.IOException;
import java.util.List;

import br.com.caelum.tubaina.Book;
import br.com.caelum.tubaina.Chapter;
import br.com.caelum.tubaina.Chunk;
import br.com.caelum.tubaina.Section;
import br.com.caelum.tubaina.builder.BookBuilder;
import br.com.caelum.tubaina.chunk.ExerciseChunk;
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

		System.out.println("Capitulo " + afc.getNumber());
		Chapter chapter = chapterFrom(afc.getText());
		String text = replaceImgs(extractFromChunks(chapter));
        String title = chapter.getTitle();

        GnarusSection convertedSection = new GnarusSection(afc.getNumber(), title, text);
		
        int exNumber = 1;
		for(String sb : afc.getExercises()) {
			try {
				Chapter questionChap = exerciseFrom(sb);
				String question = replaceImgs(extractFromChunks(questionChap));
				String answer = replaceImgs(answerFrom(sb));
				
				GnarusExercise exercise = new GnarusExercise(exNumber++, question, defaultAnswer(answer));
				convertedSection.addExercise(exercise);
			} catch(Exception e) {
				System.err.println("Exercicio " + exNumber + " no cap " + title + " nao rolou");
				System.err.println(sb);
				e.printStackTrace();
				System.exit(1);
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

	private String replaceImgs(String text) {
		return text==null?"" : text.replace("$$RELATIVE$$", IMG_URL + courseCode);
	}

	private Chapter chapterFrom(String originalContent) {
		BookBuilder builder = new BookBuilder("Caelum Online");
		builder.addReaderFromString(originalContent);
		
        Book book = builder.build();
		List<Chapter> chapters = book.getChapters();
		if(chapters.isEmpty()) throw new IllegalArgumentException("There is no chapter in this file");
		Chapter uniqueChapter = chapters.get(0);
        return uniqueChapter;
	}
	
	private Chapter exerciseFrom(String originalContent) {
		String fakeChapter = "[chapter x][section __YY__][exercise][question]" + originalContent + "[/question][/exercise]";
		return chapterFrom(fakeChapter);
	}

	private String extractFromChunks(Chapter c) {
		StringBuilder result = new StringBuilder();
		String introduction = c.getIntroduction(parser);
		result.append(introduction);

		for(Section section : c.getSections()) {
			String sectionContent = grabSection(section);
			if(!sectionContent.trim().isEmpty()) {
				if(!section.getTitle().contains("__YY__")) {
					result.append("\n<b>" + section.getTitle() + "</b>\n\n");
				}
				result.append(sectionContent);
			}
		}
		return result.toString();
	}

	private String grabSection(Section s) {
		StringBuilder content = new StringBuilder();
		for(Chunk chunk : s.getChunks()) {
			if(chunk instanceof ExerciseChunk) {
				String html = chunk.getContent(new ExerciseWithoutAnswerParser(parser));
				content.append(html);
			} else {
				String html = chunk.getContent(parser);
				content.append(html);
			}
			content.append("\n");
		}
		return content.toString();
	}
}
