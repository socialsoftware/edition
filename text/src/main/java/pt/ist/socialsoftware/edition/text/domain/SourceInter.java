package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.text.domain.Source.SourceType;

public class SourceInter extends SourceInter_Base implements Comparable<SourceInter> {
    private static final Logger logger = LoggerFactory.getLogger(SourceInter.class);

    public SourceInter() {
    }

    @Override
    public Heteronym getHeteronym() {
        return getSource().getHeteronym();
    }

    @Override
    public String getShortName() {
        return getSource().getName();
    }

    @Override
    public String getTitle() {
        return getFragment().getTitle();
    }

    @Override
    public LdoDDate getLdoDDate() {
        if (getSource() == null) {
            return null;
        }
        return getSource().getLdoDDate();
    }

    @Override
    public boolean isExpertInter() {
        return false;
    }

    @Override
    public int compareTo(SourceInter other) {
        SourceType thisType = getSource().getType();
        SourceType otherType = other.getSource().getType();

        if (thisType.equals(SourceType.MANUSCRIPT) && otherType.equals(SourceType.MANUSCRIPT)) {
            boolean thisIsManuscript = !((ManuscriptSource) this.getSource()).getHandNoteSet().isEmpty();
            boolean thisIsDactiloscript = !((ManuscriptSource) this.getSource()).getTypeNoteSet().isEmpty();
            boolean otherIsManuscript = !((ManuscriptSource) other.getSource()).getHandNoteSet().isEmpty();
            boolean otherIsDactiloscript = !((ManuscriptSource) other.getSource()).getTypeNoteSet().isEmpty();

            if (thisIsDactiloscript && otherIsDactiloscript) {
                return getShortName().compareTo(other.getShortName());
            }

            if (thisIsManuscript && otherIsManuscript) {
                return getShortName().compareTo(other.getShortName());
            }

            if (thisIsManuscript) {
                return -1;
            } else {
                // dactiloscript
                return 1;
            }
        }

        if (thisType.equals(SourceType.PRINTED) && otherType.equals(SourceType.PRINTED)) {
            return getShortName().compareTo(other.getShortName());
        }

        if (thisType.equals(SourceType.MANUSCRIPT)) {
            return -1;
        } else {
            // printed
            return 1;
        }
    }

    @Override
    public void remove() {
        setSource(null);

        super.remove();
    }

    @Override
    public int getNumber() {
        return 0;
    }


    @Override
    public ScholarInter getLastUsed() {
        return this;
    }

    @Override
    public ScholarEdition getEdition() {
        return TextModule.getInstance().getNullEdition();
    }

    @Override
    public String getReference() {
        return getShortName();
    }

    public Surface getPrevSurface(PbText pbText) {
        if (pbText == null) {
            return null;
        } else {
            PbText prevPbText = pbText.getPrevPbText(this);
            if (prevPbText == null) {
                return getSource().getFacsimile().getFirstSurface();
            } else {
                return prevPbText.getSurface();
            }
        }
    }

    public Surface getNextSurface(PbText pbText) {
        if (pbText == null) {
            if (getFirstPbText() == null) {
                return null;
            } else {
                return getFirstPbText().getSurface();
            }
        } else {
            PbText nextPbText = pbText.getNextPbText(this);
            if (nextPbText == null) {
                return null;
            } else {
                return nextPbText.getSurface();
            }
        }
    }

    private PbText getFirstPbText() {
        PbText firstPbText = null;
        for (PbText pbText : getPbTextSet()) {
            if ((firstPbText == null) || (firstPbText.getOrder() > pbText.getOrder())) {
                firstPbText = pbText;
            }
        }
        return firstPbText;
    }

    public PbText getPrevPbText(PbText pbText) {
        if (pbText == null) {
            return null;
        } else {
            return pbText.getPrevPbText(this);
        }
    }

    public PbText getNextPbText(PbText pbText) {
        if (pbText == null) {
            return getFirstPbText();
        } else {
            return pbText.getNextPbText(this);
        }
    }

}
