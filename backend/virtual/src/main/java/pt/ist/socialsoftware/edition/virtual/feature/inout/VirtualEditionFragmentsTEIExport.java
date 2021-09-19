package pt.ist.socialsoftware.edition.virtual.feature.inout;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.notification.dtos.text.FragmentDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.domain.*;


public class VirtualEditionFragmentsTEIExport {
    Namespace xmlns = Namespace.getNamespace("http://www.tei-c.org/ns/1.0");

    Document jdomDoc = null;

    @Atomic
    public void export() {



        for (FragmentDto fragmentDto : VirtualRequiresInterface.getInstance().getFragmentDtoSet()) {
            exportFragment(fragmentDto.getXmlId());
        }
    }

    @Atomic
    public String exportFragment(String fragmentXmlId) {
        this.jdomDoc = new Document();
        Element rootElement = new Element("teiCorpus");
        rootElement.setNamespace(this.xmlns);
        this.jdomDoc.setRootElement(rootElement);
        Element tei = new Element("TEI", this.xmlns);
        Attribute id = new Attribute("id", fragmentXmlId, Namespace.XML_NAMESPACE);
        tei.setAttribute(id);
        rootElement.addContent(tei);
        Element teiHeader = new Element("teiHeader", this.xmlns);
        teiHeader.setAttribute("type", "text");
        tei.addContent(teiHeader);
        Element fileDesc = new Element("fileDesc", this.xmlns);
        teiHeader.addContent(fileDesc);
        Element sourceDesc = new Element("sourceDesc", this.xmlns);
        fileDesc.addContent(sourceDesc);

        Element witnesses = new Element("listWit", this.xmlns);
        id = new Attribute("id", fragmentXmlId + ".WIT.ED.VIRT", Namespace.XML_NAMESPACE);
        witnesses.setAttribute(id);
        sourceDesc.addContent(witnesses);
        for (VirtualEditionInter virtualEditionInter : VirtualModule.getInstance().getVirtualEditionInterSet(fragmentXmlId)) {
            exportVirtualEditionInterWitness(witnesses, virtualEditionInter);
        }

        Element profileDesc = new Element("profileDesc", this.xmlns);
        teiHeader.addContent(profileDesc);
        for (VirtualEditionInter virtualEditionInter : VirtualModule.getInstance().getVirtualEditionInterSet(fragmentXmlId)) {
            Element textClass = new Element("textClass", this.xmlns);
            textClass.setAttribute("source", "#" + virtualEditionInter.getXmlId());
            profileDesc.addContent(textClass);

            exportVirtualEditionInterTags(textClass, virtualEditionInter);
            exportVirtualEditionInterAnnotations(textClass, virtualEditionInter);
            //exportClassificationGames(textClass, virtualEditionInter);
        }

        // NOT NECESSARY THEY CAN BE LOADED FROM THE FILES
        //exportFragmentCitations(teiHeader, fragment);

        XMLOutputter xml = new XMLOutputter();
        xml.setFormat(Format.getPrettyFormat());
        // System.out.println(xml.outputString(rootElement));

        return xml.outputString(rootElement);
    }

    private void exportVirtualEditionInterWitness(Element witnesses, VirtualEditionInter virtualEditionInter) {
        Element witness = new Element("witness", this.xmlns);
        Attribute id = new Attribute("id", virtualEditionInter.getXmlId(), Namespace.XML_NAMESPACE);
        witness.setAttribute(id);
        witness.setAttribute("source", "#" + (virtualEditionInter.getUses() != null ? virtualEditionInter.getUses().getXmlId() : virtualEditionInter.getUsesScholarInterId()));
        witnesses.addContent(witness);

        Element number = new Element("num", this.xmlns);
        number.setAttribute("value", Integer.toString(virtualEditionInter.getNumber()));
        witness.addContent(number);
    }

    private void exportVirtualEditionInterTags(Element textClass, VirtualEditionInter virtualEditionInter) {
        for (Tag tag : virtualEditionInter.getTagSet()) {
            if (tag.getAnnotation() == null) {
                Element catRef = new Element("catRef", this.xmlns);
                catRef.setAttribute("scheme", "#" + virtualEditionInter.getEdition().getXmlId());
                catRef.setAttribute("target", "#" + tag.getCategory().getName());
                catRef.setAttribute("resp", "#" + tag.getContributor());
                textClass.addContent(catRef);
            }
        }
    }

//	private void exportFragmentCitations(Element teiHeader, Fragment fragment) {
//		Element citationList = new Element("citationList", this.xmlns);
//		teiHeader.addContent(citationList);
//		for (Citation citation : fragment.getCitationSet()) {
//			Element citationElement = new Element("citation", this.xmlns);
//			citationList.addContent(citationElement);
//
//			exportCitation(citationElement, citation);
//			if (citation instanceof TwitterCitation) {
//				exportTwitterCitation(citationElement, (TwitterCitation) citation);
//			}
//			exportInfoRanges(citationElement, citation);
//		}
//	}

//	private void exportInfoRanges(Element citationElement, Citation citation) {
//		Element infoRangesList = new Element("infoRangesList", this.xmlns);
//		citationElement.addContent(infoRangesList);
//
//		for (InfoRange infoRange : citation.getInfoRangeSet()) {
//			Element infoRangeElement = new Element("infoRange", this.xmlns);
//			infoRangesList.addContent(infoRangeElement);
//
//			infoRangeElement.setAttribute("start", infoRange.getStart());
//			infoRangeElement.setAttribute("startOffset", Integer.toString(infoRange.getStartOffset()));
//			infoRangeElement.setAttribute("end", infoRange.getEnd());
//			infoRangeElement.setAttribute("endOffset", Integer.toString(infoRange.getEndOffset()));
//
//			Element quoteElement = new Element("quote", this.xmlns);
//			quoteElement.addContent(infoRange.getQuote());
//			infoRangeElement.addContent(quoteElement);
//
//			Element textElement = new Element("text", this.xmlns);
//			textElement.addContent(infoRange.getTextModule());
//			infoRangeElement.addContent(textElement);
//
//			// discutir também utilidade destes atributos
//			// fragInterXmlId seria necessário para descobrir o fragInter ao importar
//			infoRangeElement.setAttribute("fragInterXmlId", infoRange.getFragInterDto().getXmlId());
//			infoRangeElement.setAttribute("citationId", Long.toString(infoRange.getCitation().getId()));
//		}
//	}

//	protected void exportCitation(Element citationElement, Citation citation) {
//		citationElement.setAttribute("sourceLink", citation.getSourceLink());
//		citationElement.setAttribute("date", citation.getDate());
//		// citationElement.setAttribute("fragText", citation.getFragText());
//
//		Element fragText = new Element("fragText", this.xmlns);
//		fragText.addContent(citation.getFragText());
//		citationElement.addContent(fragText);
//
//		// old code
//		// Element citationElement = new Element("citation", this.xmlns);
//		// citationList.addContent(citationElement);
//	}
//
//	protected void exportTwitterCitation(Element citationElement, TwitterCitation citation) {
//		citationElement.setAttribute("type", "twitter");
//
//		Element tweetText = new Element("tweetText", this.xmlns);
//		tweetText.addContent(new CDATA(citation.getTweetText()));
//		citationElement.addContent(tweetText);
//
//		citationElement.setAttribute("tweetId", Long.toString(citation.getTweetID()));
//		citationElement.setAttribute("location", citation.getLocation());
//		citationElement.setAttribute("country", citation.getCountry());
//		citationElement.setAttribute("username", citation.getUsername());
//		citationElement.setAttribute("userProfileURL", citation.getUserProfileURL());
//		citationElement.setAttribute("userImageURL", citation.getUserProfileURL());
//
//		Element tweets = new Element("tweets", this.xmlns);
//		citationElement.addContent(tweets);
//
//		for (Tweet tweet : citation.getTweetSet()) {
//			Element tweetElement = new Element("tweet", this.xmlns);
//			tweets.addContent(tweetElement);
//
//			// acho q o set é este em vez do q fiz inicialmente com o if e else
//			tweetElement.setAttribute("tweetId", Long.toString(tweet.getTweetID()));
//
//			// if (tweet.getIsRetweet()) {
//			// tweetElement.setAttribute("tweetId",
//			// Long.toString(tweet.getOriginalTweetID()));
//			// } else {
//			// tweetElement.setAttribute("tweetId", Long.toString(tweet.getTweetID()));
//			// }
//		}
//	}

    private void exportVirtualEditionInterAnnotations(Element textClass, VirtualEditionInter virtualEditionInter) {
        for (Annotation annotation : virtualEditionInter.getAnnotationSet()) {
            if (annotation instanceof HumanAnnotation) {
                Element note = new Element("note", this.xmlns);

                note.setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
                textClass.addContent(note);

                exportAnnotationRanges(annotation, note);

                note.setAttribute("resp", "#" + annotation.getUser());
                note.setAttribute("type", "human");
                exportAnnotationCategories(virtualEditionInter, (HumanAnnotation) annotation, note);
            } else if (annotation instanceof AwareAnnotation) {
//                note.setAttribute("type", "aware");
//                note.setAttribute("citationId", Long.toString(((AwareAnnotation) annotation).getCitation().getId()));
            }
        }
    }

    private void exportAnnotationCategories(VirtualEditionInter virtualEditionInter, HumanAnnotation annotation,
                                            Element note) {
        for (Category category : annotation.getCategories()) {
            Element catRef = new Element("catRef", this.xmlns);
            catRef.setAttribute("scheme", "#" + virtualEditionInter.getEdition().getXmlId());
            catRef.setAttribute("target", "#" + category.getName());
            note.addContent(catRef);
        }
    }

    private void exportAnnotationRanges(Annotation annotation, Element note) {
        for (Range range : annotation.getRangeSet()) {
            Element quote = new Element("quote", this.xmlns);
            quote.setAttribute("from", range.getStart());
            quote.setAttribute("to", range.getEnd());
            quote.setAttribute("fromOffset", Integer.toString(range.getStartOffset()));
            quote.setAttribute("toOffset", Integer.toString(range.getEndOffset()));
            quote.setText(annotation.getQuote());
            note.addContent(quote);
        }
    }


    //TODO : these class game related functions should be moved to their own exporter in the game module
   /* private void exportClassificationGames(Element textClass, VirtualEditionInter virtualEditionInter) {
        Element classificationGameList = new Element("classificationGameList", this.xmlns);
        textClass.addContent(classificationGameList);

        for (ClassificationGame game : ClassificationModule.getInstance().getClassificationGamesForInter(virtualEditionInter.getXmlId())) {
            Element gameElement = new Element("classificationGame", this.xmlns);
            gameElement.setAttribute("state", game.getState().toString());
            gameElement.setAttribute("description", game.getDescription());
            gameElement.setAttribute("dateTime", String.valueOf(game.getDateTime()));
            gameElement.setAttribute("sync", Boolean.toString(game.getSync()));
            gameElement.setAttribute("responsible", game.getResponsible());
            if (game.getTagId() != null) {
                gameElement.setAttribute("tag", virtualEditionInter.getTagSet()
                        .stream().filter(tag -> tag.getCategory().getUrlId().equals(game.getTagId())).findAny().orElseThrow(LdoDException::new).getCategory().getName());
            }
            ClassificationGameParticipant participant = game.getClassificationGameParticipantSet().stream()
                    .filter(ClassificationGameParticipant::getWinner).findFirst().orElse(null);
            gameElement.setAttribute("winningUser",
                    participant != null ? participant.getPlayer().getUser() : " ");

            exportClassificationGameRounds(gameElement, game);
            exportClassificationGameParticipants(gameElement, game);
            classificationGameList.addContent(gameElement);
        }

    }

    private void exportClassificationGameRounds(Element gameElement, ClassificationGame game) {
        Element classificationRoundList = new Element("classificationGameRoundList", this.xmlns);

        game.getAllRounds().forEach(round -> {
            Element roundElement = new Element("classificationGameRound", this.xmlns);
            roundElement.setAttribute("paragraphNumber", Integer.toString(round.getNumber()));
            roundElement.setAttribute("roundNumber", Integer.toString(round.getRound()));
            roundElement.setAttribute("tag", round.getTag());
            roundElement.setAttribute("vote", Double.toString(round.getVote()));
            roundElement.setAttribute("dateTime", String.valueOf(round.getTime()));
            roundElement.setAttribute("username",
                    round.getClassificationGameParticipant().getPlayer().getUser());
            classificationRoundList.addContent(roundElement);
        });
        gameElement.addContent(classificationRoundList);
    }

    private void exportClassificationGameParticipants(Element element, ClassificationGame game) {
        Element classificationParticipantList = new Element("classificationGameParticipantList", this.xmlns);

        for (ClassificationGameParticipant participant : game.getClassificationGameParticipantSet()) {
            Element participantElement = new Element("classificationGameParticipant", this.xmlns);
            participantElement.setAttribute("username", participant.getPlayer().getUser());
            participantElement.setAttribute("winner", Boolean.toString(participant.getWinner()));
            participantElement.setAttribute("score", Double.toString(participant.getScore()));
            classificationParticipantList.addContent(participantElement);
        }

        element.addContent(classificationParticipantList);
    }*/
}
