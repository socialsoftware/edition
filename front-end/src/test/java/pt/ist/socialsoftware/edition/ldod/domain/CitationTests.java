package pt.ist.socialsoftware.edition.ldod.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.TweetDto;
import pt.ist.socialsoftware.edition.notification.dtos.virtual.TwitterCitationDto;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CitationTests extends TestWithFragmentsLoading {
	@Mock
	TwitterCitationDto twitterCitation;
	@Mock
	TweetDto tweet;

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabase();
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = new String[0];

		return fragments;
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getIdTest() {
		doCallRealMethod().when(this.twitterCitation).getId();

		when(this.twitterCitation.getTweetId()).thenReturn(1111l);

		this.twitterCitation.getId();
		verify(this.twitterCitation, times(1)).getTweetId();
		assertEquals(1111l, this.twitterCitation.getId());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void getNumberOfRetweetsTest() {
		doCallRealMethod().when(this.twitterCitation).getNumberOfRetweets();

		Set<TweetDto> tweets = new HashSet<TweetDto>();
		tweets.add(this.tweet);
		when(this.twitterCitation.getTweets()).thenReturn(tweets);

		this.twitterCitation.getNumberOfRetweets();
		verify(this.twitterCitation, times(1)).getTweets();
		assertEquals(0, this.twitterCitation.getNumberOfRetweets());
	}
}
