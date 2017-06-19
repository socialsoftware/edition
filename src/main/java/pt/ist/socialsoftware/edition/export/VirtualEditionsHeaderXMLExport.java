package pt.ist.socialsoftware.edition.export;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Member;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;

public class VirtualEditionsHeaderXMLExport {
	private static final String ED_VIRT = "ED.VIRT";

	Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

	Document jdomDoc = null;

	@Atomic
	public String export() {
		this.jdomDoc = new Document();
		Element rootElement = new Element("teiCorpus");
		rootElement.setNamespace(this.xmlns);
		this.jdomDoc.setRootElement(rootElement);

		Element listBibl = generateTeiHeader(rootElement);

		for (VirtualEdition virtualEdition : LdoD.getInstance().getVirtualEditionsSet()) {
			exportVirtualEditionBibl(listBibl, virtualEdition);
		}

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		// System.out.println(xml.outputString(rootElement));

		return xml.outputString(rootElement);
	}

	public Element generateTeiHeader(Element rootElement) {
		Element teiHeader = new Element("teiHeader", this.xmlns);
		teiHeader.setAttribute("type", "corpus");
		rootElement.addContent(teiHeader);
		Element fileDesc = new Element("fileDesc", this.xmlns);
		teiHeader.addContent(fileDesc);
		Element sourceDesc = new Element("sourceDesc", this.xmlns);
		fileDesc.addContent(sourceDesc);
		Element listBibl = new Element("listBibl", this.xmlns);
		Attribute id = new Attribute("id", ED_VIRT, Namespace.XML_NAMESPACE);
		listBibl.setAttribute(id);
		sourceDesc.addContent(listBibl);
		return listBibl;
	}

	private void exportVirtualEditionBibl(Element element, VirtualEdition virtualEdition) {
		Element bibl = new Element("bibl", this.xmlns);
		Attribute id = new Attribute("id", ED_VIRT + "." + virtualEdition.getAcronym(), Namespace.XML_NAMESPACE);
		bibl.setAttribute(id);

		Element idElement = new Element("id", this.xmlns);
		idElement.setText(virtualEdition.getAcronym());
		bibl.addContent(idElement);

		Element title = new Element("title", this.xmlns);
		title.setText(virtualEdition.getTitle());
		bibl.addContent(title);

		Element date = new Element("date", this.xmlns);
		date.setAttribute("when", virtualEdition.getDate().toString());
		bibl.addContent(date);

		bibl.setAttribute("pub", exportBoolean(virtualEdition.getPub()));

		exportEditors(bibl, virtualEdition);
		exportTaxonomy(bibl, virtualEdition.getTaxonomy());

		element.addContent(bibl);
	}

	private void exportEditors(Element element, VirtualEdition virtualEdition) {
		Element editorsElement = new Element("editor");

		for (Member member : virtualEdition.getMemberSet()) {
			exportEditor(editorsElement, member);
		}

		element.addContent(editorsElement);
	}

	private void exportEditor(Element element, Member member) {
		Element editorElement = new Element("persName");
		editorElement.setAttribute("user", member.getUser().getUsername());
		editorElement.setAttribute("role", member.getRole().toString());
		editorElement.setAttribute("active", exportBoolean(member.getActive()));

		element.addContent(editorElement);
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
