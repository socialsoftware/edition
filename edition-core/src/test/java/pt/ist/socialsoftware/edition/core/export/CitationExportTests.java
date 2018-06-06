package pt.ist.socialsoftware.edition.core.export;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import pt.ist.socialsoftware.edition.core.MockitoExtension;
import pt.ist.socialsoftware.edition.core.RollbackCaseTest;
import pt.ist.socialsoftware.edition.core.domain.Citation;

@ExtendWith(MockitoExtension.class)
// @RunWith(JUnitPlatform.class)
public class CitationExportTests extends RollbackCaseTest {
	private static final String SOURCE_LINK = "sourceLink";
	@Mock
	Citation citation;

	@Override
	public void populate4Test() {
	}

	@Test
	public void test() {
		when(citation.getSourceLink()).thenReturn(SOURCE_LINK);

		VirtualEditionFragmentsTEIExport exporter = new VirtualEditionFragmentsTEIExport();
		Element citationList = new Element("citationList");
		exporter.exportCitation(citationList, citation);

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		String result = xml.outputString(citationList);

		// assertTrue(result.contains("<citation type=\"twitter\">"));
		assertTrue(result.contains("<sourceLink>" + SOURCE_LINK + "</sourceLink>"));
	}

	// @Test
	// public void canUpdateTest() {
	// doCallRealMethod().when(this.annotation).canUpdate(any());
	//
	// when(this.annotation.getVirtualEditionInter()).thenReturn(this.inter);
	// when(this.inter.getVirtualEdition()).thenReturn(this.virtualEdition);
	// Set<LdoDUser> users = new HashSet<LdoDUser>();
	// users.add(this.user);
	// when(this.virtualEdition.getParticipantSet()).thenReturn(users);
	// when(this.annotation.getUser()).thenReturn(this.user);
	//
	// assertTrue(this.annotation.canUpdate(this.user));
	// }

}
