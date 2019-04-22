package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.search.Indexer;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;

import java.util.*;

public class ExpertEditionInter extends ExpertEditionInter_Base implements Comparable<ExpertEditionInter> {
    public ExpertEditionInter() {
        setNotes("");
        setVolume("");
        setSubNumber("");
    }

    @Override
    public void remove() {

        setExpertEdition(null);

        String externalId = getExternalId();

        // remove from Lucene
        List<String> externalIds = new ArrayList<>();
        externalIds.add(externalId);
        Indexer indexer = Indexer.getIndexer();
        indexer.cleanMissingHits(externalIds);

        // remove from mallet directory
        TopicModeler topicModeler = new TopicModeler();
        topicModeler.deleteFile(externalId);

        super.remove();
    }

    @Override
    public String getShortName() {
        return getExpertEdition().getEditorShortName();
    }

    @Override
    public String getTitle() {
        String fragTitle = super.getTitle();
        if (fragTitle == null || fragTitle.trim().equals("")) {
            return getFragment().getTitle();
        } else {
            return fragTitle;
        }
    }

    @Override
    public Edition.EditionType getSourceType() {
        return Edition.EditionType.EDITORIAL;
    }

    @Override
    public int compareTo(ExpertEditionInter other) {
        String myEditor = getExpertEdition().getEditor();
        String otherEditor = other.getExpertEdition().getEditor();

        if (myEditor.equals(otherEditor)) {
            return compareSameEditor(other);
        } else if (myEditor.equals(ExpertEdition.COELHO_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(ExpertEdition.COELHO_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(ExpertEdition.CUNHA_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(ExpertEdition.CUNHA_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(ExpertEdition.ZENITH_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(ExpertEdition.ZENITH_EDITION_NAME)) {
            return 1;
        } else {
            assert false : "To extend when new expert editions are include";
            return 0;
        }
    }

    public int compareSameEditor(ExpertEditionInter other) {
        int result = comparePage(other);
        if (result == 0) {
            return compareNumber(other);
        } else {
            return result;
        }
    }

    private int comparePage(ExpertEditionInter other) {
        int result = getVolume().compareTo(other.getVolume());
        if (result == 0) {
            return getStartPage() - other.getStartPage();
        } else {
            return result;
        }
    }

    private int compareNumber(ExpertEditionInter other) {
        int result = getNumber() - other.getNumber();
        if (result == 0) {
            return compareSubNumber(other);
        }
        return result;
    }

    private int compareSubNumber(ExpertEditionInter other) {
        return getSubNumber().compareTo(other.getSubNumber());
    }

    @Override
    public ScholarInter getLastUsed() {
        return this;
    }

    @Override
    public ExpertEdition getEdition() {
        return getExpertEdition();
    }

    public String getReference() {
        return Integer.toString(getNumber());
    }

    public Set<Category> getAllDepthCategories() {
        return new HashSet<>();
    }

    public Set<HumanAnnotation> getAllDepthHumanAnnotations() {
        return new HashSet<>();
    }

    //solução para suportar os dois tipos de annotations
    public Set<Annotation> getAllDepthAnnotations() {
        return new HashSet<>();
    }


    public Set<Tag> getAllDepthTags() {
        return new HashSet<>();
    }

    public String getCompleteNumber() {
        return Integer.toString(getNumber()) + (!getSubNumber().equals("") ? "-" + getSubNumber() : getSubNumber());
    }

    public int getUsesDepth() {
        return 0;
    }

    public ExpertEditionInter getNextNumberInter() {
        List<ExpertEditionInter> interps = new ArrayList<>(this.getExpertEdition().getIntersSet());

        Collections.sort(interps);

        return findNextElementByNumber(interps);
    }

    public ExpertEditionInter getPrevNumberInter() {
        List<ExpertEditionInter> interps = new ArrayList<>(this.getExpertEdition().getIntersSet());

        Collections.sort(interps, Collections.reverseOrder());

        return findNextElementByNumber(interps);
    }


    private ExpertEditionInter findNextElementByNumber(List<ExpertEditionInter> interps) {
        Boolean stopNext = false;
        for (ExpertEditionInter tmpInter : interps) {
            if (stopNext) {
                return tmpInter;
            }
            if (tmpInter.getNumber() == getNumber() && tmpInter == this) {
                stopNext = true;
            }
        }
        return interps.get(0);
    }
}
