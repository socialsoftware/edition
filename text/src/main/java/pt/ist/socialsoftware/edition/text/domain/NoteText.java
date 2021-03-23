package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.domain.RefText.RefType;
import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

public class NoteText extends NoteText_Base {

    public enum NoteType {
        ANNEX("annex");

        private final String desc;

        NoteType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

	public NoteText(TextPortion parent, NoteType type) {
        parent.addChildText(this);

        setType(type);

        if (type == NoteType.ANNEX) {
            for (ScholarInter inter : getInterps()) {
                new AnnexNote(inter, this);
            }
        }
    }

    public NoteText(AnnexNote annexNote, NoteType type) {
        setParentText(null);
        setType(type);
        addAnnexNote(annexNote);
    }

    @Override
    public void remove() {
        for (AnnexNote annexNote : getAnnexNoteSet()) {
            annexNote.remove();
        }

        super.remove();
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

    // TODO: To consider whether a complete generation, without using the
    // visitors will be needed in the future
    public String generatePresentationText() {
        String result = "";

        TextPortion text = getFirstChildText();
        while (text != null) {
            if (text instanceof SimpleText) {
                SimpleText stext = (SimpleText) text;
                result = result + stext.getValue() + " ";
            } else if (text instanceof RefText) {
                RefText refText = (RefText) text;
                String link = "#";
                if (refText.getType() == RefType.GRAPHIC) {
                    link = "/facs/" + refText.getSurface().getGraphic();
                } else if (refText.getType() == RefType.WITNESS) {
                    link = "/fragments/fragment/" + refText.getScholarInter().getFragment().getXmlId() + "/inter/"
                            + refText.getScholarInter().getUrlId();
                } else if (refText.getType() == RefType.FRAGMENT) {
                    if (refText.getRefFrag() != null) {
                        link = "/fragments/fragment/" + refText.getRefFrag().getXmlId();
                    } else {
                        // this situation should never occur if the fragments
                        // were consistently loaded
                        link = "/fragments/fragment/" + refText.getTarget();
                    }
                }
                result = result + "<a href=\"" + link + "\">";
                SimpleText childText = (SimpleText) text.getFirstChildText();
                result = result + childText.getValue() + "</a> ";
            }

            text = text.getNextText();
        }

        return result;
    }
}
