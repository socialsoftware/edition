package pt.ist.socialsoftware.edition.ldod.domain;

import static org.junit.Assert.assertEquals;
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
	TwitterCitation twitterCitation;
	@Mock
	Tweet tweet;

	@Override
	public void populate4Test() {
	}

	@Test
	public void getIdTest() {
		doCallRealMethod().when(this.twitterCitation).getId();

		when(this.twitterCitation.getTweetID()).thenReturn(1111l);

		this.twitterCitation.getId();
		verify(this.twitterCitation, times(1)).getTweetID();
		assertEquals(1111l, this.twitterCitation.getId());
	}

	@Test
	public void getNumberOfRetweetsTest() {
		doCallRealMethod().when(this.twitterCitation).getNumberOfRetweets();

		Set<Tweet> tweets = new HashSet<Tweet>();
		tweets.add(this.tweet);
		when(this.twitterCitation.getTweetSet()).thenReturn(tweets);

		this.twitterCitation.getNumberOfRetweets();
		verify(this.twitterCitation, times(1)).getTweetSet();
		assertEquals(0, this.twitterCitation.getNumberOfRetweets());
	}
}
