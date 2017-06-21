package pt.ist.socialsoftware.edition.loaders;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Member;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.shared.exception.LdoDLoadException;

public class VirtualEditionsTEICorpusImport {

	public void importVirtualEditionsCorpus(InputStream inputStream) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		Document doc;
		try {
			doc = builder.build(inputStream);
		} catch (FileNotFoundException e) {
			throw new LdoDLoadException("Ficheiro não encontrado");
		} catch (JDOMException e) {
			throw new LdoDLoadException("Ficheiro com problemas de codificação TEI");
		} catch (IOException e) {
			throw new LdoDLoadException("Problemas com o ficheiro, tipo ou formato");
		}

		if (doc == null) {
			LdoDLoadException ex = new LdoDLoadException("Ficheiro inexistente ou sem formato TEI");
			throw ex;
		}

		processImport(doc);
	}

	public void importVirtualEditionsCorpus(String corpusTEI) {
		SAXBuilder builder = new SAXBuilder();
		builder.setIgnoringElementContentWhitespace(true);

		InputStream stream = new ByteArrayInputStream(corpusTEI.getBytes());

		importVirtualEditionsCorpus(stream);
	}

	@Atomic(mode = TxMode.WRITE)
	public void processImport(Document doc) {
		LdoD ldoD = LdoD.getInstance();

		importVirtualEditions(doc, ldoD);

		importTaxonomies(doc, ldoD);
	}

	private void importVirtualEditions(Document doc, LdoD ldoD) {
		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:bibl", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		for (Element bibl : xp.evaluate(doc)) {
			VirtualEdition virtualEdition = null;

			boolean pub = bibl.getAttributeValue("status").equals("PUBLIC") ? true : false;
			String acronym = bibl.getChildText("id", namespace);
			String title = bibl.getChildText("title", namespace);
			LocalDate date = LocalDate.parse(bibl.getChild("date", namespace).getAttributeValue("when"));

			LdoDUser owner = null;
			for (Element editor : bibl.getChildren("editor", namespace)) {
				if (editor.getAttributeValue("role").equals("ADMIN")) {
					owner = ldoD.getUser(editor.getAttributeValue("nymRef"));
					// if a virtual edition exists with the same name, it is
					// deleted
					virtualEdition = ldoD.getVirtualEdition(acronym);
					if (virtualEdition != null) {
						virtualEdition.remove();
						virtualEdition = null;
					}
					virtualEdition = ldoD.createVirtualEdition(owner, acronym, title, date, pub, null);
					break;
				}
			}

			for (Element editor : bibl.getChildren("editor", namespace)) {
				LdoDUser user = ldoD.getUser(editor.getAttributeValue("nymRef"));
				Member.MemberRole role = Member.MemberRole.valueOf(editor.getAttributeValue("role"));
				boolean active = editor.getAttributeValue("active").equals("true") ? true : false;

				Member member = null;
				if (user == owner) {
					member = virtualEdition.getMember(user);
				} else {
					member = new Member(virtualEdition, user, role, active);
				}

				date = LocalDate.parse(editor.getAttributeValue("date"));
				member.setDate(date);
			}
		}
	}

	private void importTaxonomies(Document doc, LdoD ldoD) {
		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:taxonomy", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		for (Element tax : xp.evaluate(doc)) {
			String xmlId = tax.getAttributeValue("corresp").substring(1);
			Taxonomy taxonomy = LdoD.getInstance().getVirtualEditionByXmlId(xmlId).getTaxonomy();

			for (Element item : tax.getChild("desc", namespace).getChild("list", namespace).getChildren("item",
					namespace)) {
				switch (item.getText()) {
				case "OPEN_MANAGEMENT":
					taxonomy.setOpenManagement(true);
					break;
				case "OPEN_ANNOTATION":
					taxonomy.setOpenAnnotation(true);
					break;
				case "CLOSED_VOCABULARY":
					taxonomy.setOpenVocabulary(false);
					break;
				default:
					break;
				}
			}

			for (Element cat : tax.getChildren("category", namespace)) {
				new Category().init(taxonomy, cat.getChildText("catDesc", namespace));
			}
		}
	}

}
