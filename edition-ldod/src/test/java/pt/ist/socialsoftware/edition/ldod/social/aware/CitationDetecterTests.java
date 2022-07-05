package pt.ist.socialsoftware.edition.ldod.social.aware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;

@ExtendWith(MockitoExtension.class)
public class CitationDetecterTests {

	private final Logger logger = LoggerFactory.getLogger(CitationDetecterTests.class);

	private CitationDetecter detecter;

	@BeforeAll
	@Atomic(mode = TxMode.WRITE)
	public static void setUpAll() throws FileNotFoundException {
		TestLoadUtils.setUpDatabaseWithCorpus();

		String[] fragments = { "001.xml", "002.xml", "003.xml" };
		TestLoadUtils.loadFragments(fragments);
	}

	@AfterAll
	@Atomic(mode = TxMode.WRITE)
	public static void tearDownAll() throws FileNotFoundException {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@BeforeEach
	public void populate4Test() {
		try {
			this.detecter = new CitationDetecter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void earlyStartTest() throws IOException {
		String teste = "O João foi à escola. A Maria foi para casa";

		int res = this.detecter.lastIndexOfCapitalLetter(teste, 21);
		this.logger.debug("Posição da úlitma maíscula: " + res);

		// int pos = teste.indexOf(".");
		// logger.debug("pos:" + pos);

	}

	@Test
	public void patternFindingSuccessTest() throws IOException {
		String text = "The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. "
				+ "To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages. The new common language will be more";
		String pattern = "The languages only differ in their grammar their pronunciation and their most common words. Everyone realizes why a new common language would be desirable:";
		List<String> result = this.detecter.patternFinding(text, pattern);
		this.logger.debug("Pattern foound: " + result.get(0));
		assertEquals(pattern, result.get(0));
	}

	@Test
	public void patternFindingSubstringSuccessTest() throws IOException {
		String text = "The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. "
				+ "To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages. The new common language will be more";
		String pattern = "aksjdnakfn jndkfnsjkjns The languages only differ skjsdfjfn in their grammar, their pronunciation and their most common words. Everyone realizes why a new common sdbakisdbaikd language would be desirable: ";
		String substringToBeFound = "In their grammar their pronunciation and their most common words. Everyone realizes why a new common";
		List<String> result = this.detecter.patternFinding(text, pattern);
		this.logger.debug("Pattern foound: " + result.get(0));
		assertEquals(substringToBeFound, result.get(0));
	}

	@Test
	public void patternFindingWithFewWordsTest() throws IOException {
		String text = "The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. "
				+ "To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages. The new common language will be more";
		String pattern = "The languages only differ in their grammar, their";
		List<String> result = this.detecter.patternFinding(text, pattern);
		this.logger.debug("Pattern foound: " + result.get(0));
		assertEquals("", result.get(0));
	}

	@Test
	public void patternFindingWithNoPatternTest() throws IOException {
		String text = "The European languages are members of the same family. Their separate existence is a myth. For science, music, sport, etc, Europe uses the same vocabulary. The languages only differ in their grammar, their pronunciation and their most common words. Everyone realizes why a new common language would be desirable: one could refuse to pay expensive translators. "
				+ "To achieve this, it would be necessary to have uniform grammar, pronunciation and more common words. If several languages coalesce, the grammar of the resulting language is more simple and regular than that of the individual languages. The new common language will be more";
		String pattern = "Fernando Pessoa escreveu o Livro do Desassossego, bem como outras obras, como por exemplo A Mensagem";
		List<String> result = this.detecter.patternFinding(text, pattern);
		this.logger.debug("Pattern foound: " + result.get(0));
		assertEquals("", result.get(0));
	}

	@Test
	public void maxJaroValueTest() {
		String text = "gato gat ga g";
		String wordToFind = "ga";
		List<String> result = this.detecter.maxJaroValue(text, wordToFind);
		assertEquals(wordToFind, result.get(0));
	}

	@Test
	public void cleanTweetTextSeparatedHyphenTest() {
		String s = "chamar - lhe";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "chamar   lhe";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointHyphenTest() {
		String s = "chamar-lhe";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "chamar-lhe";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedFullStopTest() {
		String s = "acabou . Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointFullStopTest() {
		String s = "acabou.Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou.depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedCommaTest() {
		String s = "acabou , Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointCommaTest() {
		String s = "acabou,Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou,depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedQuestionMarkTest() {
		String s = "acabou ? Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointQuestionMarkTest() {
		String s = "acabou?Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou?depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedExclamationMarkTest() {
		String s = "acabou ! Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointExclamationMarkTest() {
		String s = "acabou!Depois fomos";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "acabou!depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetText_Q_inTheEndTest() {
		String s = "ele e o amigo q";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "ele e o amigo q";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparated_Q_Test() {
		String s = "ele e o amigo q estavam a jogar";
		String result = this.detecter.cleanTweetText(s);
		String toAssert = "ele e o amigo que estavam a jogar";
		assertEquals(toAssert, result);
	}

	@Test
	public void countOccurencesOfOneSubstringTest() {
		int occurrences = this.detecter.countOccurencesOfSubstring("cão galinha gato", "cão", 100);
		assertEquals(1, occurrences);
	}

	@Test
	public void countOccurencesOfSemiSubstringTest() {
		int occurrences = this.detecter.countOccurencesOfSubstring("cão galinha gato", "ga", 100);
		assertEquals(2, occurrences);
	}

	@Test
	public void countOccurencesOfConsecutiveSubstringTest() {
		int occurrences = this.detecter.countOccurencesOfSubstring("cão galinha galinha gato", "galinha", 100);
		assertEquals(2, occurrences);
	}

	@Test
	public void countOccurencesOfAlternateSubstringTest() {
		int occurrences = this.detecter.countOccurencesOfSubstring("gato galinha gato galinha gato gato galinha",
				"gato", 100);
		assertEquals(4, occurrences);
	}

	@Test
	public void countOccurencesOfNonExistingSubstringTest() {
		int occurrences = this.detecter.countOccurencesOfSubstring("gato galinha gato galinha gato gato galinha", "cão",
				100);
		assertEquals(0, occurrences);
	}

	@Test
	public void startBiggerThanEndTest() {
		assertTrue(this.detecter.startBiggerThanEnd(5, 4, 3, 3));
	}

	@Test
	public void endBiggerThanStartTest() {
		assertFalse(this.detecter.startBiggerThanEnd(4, 5, 3, 3));
	}

	@Test
	public void startEqualToEndTest() {
		assertFalse(this.detecter.startBiggerThanEnd(5, 5, 3, 3));
	}

	@Test
	public void startBiggerThanEndWithDifferentDivTest() {
		assertFalse(this.detecter.startBiggerThanEnd(5, 4, 3, 6));
	}
}
