package br.com.caelum.tubaina.parser.online;

import br.com.caelum.tubaina.parser.Parser;

public class DelegateParser implements Parser{

	private final Parser delegate;

	public String parse(String text) {
		return delegate.parse(text);
	}

	public String parseParagraph(String text) {
		return delegate.parseParagraph(text);
	}

	public String parseParagraphInsideItem(String text) {
		return delegate.parseParagraphInsideItem(text);
	}

	public String parseJava(String text, String options) {
		return delegate.parseJava(text, options);
	}

	public String parseBox(String text, String options) {
		return delegate.parseBox(text, options);
	}

	public String parseImage(String text, String options) {
		return delegate.parseImage(text, options);
	}

	public String parseCode(String text, String options) {
		return delegate.parseCode(text, options);
	}

	public String parseGist(String options) {
		return delegate.parseGist(options);
	}

	public String parseList(String text, String options) {
		return delegate.parseList(text, options);
	}

	public String parseXml(String text, String options) {
		return delegate.parseXml(text, options);
	}

	public String parseExercise(String text, int id) {
		return delegate.parseExercise(text, id);
	}

	public String parseAnswer(String text, int id) {
		return delegate.parseAnswer(text, id);
	}

	public String parseQuestion(String text) {
		return delegate.parseQuestion(text);
	}

	public String parseNote(String text, String title) {
		return delegate.parseNote(text, title);
	}

	public String parseItem(String text) {
		return delegate.parseItem(text);
	}

	public String parseTodo(String text) {
		return delegate.parseTodo(text);
	}

	public String parseIndex(String name) {
		return delegate.parseIndex(name);
	}

	public String parseTable(String text, String title, boolean noborder,
			int columns) {
		return delegate.parseTable(text, title, noborder, columns);
	}

	public String parseRow(String text) {
		return delegate.parseRow(text);
	}

	public String parseColumn(String text) {
		return delegate.parseColumn(text);
	}

	public String parseCenteredParagraph(String content) {
		return delegate.parseCenteredParagraph(content);
	}

	public String parseRuby(String content, String options) {
		return delegate.parseRuby(content, options);
	}

	public DelegateParser(Parser delegate) {
		this.delegate = delegate;
	}

}
