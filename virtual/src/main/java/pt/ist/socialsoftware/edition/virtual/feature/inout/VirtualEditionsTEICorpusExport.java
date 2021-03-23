package pt.ist.socialsoftware.edition.virtual.feature.inout;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.virtual.domain.*;

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


        // NOT NECESSARY THEY CAN BE LOADED FROM THE FILES
//		Element tweetList = generateTweetList(teiHeader);
//		for (Tweet tweet : VirtualModule.getInstance().getTweetSet()) {
//			exportTweet(tweetList, tweet);
//		}


        Element listPlayer = generatePlayerList(teiHeader);
        /*for (Player player : ClassificationModule.getInstance().getPlayerSet()) { //TODO: this should be exported through the game module
            exportPlayer(listPlayer, player);
        }*/

        Element listBibl = generateFileDesc(teiHeader);
        for (VirtualEdition virtualEdition : VirtualModule.getInstance().getVirtualEditionsSet()) {
            exportVirtualEditionBibl(listBibl, virtualEdition);
        }

        Element classDecl = generateEncodingDesc(teiHeader);
        for (VirtualEdition virtualEdition : VirtualModule.getInstance().getVirtualEditionsSet()) {
            exportVirtualEditionTaxonomy(classDecl, virtualEdition);
        }

        Element socialMediaCriteria = generateSocialMediaCriteria(teiHeader);
        for (VirtualEdition virtualEdition : VirtualModule.getInstance().getVirtualEditionsSet()) {
            exportVirtualEditionSocialMediaCriteria(socialMediaCriteria, virtualEdition);
        }

        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        // System.out.println(xml.outputString(rootElement));

        return xml.outputString(rootElement);
    }

    // TODO: passar os element para attribute - done
//	protected void exportTweet(Element tweetList, Tweet tweet) {
//		Element tweetElement = new Element("tweet", this.xmlns);
//		tweetList.addContent(tweetElement);
//
//		tweetElement.setAttribute("sourceLink", tweet.getSourceLink());
//		tweetElement.setAttribute("date", tweet.getDate());
//
//		Element tweetText = new Element("tweetText", this.xmlns);
//		tweetText.addContent(tweet.getTweetText());
//		tweetElement.addContent(tweetText);
//
//		tweetElement.setAttribute("tweetId", Long.toString(tweet.getTweetID()));
//		try {
//			tweetElement.setAttribute("location", tweet.getLocation().replace("?", ""));
//		} catch (org.jdom2.IllegalDataException e) {
//			tweetElement.setAttribute("location", "");
//		}
//		tweetElement.setAttribute("country", tweet.getCountry());
//		tweetElement.setAttribute("username", tweet.getUsername());
//		tweetElement.setAttribute("userProfileURL", tweet.getUserProfileURL());
//		tweetElement.setAttribute("userImageURL", tweet.getUserProfileURL());
//
//		tweetElement.setAttribute("originalTweetId", Long.toString(tweet.getOriginalTweetID()));
//		tweetElement.setAttribute("isRetweet", String.valueOf(tweet.getIsRetweet()));
//
//		// TODO: discutir com o professor a utilidade deste atributo, útil apenas para
//		// debug
//		// Este atributo stressa com o teste original de export do corpus pq os tweets
//		// são exportados antes das citations, e no início os tweets têm todas as
//		// citations a null e por isso este atributo não era exportado
//		// if (tweet.getCitation() != null) {
//		// tweetElement.setAttribute("citationId",
//		// Long.toString(tweet.getCitation().getId()));
//		// }
//	}

//	protected Element generateTweetList(Element teiHeader) {
//		Element tweetList = new Element("tweetList", this.xmlns);
//		teiHeader.addContent(tweetList);
//		return tweetList;
//	}


    protected Element generatePlayerList(Element teiHeader) {
        Element playerList = new Element("playerList", this.xmlns);
        teiHeader.addContent(playerList);
        return playerList;
    }

//    private void exportPlayer(Element userElement, Player player) {
//        Element playerElement = new Element("player");
//        playerElement.setAttribute("user", player.getUser());
//        playerElement.setAttribute("score", Double.toString(player.getScore()));
//        userElement.addContent(playerElement);
//    }

    protected Element generateFileDesc(Element teiHeader) {
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

    protected void exportVirtualEditionBibl(Element element, VirtualEdition virtualEdition) {
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
        editorElement.setAttribute("nymRef", member.getUser());
        editorElement.setAttribute("role", member.getRole().toString());
        editorElement.setAttribute("active", exportBoolean(member.getActive()));
        element.addContent(editorElement);

//        Element persNameElement = new Element("persName", this.xmlns);
//        persNameElement.setText(member.getUser().getFirstName() + " " + member.getUser().getLastName());
//        editorElement.addContent(persNameElement);

        Element date = new Element("date", this.xmlns);
        date.setAttribute("when", member.getDate().toString());
        editorElement.addContent(date);

    }

    protected Element generateEncodingDesc(Element teiHeader) {
        Element encodingDesc = new Element("encodingDesc", this.xmlns);
        teiHeader.addContent(encodingDesc);
        Element classDecl = new Element("classDecl", this.xmlns);
        encodingDesc.addContent(classDecl);
        return classDecl;
    }

    protected void exportVirtualEditionTaxonomy(Element element, VirtualEdition virtualEdition) {
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

    protected Element generateSocialMediaCriteria(Element teiHeader) {
        Element socialMediaCriteria = new Element("socialMediaCriteria", this.xmlns);
        teiHeader.addContent(socialMediaCriteria);
        return socialMediaCriteria;
    }

    protected Element exportVirtualEditionSocialMediaCriteria(Element socialMediaCriteria,
                                                              VirtualEdition virtualEdition) {
        Element virtualEditionSocialCriteria = new Element("editionCriteria", this.xmlns);
        virtualEditionSocialCriteria.setAttribute("source", "#" + ED_VIRT + "." + virtualEdition.getAcronym());
        socialMediaCriteria.addContent(virtualEditionSocialCriteria);

        for (SocialMediaCriteria criteria : virtualEdition.getCriteriaSet()) {
            if (criteria instanceof MediaSource) {
                Element mediaSource = new Element("mediaSource", this.xmlns);
                mediaSource.setAttribute("name", ((MediaSource) criteria).getName());
                virtualEditionSocialCriteria.addContent(mediaSource);
            } else if (criteria instanceof TimeWindow) {
                Element timeWindow = new Element("timeWindow", this.xmlns);

                // TODO: changed because begindate and enddate cannot be null
                String beginDate = "";
                String endDate = "";
                if (((TimeWindow) criteria).getBeginDate() != null) {
                    beginDate = ((TimeWindow) criteria).getBeginDate().toString();
                }
                if (((TimeWindow) criteria).getEndDate() != null) {
                    endDate = ((TimeWindow) criteria).getEndDate().toString();
                }
                timeWindow.setAttribute("beginDate", beginDate);
                timeWindow.setAttribute("endDate", endDate);

                // old code
                // timeWindow.setAttribute("beginDate", ((TimeWindow)
                // criteria).getBeginDate().toString());
                // timeWindow.setAttribute("endDate", ((TimeWindow)
                // criteria).getEndDate().toString());

                virtualEditionSocialCriteria.addContent(timeWindow);
            } else if (criteria instanceof GeographicLocation) {
                Element geographicLocation = new Element("geographicLocation", this.xmlns);
                geographicLocation.setAttribute("country", ((GeographicLocation) criteria).getCountry());

                // TODO: changed because location cannot be null
                String location = ((GeographicLocation) criteria).getLocation();
                if (location == null) {
                    location = "";
                }
                geographicLocation.setAttribute("location", location);

                // old code
                // geographicLocation.setAttribute("location", ((GeographicLocation)
                // criteria).getLocation());

                virtualEditionSocialCriteria.addContent(geographicLocation);
            } else if (criteria instanceof Frequency) {
                Element frequency = new Element("frequency", this.xmlns);
                frequency.setAttribute("frequency", Integer.toString(((Frequency) criteria).getFrequency()));
                virtualEditionSocialCriteria.addContent(frequency);
            } else {
                assert true; // TODO: qual é o objetivo disto?
            }

        }

        return virtualEditionSocialCriteria;
    }

}
