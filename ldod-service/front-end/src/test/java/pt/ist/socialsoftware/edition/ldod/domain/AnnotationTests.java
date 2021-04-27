package pt.ist.socialsoftware.edition.ldod.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.MockitoExtension;
import pt.ist.socialsoftware.edition.ldod.TestLoadUtils;
import pt.ist.socialsoftware.edition.ldod.TestWithFragmentsLoading;
import pt.ist.socialsoftware.edition.ldod.frontend.user.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.virtual.domain.VirtualEditionInter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
// @RunWith(JUnitPlatform.class)
public class AnnotationTests extends TestWithFragmentsLoading {
    @Mock
    VirtualEdition virtualEdition;
    @Mock
    VirtualEditionInter inter;
    @Mock
    UserDto user;
//    @Mock
//    SimpleTextDto startText, endText;
    @Mock
    HumanAnnotation annotation;

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
    public void update() {
        doCallRealMethod().when(this.annotation).update(any(), any());

        when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);

        this.annotation.update("text", new ArrayList<>());
        verify(this.annotation, times(1)).setText("text");
        verify(this.annotation, times(1)).getVirtualEditionInter();
    }

    @Test
    @Atomic(mode = TxMode.WRITE)
    public void canUpdateTest() {
        doCallRealMethod().when(this.annotation).canUpdate(any());

        when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);
        when(this.inter.getVirtualEdition()).thenReturn(this.virtualEdition);
        Set<String> users = new HashSet<>();
        users.add(this.user.getUsername());
        System.out.println(this.user);
        when(this.virtualEdition.getParticipantSet()).thenReturn(users);
        when(this.annotation.getUser()).thenReturn(this.user.getUsername());

        assertTrue(this.annotation.canUpdate(this.user.getUsername()));
    }

}
