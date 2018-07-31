package pt.ist.socialsoftware.edition.ldod.social.aware;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.RollbackCaseTest;

@ExtendWith(MockitoExtension.class)
public class CitationDetecterTests extends RollbackCaseTest {

	private Logger logger = LoggerFactory.getLogger(CitationDetecterTests.class);

	CitationDetecter detecter;

	@Override
	public void populate4Test() {
		try {
			detecter = new CitationDetecter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void patternFindingTest() {

	}

	@Test
	public void maxJaroValueTest() {
		String text = "gato gat ga g";
		String wordToFind = "ga";
		List<String> result = detecter.maxJaroValue(text, wordToFind);
		assertEquals(wordToFind, result.get(0));
	}

	@Test
	public void cleanTweetTextSeparatedHyphenTest() {
		String s = "chamar - lhe";
		String result = detecter.cleanTweetText(s);
		String toAssert = "chamar   lhe";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointHyphenTest() {
		String s = "chamar-lhe";
		String result = detecter.cleanTweetText(s);
		String toAssert = "chamar-lhe";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedFullStopTest() {
		String s = "acabou . Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointFullStopTest() {
		String s = "acabou.Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou.depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedCommaTest() {
		String s = "acabou , Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointCommaTest() {
		String s = "acabou,Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou,depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedQuestionMarkTest() {
		String s = "acabou ? Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointQuestionMarkTest() {
		String s = "acabou?Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou?depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparatedExclamationMarkTest() {
		String s = "acabou ! Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou   depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextJointExclamationMarkTest() {
		String s = "acabou!Depois fomos";
		String result = detecter.cleanTweetText(s);
		String toAssert = "acabou!depois fomos";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetText_Q_inTheEndTest() {
		String s = "ele e o amigo q";
		String result = detecter.cleanTweetText(s);
		String toAssert = "ele e o amigo q";
		assertEquals(toAssert, result);
	}

	@Test
	public void cleanTweetTextSeparated_Q_Test() {
		String s = "ele e o amigo q estavam a jogar";
		String result = detecter.cleanTweetText(s);
		String toAssert = "ele e o amigo que estavam a jogar";
		assertEquals(toAssert, result);
	}

	@Test
	public void countOccurencesOfOneSubstringTest() {
		int occurrences = detecter.countOccurencesOfSubstring("cão galinha gato", "cão", 100);
		assertEquals(1, occurrences);
	}

	@Test
	public void countOccurencesOfSemiSubstringTest() {
		int occurrences = detecter.countOccurencesOfSubstring("cão galinha gato", "ga", 100);
		assertEquals(2, occurrences);
	}

	@Test
	public void countOccurencesOfConsecutiveSubstringTest() {
		int occurrences = detecter.countOccurencesOfSubstring("cão galinha galinha gato", "galinha", 100);
		assertEquals(2, occurrences);
	}

	@Test
	public void countOccurencesOfAlternateSubstringTest() {
		int occurrences = detecter.countOccurencesOfSubstring("gato galinha gato galinha gato gato galinha", "gato",
				100);
		assertEquals(4, occurrences);
	}

	@Test
	public void countOccurencesOfNonExistingSubstringTest() {
		int occurrences = detecter.countOccurencesOfSubstring("gato galinha gato galinha gato gato galinha", "cão",
				100);
		assertEquals(0, occurrences);
	}

	@Test
	public void startBiggerThanEndTest() {
		assertTrue(detecter.startBiggerThanEnd(5, 4, 3, 3));
	}

	@Test
	public void endBiggerThanStartTest() {
		assertFalse(detecter.startBiggerThanEnd(4, 5, 3, 3));
	}

	@Test
	public void startEqualToEndTest() {
		assertFalse(detecter.startBiggerThanEnd(5, 5, 3, 3));
	}

	@Test
	public void startBiggerThanEndWithDifferentDivTest() {
		assertFalse(detecter.startBiggerThanEnd(5, 4, 3, 6));
	}
}
