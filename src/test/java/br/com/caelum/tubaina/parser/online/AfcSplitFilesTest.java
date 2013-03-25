package br.com.caelum.tubaina.parser.online;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class AfcSplitFilesTest {
	
	String part1 = "[chapter Parte 1] Primeira parte\n";
	String part2 = "[section Parte 2]Parte 2\nContinuacao da parte 2";
	String part3 = "[section Pre-exercises]pre exercicios";
	String part4 = "[section Exercises for those sections][exercises]pre exercise[exercise]first exercise[/exercise]concluding[/exercises]";
	String part5 = "[section 2nd Pre-exercises]2nd pre exercicios";
	String part6 = "[section Parte 6]Parte 6\nContinuacao da parte";
	String part7 = "[section Exercises for those sections 7][exercises]pre exercise 7[exercise]first exercise[/exercise]concluding[/exercises]";

	@Test
	public void should_join_sections_without_exercises() {
		String content = part1 + part2 + part3;
		List<String> afcs = new AfcSplitFiles().splitAfcIntoAfcs(content, 0);
		assertEquals(1, afcs.size());
		assertEquals(part1 + part2 + part3, afcs.get(0));
	}

	@Test
	public void should_join_sections_including_the_first_with_exercises() {
		String content = part1 + part2 + part3 + part4;
		List<String> afcs = new AfcSplitFiles().splitAfcIntoAfcs(content, 0);
		assertEquals(1, afcs.size());
		assertEquals(part1 + part2 + part3 + part4, afcs.get(0));
	}

	@Test
	public void should_not_join_section_even_without_exercises_right_after_exercises() {
		String content = part1 + part2 + part3 + part4 + part5 + part6 + part7;
		List<String> afcs = new AfcSplitFiles().splitAfcIntoAfcs(content, 0);
		assertEquals(2, afcs.size());
		assertEquals(part1 + part2 + part3 + part4, afcs.get(0));
		assertEquals(part5.replaceAll("section", "chapter") + part6 + part7, afcs.get(1));
	}

	@Test(expected=IllegalArgumentException.class)
	public void must_have_a_chapter() {
		new AfcSplitFiles().splitAfcIntoAfcs(part2, 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void must_have_a_section() {
		new AfcSplitFiles().splitAfcIntoAfcs(part1, 0);
	}

}
