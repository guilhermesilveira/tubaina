package br.com.caelum.tubaina.parser.online;

import static org.junit.Assert.*;

import org.junit.Test;

public class AfcTest {
	
	@Test
	public void shouldIgnoreCitations() {
		String result = new Afc(1).addText("God[quote \"The world is mine\" -- God]God").getText();
		assertEquals("GodGod", result);
	}

}
