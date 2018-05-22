package pt.ist.socialsoftware.edition.core.domain;

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

import pt.ist.socialsoftware.edition.core.MockitoExtension;

@ExtendWith(MockitoExtension.class)
// @RunWith(JUnitPlatform.class)
public class AnnotationTests extends RollbackCaseTest {
	@Mock
	VirtualEdition virtualEdition;
	@Mock
	VirtualEditionInter inter;
	@Mock
	LdoDUser user;
	@Mock
	SimpleText startText, endText;
	@Mock
	Annotation annotation;

	@Override
	public void populate4Test() {
	}

	@Test
	public void update() {
		doCallRealMethod().when(this.annotation).update(any(), any());
		when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);

		this.annotation.update("text", new ArrayList<String>());

		verify(this.annotation, times(1)).setText("text");
		verify(this.annotation, times(1)).getVirtualEditionInter();
	}

	@Test
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
