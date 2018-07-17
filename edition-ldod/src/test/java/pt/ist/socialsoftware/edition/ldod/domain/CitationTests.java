package pt.ist.socialsoftware.edition.ldod.domain;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.RollbackCaseTest;

@ExtendWith(MockitoExtension.class)
public class CitationTests extends RollbackCaseTest {
	@Mock
	VirtualEdition virtualEdition;
	@Mock
	VirtualEditionInter inter;
	@Mock
	LdoDUser user;
	@Mock
	SimpleText startText, endText;
	@Mock
	HumanAnnotation annotation;

	@Mock
	TwitterCitation twitterCitation;
	@Mock
	Tweet tweet;

	@Override
	public void populate4Test() {
	}

	@Test
	public void getIdTest() {
		doCallRealMethod().when(this.twitterCitation).getId();

		// whens ??

		verify(this.twitterCitation, times(1)).getTweetID();

		// asserts ??
	}

	@Test
	public void getNumberOfRetweetsTest() {
		doCallRealMethod().when(this.twitterCitation).getNumberOfRetweets();

		Set<Tweet> tweets = new HashSet<Tweet>();
		tweets.add(this.tweet);
		when(this.twitterCitation.getTweetSet()).thenReturn(tweets);

		verify(this.twitterCitation, times(1)).getTweetSet();

		// asserts ??
	}
}
