package br.com.caelum.tubaina.parser.html;

import br.com.caelum.tubaina.parser.Tag;

public class IndexTagTemplate implements Tag {

	public String parse(String string, String options) {
		return "\n<a id=\"" + string + "\"></a>\n";
	}

}
