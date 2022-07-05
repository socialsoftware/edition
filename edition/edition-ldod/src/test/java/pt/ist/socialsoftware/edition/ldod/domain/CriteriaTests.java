package pt.ist.socialsoftware.edition.ldod.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@ExtendWith(MockitoExtension.class)
public class CriteriaTests extends TestWithFragmentsLoading {

	// utilizado no mock test para testar se todos os métodos são chamados
	@Mock
	SocialMediaCriteria criteria;
	// utilizado nos restantes testes de sucesso e de exceção
	MediaSource mediaSource;

	VirtualEdition virtualEdition;
	// rule: Class cannot be mocked
	Class<?> clazz;

	private LdoDUser user;

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = new String[0];

		return fragments;
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void populate4Test() {
		LdoD ldoD = LdoD.getInstance();
		this.user = new LdoDUser(ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		ExpertEdition expertEdition = ldoD.getRZEdition();

		this.virtualEdition = new VirtualEdition(ldoD, this.user, "acronym", "title", localDate, true, expertEdition);
		this.clazz = pt.ist.socialsoftware.edition.ldod.domain.MediaSource.class;
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void initMockTest() {
		doCallRealMethod().when(this.criteria).init(any(), any());

		this.criteria.init(this.virtualEdition, this.clazz);
		verify(this.criteria, times(1)).checkUniqueCriteriaType(this.virtualEdition, this.clazz);
		verify(this.criteria, times(1)).setVirtualEdition(this.virtualEdition);
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void initSuccessTest() {
		this.mediaSource = new MediaSource(this.virtualEdition, "Twitter");
		assertEquals(1, this.virtualEdition.getCriteriaSet().size());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void initWithOneCriterionTest() {
		this.mediaSource = new MediaSource(this.virtualEdition, "Twitter");
		new TimeWindow(this.virtualEdition, LocalDate.parse("20018-07-20"), LocalDate.parse("20018-07-25"));
		assertEquals(2, this.virtualEdition.getCriteriaSet().size());
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void checkUniqueWithRepeatedCriteriaTest() {
		this.mediaSource = new MediaSource(this.virtualEdition, "Twitter");
		Executable codeToTest = () -> {
			this.mediaSource.checkUniqueCriteriaType(this.virtualEdition, this.clazz);
		};

		assertThrows(LdoDException.class, codeToTest, "assert message");
	}

	// método experimental
	@Test
	@Atomic(mode = TxMode.WRITE)
	void testExpectedException() {
		Assertions.assertThrows(NumberFormatException.class, () -> {
			Integer.parseInt("One");
		});
	}

}
