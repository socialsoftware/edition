package pt.ist.socialsoftware.edition.export;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Member;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class VirtualEditionXMLExport {
	@Atomic
	public String export() {

		Element element = createHeader();

		for (VirtualEdition virtualEdition : LdoD.getInstance().getVirtualEditionsSet()) {
			exportVirtualEdition(element, virtualEdition);
		}

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		// System.out.println(xml.outputString(element));

		return xml.outputString(element);
	}

	private Element createHeader() {
		Document jdomDoc = new Document();
		Element rootElement = new Element("virtual-edition-list");

		jdomDoc.setRootElement(rootElement);
		return rootElement;
	}

	private void exportVirtualEdition(Element element, VirtualEdition virtualEdition) {
		Element veElement = new Element("virtual-edition");
		veElement.setAttribute("acronym", virtualEdition.getAcronym());
		veElement.setAttribute("title", virtualEdition.getTitle());
		veElement.setAttribute("pub", exportBoolean(virtualEdition.getPub()));
		veElement.setAttribute("date", virtualEdition.getDate().toString());

		exportParticipants(veElement, virtualEdition);
		exportTaxonomy(veElement, virtualEdition.getTaxonomy());
		// exportInterpretations()

		element.addContent(veElement);
	}

	private void exportParticipants(Element element, VirtualEdition virtualEdition) {
		Element participantListElement = new Element("participant-list");

		for (Member member : virtualEdition.getMemberSet()) {
			exportParticipant(participantListElement, member);
		}

		element.addContent(participantListElement);
	}

	private void exportParticipant(Element element, Member member) {
		Element participantElement = new Element("participant");
		participantElement.setAttribute("user", member.getUser().getUsername());
		participantElement.setAttribute("role", member.getRole().toString());
		participantElement.setAttribute("active", exportBoolean(member.getActive()));

		element.addContent(participantElement);
	}

	private void exportTaxonomy(Element element, Taxonomy taxonomy) {
		Element taxonomyElement = new Element("taxonomy");

		for (Category category : taxonomy.getCategoriesSet()) {
			exportCategory(taxonomyElement, category);
		}

		element.addContent(taxonomyElement);
	}

	private void exportCategory(Element element, Category category) {
		Element categoryElement = new Element("category");
		categoryElement.setAttribute("name", category.getName());

		element.addContent(categoryElement);
	}

	private String exportBoolean(boolean value) {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}

}
