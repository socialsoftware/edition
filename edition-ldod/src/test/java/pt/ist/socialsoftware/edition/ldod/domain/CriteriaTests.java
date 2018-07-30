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

import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.RollbackCaseTest;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@ExtendWith(MockitoExtension.class)
public class CriteriaTests extends RollbackCaseTest {

	// utilizado no mock test para testar se todos os métodos são chamados
	@Mock
	SocialMediaCriteria criteria;
	// utilizado nos restantes testes de sucesso e de exceção
	MediaSource mediaSource;

	VirtualEdition virtualEdition;
	// rule: Class cannot be mocked
	Class<?> clazz;

	// regra: não se pode passar mocks a construtores!
	@Override
	public void populate4Test() {
		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = new LdoDUser(ldoD, "ars1", "ars", "Antonio", "Silva", "a@a.a");
		LocalDate localDate = LocalDate.parse("20018-07-20");
		ExpertEdition expertEdition = ldoD.getRZEdition();

		this.virtualEdition = new VirtualEdition(ldoD, user, "acronym", "title", localDate, true, expertEdition);
		clazz = pt.ist.socialsoftware.edition.ldod.domain.MediaSource.class;
	}

	@Test
	public void initMockTest() {
		doCallRealMethod().when(this.criteria).init(any(), any());

		this.criteria.init(virtualEdition, clazz);
		verify(this.criteria, times(1)).checkUniqueCriteriaType(virtualEdition, clazz);
		verify(this.criteria, times(1)).setVirtualEdition(virtualEdition);
	}

	@Test
	public void initSuccessTest() {
		this.mediaSource = new MediaSource(virtualEdition, "Twitter");
		assertEquals(1, virtualEdition.getCriteriaSet().size());
	}

	@Test
	public void initWithOneCriterionTest() {
		this.mediaSource = new MediaSource(virtualEdition, "Twitter");
		new TimeWindow(virtualEdition, LocalDate.parse("20018-07-20"), LocalDate.parse("20018-07-25"));
		assertEquals(2, virtualEdition.getCriteriaSet().size());
	}

	@Test
	public void checkUniqueWithRepeatedCriteriaTest() {
		this.mediaSource = new MediaSource(virtualEdition, "Twitter");
		Executable codeToTest = () -> {
			this.mediaSource.checkUniqueCriteriaType(virtualEdition, clazz);
		};

		assertThrows(LdoDException.class, codeToTest, "assert message");
	}

	// método experimental
	@Test
	void testExpectedException() {
		Assertions.assertThrows(NumberFormatException.class, () -> {
			Integer.parseInt("One");
		});
	}

}
