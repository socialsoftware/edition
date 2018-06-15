package pt.ist.socialsoftware.edition.ldod.export;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.Member;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;

public class VirtualEditionsTEICorpusExport {
	private static final String ED_VIRT = "ED.VIRT";

	Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

	Document jdomDoc = null;

	@Atomic
	public String export() {
		this.jdomDoc = new Document();
		Element rootElement = new Element("teiCorpus");
		rootElement.setNamespace(this.xmlns);
		this.jdomDoc.setRootElement(rootElement);
		Element teiHeader = new Element("teiHeader", this.xmlns);
		teiHeader.setAttribute("type", "corpus");
		rootElement.addContent(teiHeader);

		Element listBibl = generateFileDesc(teiHeader);
		for (VirtualEdition virtualEdition : LdoD.getInstance().getVirtualEditionsSet()) {
			exportVirtualEditionBibl(listBibl, virtualEdition);
		}

		Element classDecl = generateEncodingDesc(teiHeader);
		for (VirtualEdition virtualEdition : LdoD.getInstance().getVirtualEditionsSet()) {
			exportVirtualEditionTaxonomy(classDecl, virtualEdition);
		}

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		// System.out.println(xml.outputString(rootElement));

		return xml.outputString(rootElement);
	}

	private Element generateFileDesc(Element teiHeader) {
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
		Attribute id = new Attribute("id", virtualEdition.getAcronym(), Namespace.XML_NAMESPACE);
		bibl.setAttribute(id);
		bibl.setAttribute("status", virtualEdition.getPub() ? "PUBLIC" : "PRIVATE");

		Element title = new Element("title", this.xmlns);
		title.setText(StringEscapeUtils.unescapeHtml(virtualEdition.getTitle()));
		bibl.addContent(title);

		Element synopsis = new Element("synopsis", this.xmlns);
		synopsis.setText(StringEscapeUtils.unescapeHtml(virtualEdition.getSynopsis()));
		bibl.addContent(synopsis);

		Element date = new Element("date", this.xmlns);
		date.setAttribute("when", virtualEdition.getDate().toString());
		bibl.addContent(date);

		exportEditors(bibl, virtualEdition);

		element.addContent(bibl);
	}

	private void exportEditors(Element element, VirtualEdition virtualEdition) {
		for (Member member : virtualEdition.getMemberSet()) {
			exportEditor(element, member);
		}
	}

	private void exportEditor(Element element, Member member) {
		Element editorElement = new Element("editor", this.xmlns);
		editorElement.setAttribute("nymRef", member.getUser().getUsername());
		editorElement.setAttribute("role", member.getRole().toString());
		editorElement.setAttribute("active", exportBoolean(member.getActive()));
		element.addContent(editorElement);

		Element persNameElement = new Element("persName", this.xmlns);
		persNameElement.setText(member.getUser().getFirstName() + " " + member.getUser().getLastName());
		editorElement.addContent(persNameElement);

		Element date = new Element("date", this.xmlns);
		date.setAttribute("when", member.getDate().toString());
		editorElement.addContent(date);

	}

	private Element generateEncodingDesc(Element teiHeader) {
		Element encodingDesc = new Element("encodingDesc", this.xmlns);
		teiHeader.addContent(encodingDesc);
		Element classDecl = new Element("classDecl", this.xmlns);
		encodingDesc.addContent(classDecl);
		return classDecl;
	}

	private void exportVirtualEditionTaxonomy(Element element, VirtualEdition virtualEdition) {
		Taxonomy taxonomy = virtualEdition.getTaxonomy();

		Element taxonomyElement = new Element("taxonomy", this.xmlns);
		taxonomyElement.setAttribute("source", "#" + ED_VIRT + "." + virtualEdition.getAcronym());

		Element taxonomyDesc = new Element("desc", this.xmlns);
		taxonomyElement.addContent(taxonomyDesc);

		Element descList = new Element("list", this.xmlns);
		descList.setAttribute("type", "interaction");
		taxonomyDesc.addContent(descList);

		Element listItem = new Element("item", this.xmlns);
		listItem.setText(taxonomy.getOpenManagement() ? "OPEN_MANAGEMENT" : "CLOSED_MANAGEMENT");
		descList.addContent(listItem);

		listItem = new Element("item", this.xmlns);
		listItem.setText(taxonomy.getOpenAnnotation() ? "OPEN_ANNOTATION" : "CLOSED_ANNOTATION");
		descList.addContent(listItem);

		listItem = new Element("item", this.xmlns);
		listItem.setText(taxonomy.getOpenVocabulary() ? "OPEN_VOCABULARY" : "CLOSED_VOCABULARY");
		descList.addContent(listItem);

		for (Category category : taxonomy.getCategoriesSet()) {
			exportCategory(taxonomyElement, category);
		}

		element.addContent(taxonomyElement);
	}

	private void exportCategory(Element element, Category category) {
		Element categoryElement = new Element("category", this.xmlns);
		Attribute id = new Attribute("id", category.getXmlId(), Namespace.XML_NAMESPACE);
		categoryElement.setAttribute(id);
		element.addContent(categoryElement);

		Element catDescElement = new Element("catDesc", this.xmlns);
		catDescElement.setText(category.getName());

		categoryElement.addContent(catDescElement);

	}

	private String exportBoolean(boolean value) {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}

}
