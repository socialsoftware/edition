package pt.ist.socialsoftware.edition.ldod.social.aware;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@ExtendWith(MockitoExtension.class)
public class FetchFromTwitterTests extends TestWithFragmentsLoading {

	FetchCitationsFromTwitter fetchFromTwitter;
	Twitter twitter;

	@Mock
	FetchCitationsFromTwitter fetchMock;
	@Mock
	Twitter twitterMock;
	// status tem de ser mocked pq não é possível
	// invocar o construtor ou setters
	@Mock
	Status status;

	public void logger(Object toPrint) {
		System.out.println(toPrint);
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = new String[0];

		return fragments;
	}

	@Override
	public void populate4Test() {
		this.fetchFromTwitter = new FetchCitationsFromTwitter();
		this.twitter = this.fetchFromTwitter.getTwitterinstance();
	}

	@Override
	protected void unpopulate4Test() {
		// LdoD.getInstance().getCitationSet().forEach(c -> c.remove());
	}

	// invocações sobre o stauts dão null pointer exception
	// apesar de estar mocked ... TODO falar com o professor
	// @Test
	@Atomic
	public void getTweetInfoInStringFormatMockTest() {
		doCallRealMethod().when(this.fetchFromTwitter).getTweetInfoInStringFormat(any());

		this.fetchFromTwitter.getTweetInfoInStringFormat(this.status);
		verify(this.status, times(1)).getPlace();
	}

	// @Test
	@Atomic
	public void fetchMockTest() throws IOException, TwitterException {
		doCallRealMethod().when(this.fetchFromTwitter).fetch();

		this.fetchFromTwitter.fetch();
		verify(this.fetchFromTwitter, times(1)).getTwitterinstance();
		verify(this.twitter, times(1)).getRateLimitStatus("search");
	}

	// TODO: invocaçõe sobre fetchMock dão null pointer exception ...
	// @Test
	@Atomic
	public void getTweetByIdMockTest() throws TwitterException {
		doCallRealMethod().when(this.fetchMock).getTweetById(any(), any());

		this.fetchMock.getTweetById(1019702700102111233l, this.twitterMock);
		verify(this.twitterMock, times(1)).showStatus(1019702700102111233l);
	}

	@Test
	public void fetchTest() throws IOException {
		// fetchFromTwitter.fetch();
	}

	@Test
	@Atomic
	public void getTweetInfoInStringFormatTest() {
		Status s = this.fetchFromTwitter.getTweetById(1019702700102111233l, this.twitter);
		String toAssert = "\t At " + new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(s.getCreatedAt()) + ", @"
				+ s.getUser().getScreenName() + " (id: " + s.getId() + ")" + "\n" + "said: " + s.getText() + "\n"
				+ "country: " + "unknown" + "\n" + "location: " + "mg" + "\n" + "tweet URL: " + "https://twitter.com/"
				+ s.getUser().getScreenName() + "/status/" + s.getId() + "\n" + "profile URL: " + "https://twitter.com/"
				+ s.getUser().getScreenName() + "\n" + "profile Picture: " + s.getUser().getBiggerProfileImageURL()
				+ "\n" + "isRetweet: " + s.isRetweet() + "\n" + "isRetweeted: " + s.isRetweeted() + "\n"
				+ "retweetCount: " + s.getRetweetCount() + "\n" + "originalTweetID: " + -1L + "\n"
				+ "currentUserRetID: " + -1L + "\n" + "############################" + "\n";
		assertEquals(toAssert, this.fetchFromTwitter.getTweetInfoInStringFormat(s));
	}

	@Test
	@Atomic
	public void createTermsMapTest() {
		Map<String, String> termsMap = this.fetchFromTwitter.createTermsMap();
		assertEquals(4, termsMap.size());
		assertEquals("livro", termsMap.get("Livro do Desassossego"));
		assertEquals("fp", termsMap.get("Fernando Pessoa"));
		assertEquals("bernardo", termsMap.get("Bernardo Soares"));
		assertEquals("vicente", termsMap.get("Vicente Guedes"));
	}

	// ID: 1019702700102111233
	@Test
	@Atomic
	public void getTweetByIdTest() {
		Status s = this.fetchFromTwitter.getTweetById(1019702700102111233l, this.twitter);

		assertEquals("18-Jul-2018 22:57:23", new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US).format(s.getCreatedAt()));
		assertNull(s.getPlace());

		// TODO: o URL da imagem parece que mudou ...
		// logger(s.getUser().getBiggerProfileImageURL());
		// assertEquals("http://pbs.twimg.com/profile_images/1022968372697423872/A_7zQH36_bigger.jpg",
		// s.getUser().getBiggerProfileImageURL());
		assertEquals(1019702700102111233l, s.getId());
		assertNotNull(s.getUser().getScreenName());
		assertFalse(s.isRetweet());
		assertNull(s.getRetweetedStatus());
		assertEquals("mg", s.getUser().getLocation());
		// logger(s.getText());
		assertEquals(
				"\u201CSe um dia amasse, não seria amado. Basta eu querer uma coisa para ela morrer. O meu destino, porém, tem a força de ser mortal para qualquer coisa. Tem a fraqueza de ser mortal nas coisas para mim\u201D \n\nLivro do Desassossego",
				s.getText());
	}

}
