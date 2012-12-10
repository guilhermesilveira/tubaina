package br.com.caelum.tubaina.parser.online;

import java.util.List;

import br.com.caelum.tubaina.parser.Tag;
import br.com.caelum.tubaina.parser.html.CodeTagOptionsParser;
import br.com.caelum.tubaina.parser.html.desktop.HtmlParser;
import br.com.caelum.tubaina.util.HtmlSanitizer;

public class GnarusParser extends HtmlParser {

	private final HtmlSanitizer sanitizer = new HtmlSanitizer();
	
	public GnarusParser(List<Tag> tags, boolean noAnswer, boolean showNotes) {
		super(tags, noAnswer, showNotes);
	}

	// SEM PYGMENTS
    public String parseCode(String text, String options) {
    	CodeTagOptionsParser opts = new CodeTagOptionsParser();
		String language = opts.parseLanguage(options);
		
		if(language.equals("text")) return parseParagraph(text);
		return "[" + language  + "]\n" + text + "\n[/" + language + "]";
    }
	
	// PYGMENTS
//    public String parseCode(String text, String options) {
//        SyntaxHighlighter syntaxHighlighter = new SyntaxHighlighter(new CommandExecutor(),
//                CodeOutputType.HTML, false, new CodeCache(CodeOutputType.HTML));
//        return new HtmlAndKindleCodeTag(syntaxHighlighter).parse(text, options);
//	}
    
    public String parseIndex(String name) {
    	return "";
    }

    public String parseTodo(String text) {
    	return "";    
    }
    
    public String parseBox(String text, String options) {
        String title = this.sanitizer.sanitize(options);
		return "<strong>" + parse(title.trim()) 
				+ "</strong>\n\n" + parse(text.trim()) + "\n\n";

    }
    
    public String parseParagraph(String text) {
    	return parse(text) + "\n\n";
    }

    public String parseImage(String text, String options) {
        return super.parseImage(text, options) + "\n\n";
    }
    
    public String parseExercise(String text, int id) {
        return parse(text);
    }

    public String parseQuestion(String text) {
        return parse(text);
    }
    
    public String parseAnswer(String text, int id) {
        return parse(text);
    }

}
