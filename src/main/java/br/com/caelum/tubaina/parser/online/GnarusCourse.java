package br.com.caelum.tubaina.parser.online;

import java.util.List;

public class GnarusCourse {

	private final String code;
	private final List<GnarusSection> sections;
	
	public GnarusCourse(String code, List<GnarusSection> sections) {
		this.code = code;
		this.sections = sections;
	}

	public String getCode() {
		return code;
	}

	public List<GnarusSection> getSections() {
		return sections;
	}

}
