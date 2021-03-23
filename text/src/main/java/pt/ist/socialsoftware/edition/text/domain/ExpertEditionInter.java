package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.feature.indexer.Indexer;
import pt.ist.socialsoftware.edition.text.feature.topicmodelling.TopicModeler;
//import pt.ist.socialsoftware.edition.ldod.virtual.feature.topicmodeling.TopicModeler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public boolean isExpertInter() {
        return true;
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

    @Override
    public String getReference() {
        return Integer.toString(getNumber());
    }

    public String getCompleteNumber() {
        return Integer.toString(getNumber()) + (!getSubNumber().equals("") ? "-" + getSubNumber() : getSubNumber());
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
