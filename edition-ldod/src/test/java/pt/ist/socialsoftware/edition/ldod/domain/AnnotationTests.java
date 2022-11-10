package pt.ist.socialsoftware.edition.ldod.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;

@ExtendWith(MockitoExtension.class)
// @RunWith(JUnitPlatform.class)
public class AnnotationTests extends TestWithFragmentsLoading {
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

	@Override
	protected void populate4Test() {
	}

	@Override
	protected void unpopulate4Test() {
		TestLoadUtils.cleanDatabaseButCorpus();
	}

	@Override
	protected String[] fragmentsToLoad4Test() {
		String[] fragments = new String[0];

		return fragments;
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void update() {
		doCallRealMethod().when(this.annotation).update(any(), any(), null);

		when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);

		this.annotation.update("text", new ArrayList<String>(), null);
		verify(this.annotation, times(1)).setText("text");
		verify(this.annotation, times(1)).getVirtualEditionInter();
	}

	@Test
	@Atomic(mode = TxMode.WRITE)
	public void canUpdateTest() {
		doCallRealMethod().when(this.annotation).canUpdate(any());

		when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);
		when(this.inter.getVirtualEdition()).thenReturn(this.virtualEdition);
		Set<LdoDUser> users = new HashSet<LdoDUser>();
		users.add(this.user);
		when(this.virtualEdition.getParticipantSet()).thenReturn(users);
		when(this.annotation.getUser()).thenReturn(this.user);

		assertTrue(this.annotation.canUpdate(this.user));
	}

}
