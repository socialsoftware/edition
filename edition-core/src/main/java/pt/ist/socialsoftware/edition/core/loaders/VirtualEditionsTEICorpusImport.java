package pt.ist.socialsoftware.edition.core.loaders;

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
import pt.ist.socialsoftware.edition.core.domain.Category;
import pt.ist.socialsoftware.edition.core.domain.Frequency;
import pt.ist.socialsoftware.edition.core.domain.GeographicLocation;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.LdoDUser;
import pt.ist.socialsoftware.edition.core.domain.MediaSource;
import pt.ist.socialsoftware.edition.core.domain.Member;
import pt.ist.socialsoftware.edition.core.domain.Taxonomy;
import pt.ist.socialsoftware.edition.core.domain.TimeWindow;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.shared.exception.LdoDLoadException;

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

		importSocialMediaCriteria(doc, ldoD);

		// TODO suggestion
		importTweets(doc, ldoD);// here??
	}

	// TODO:
	private void importTweets(Document doc, LdoD ldoD) {

	}

	private void importVirtualEditions(Document doc, LdoD ldoD) {
		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:bibl", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		for (Element bibl : xp.evaluate(doc)) {
			VirtualEdition virtualEdition = null;

			boolean pub = bibl.getAttributeValue("status").equals("PUBLIC") ? true : false;
			String acronym = bibl.getAttributeValue("id", Namespace.XML_NAMESPACE);
			String title = bibl.getChildText("title", namespace);
			String synopsis = bibl.getChildText("synopsis", namespace);
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
					virtualEdition.setSynopsis(synopsis);
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

				date = LocalDate.parse(editor.getChild("date", namespace).getAttributeValue("when"));
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
			String xmlId = tax.getAttributeValue("source").substring(1);
			System.out.println(xmlId);
			System.out.println(LdoD.getInstance().getVirtualEditionByXmlId(xmlId));
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

	private void importSocialMediaCriteria(Document doc, LdoD ldoD) {
		Namespace namespace = doc.getRootElement().getNamespace();
		XPathFactory xpfac = XPathFactory.instance();
		XPathExpression<Element> xp = xpfac.compile("//def:editionCriteria", Filters.element(), null,
				Namespace.getNamespace("def", namespace.getURI()));
		for (Element editionCriteria : xp.evaluate(doc)) {
			String xmlId = editionCriteria.getAttributeValue("source").substring(1);
			System.out.println(xmlId);
			System.out.println(LdoD.getInstance().getVirtualEditionByXmlId(xmlId));

			VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEditionByXmlId(xmlId);

			Element mediaSource = editionCriteria.getChild("mediaSource", namespace);
			if (mediaSource != null) {
				new MediaSource(virtualEdition, mediaSource.getAttributeValue("name"));
			}

			Element timeWindow = editionCriteria.getChild("timeWindow", namespace);
			if (timeWindow != null) {
				new TimeWindow(virtualEdition, LocalDate.parse(timeWindow.getAttributeValue("beginDate")),
						LocalDate.parse(timeWindow.getAttributeValue("endDate")));
			}

			Element geographicLocation = editionCriteria.getChild("geographicLocation", namespace);
			if (geographicLocation != null) {
				new GeographicLocation(virtualEdition, geographicLocation.getAttributeValue("country"),
						geographicLocation.getAttributeValue("location"));
			}

			Element frequency = editionCriteria.getChild("frequency", namespace);
			if (frequency != null) {
				new Frequency(virtualEdition, Integer.parseInt(frequency.getAttributeValue("frequency")));
			}

		}
	}

}
