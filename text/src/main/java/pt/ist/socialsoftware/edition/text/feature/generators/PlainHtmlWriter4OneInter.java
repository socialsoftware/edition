package pt.ist.socialsoftware.edition.text.feature.generators;

import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.text.domain.SpaceText.SpaceDim;
import pt.ist.socialsoftware.edition.text.domain.ScholarInter;
import pt.ist.socialsoftware.edition.text.domain.TextModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlainHtmlWriter4OneInter implements TextPortionVisitor {
    protected ScholarInter fragInter = null;
    protected String transcription = "";

    private void append2Transcription(String generated) {
        if (this.generate) {
            this.transcription = this.transcription + generated;
        }
    }

    protected Boolean highlightDiff = false;
    protected Boolean displayDel = false;
    protected Boolean highlightIns = true;
    protected Boolean highlightSubst = false;
    protected Boolean showNotes = true;

    private boolean generate = true;
    private PbText startPbText = null;
    private PbText stopPbText = null;

    private final Map<ScholarInter, Integer> interpsChar = new HashMap<>();
    private int totalChar = 0;

    public String getTranscription() {
        return this.transcription;
    }

    public Integer getInterPercentage(ScholarInter inter) {
        return (this.interpsChar.get(inter) * 100) / this.totalChar;
    }

    public PlainHtmlWriter4OneInter(ScholarInter fragInter) {
        this.fragInter = fragInter;
        this.transcription = "";

        for (ScholarInter inter : fragInter.getFragment().getScholarInterSet()) {
            this.interpsChar.put(inter, 0);
        }
    }

    public PlainHtmlWriter4OneInter(String scholarInterXmlId) {
        this(TextModule.getInstance().getScholarInterByXmlId(scholarInterXmlId));
    }

    public void write(Boolean highlightDiff) {
        this.highlightDiff = highlightDiff;
        visit((AppText) this.fragInter.getFragment().getTextPortion());
    }

    public void write(Boolean highlightDiff, Boolean displayDel, Boolean highlightIns, Boolean highlightSubst,
                      Boolean showNotes, Boolean showFacs, PbText pbText) {
        this.highlightDiff = highlightDiff;
        this.displayDel = displayDel;
        this.highlightIns = highlightIns;
        this.highlightSubst = highlightSubst;
        this.showNotes = showNotes;

        if (showFacs) {

            this.startPbText = pbText;
            if (this.startPbText != null) {
                this.generate = false;
            }

            this.stopPbText = ((SourceInter) this.fragInter).getNextPbText(this.startPbText);

        }

        visit((AppText) this.fragInter.getFragment().getTextPortion());
    }

    @Override
    public void visit(AppText appText) {
        propagate2FirstChild(appText);

        propagate2NextSibling(appText);
    }

    @Override
    public void visit(RdgGrpText rdgGrpText) {
        if (rdgGrpText.getInterps().contains(this.fragInter)) {
            propagate2FirstChild(rdgGrpText);
        }

        propagate2NextSibling(rdgGrpText);
    }

    @Override
    public void visit(RdgText rdgText) {
        if (rdgText.getInterps().contains(this.fragInter)) {

            Boolean color = false;
            if (this.highlightDiff) {
                int size = this.fragInter.getFragment().getScholarInterSet().size();
                if (rdgText.getInterps().size() < size) {
                    color = true;
                    int colorValue = 255 - (255 / size) * (size - rdgText.getInterps().size() - 1);
                    String colorCode = "<span style=\"background-color: rgb(0," + colorValue + ",255);\">";

                    append2Transcription(rdgText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter) + colorCode);
                }
            }

            if (!color) {
                append2Transcription(rdgText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter));
            }

            propagate2FirstChild(rdgText);

            if (color) {
                append2Transcription("</span>");
            }
        }

        propagate2NextSibling(rdgText);
    }

    @Override
    public void visit(ParagraphText paragraphText) {
        // append2Transcription("<p align=\"justify\">");
        append2Transcription("<p class=\"text-xs-left text-sm-justify text-md-justify text-lg-justify\">");

        propagate2FirstChild(paragraphText);

        append2Transcription("</p>");

        propagate2NextSibling(paragraphText);
    }

    @Override
    public void visit(SegText segText) {
        List<Rend> renditions = new ArrayList<>(segText.getRendSet());
        String preRend = generatePreRendition(renditions);
        String postRend = generatePostRendition(renditions);

        String altRend = "";
        if (segText.getAltTextWeight() != null) {
            altRend = "<span class=\"text-warning\">" + "<abbr title=\""
                    + segText.getAltTextWeight().getAltText().getMode().getDesc() + " "
                    + segText.getAltTextWeight().getWeight() + "\">";
        }

        append2Transcription(segText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter) + preRend + altRend);

        propagate2FirstChild(segText);

        if (segText.getAltTextWeight() != null) {
            altRend = "</abbr></span>";
        }

        append2Transcription(altRend + postRend);

        propagate2NextSibling(segText);
    }

    @Override
    public void visit(AltText altText) {
        // do nothing, the segTextOne and segTextTwo will do
        propagate2NextSibling(altText);
    }

    @Override
    public void visit(SimpleText simpleText) {
        String value = simpleText.getValue();

        this.totalChar = this.totalChar + value.length();
        for (ScholarInter inter : simpleText.getInterps()) {
            Integer number = this.interpsChar.get(inter);
            number = number + value.length();
            this.interpsChar.put(inter, number);
        }

        append2Transcription(simpleText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter) + value);

        propagate2NextSibling(simpleText);
    }

    @Override
    public void visit(LbText lbText) {
        if (lbText.getInterps().contains(this.fragInter)) {
            String hyphen = "";
            if (lbText.getHyphenated()) {
                hyphen = "-";
            }

            append2Transcription(hyphen + "<br>");
        }

        propagate2NextSibling(lbText);
    }

    @Override
    public void visit(PbText pbText) {
        if (pbText.getInterps().contains(this.fragInter)) {
            if ((this.startPbText != pbText) && (this.stopPbText != pbText)) {
                append2Transcription("<hr size=\"8\" color=\"black\">");
            }
        }

        if (this.startPbText == pbText) {
            this.generate = true;
        }

        if (this.stopPbText == pbText) {
            this.generate = false;
        }

        propagate2NextSibling(pbText);
    }

    @Override
    public void visit(SpaceText spaceText) {
        String separator = "";
        if (spaceText.getDim() == SpaceDim.VERTICAL) {
            separator = "<br>";
            // the initial line break is for a new line
            append2Transcription(separator);
        } else if (spaceText.getDim() == SpaceDim.HORIZONTAL) {
            separator = "&nbsp; ";
        }

        for (int i = 0; i < spaceText.getQuantity(); i++) {
            append2Transcription(separator);
        }

        propagate2NextSibling(spaceText);
    }

    @Override
    public void visit(AddText addText) {
        List<Rend> renditions = new ArrayList<>(addText.getRendSet());
        String preRendition = generatePreRendition(renditions);
        String postRendition = generatePostRendition(renditions);

        String prePlaceFormat = "";
        String postPlaceFormat = "";
        switch (addText.getPlace()) {
            case INLINE:
            case INSPACE:
            case OVERLEAF:
            case SUPERIMPOSED:
            case MARGIN:
            case OPPOSITE:
            case BOTTOM:
            case END:
            case UNSPECIFIED:
                prePlaceFormat = "<small>";
                postPlaceFormat = "</small>";
                break;
            case ABOVE:
            case TOP:
                prePlaceFormat = "<span style=\"position:relative; top:-3px;\">";
                postPlaceFormat = "</span>";
                // prePlaceFormat = "<sup>";
                // postPlaceFormat = "</sup>";
                break;
            case BELOW:
                prePlaceFormat = "<span style=\"position:relative; top:3px;\">";
                postPlaceFormat = "</span>";
                // prePlaceFormat = "<sub>";
                // postPlaceFormat = "</sub>";
                break;
        }

        if (this.highlightIns) {
            String insertSymbol = "<span style=\"color: rgb(128,128,128);\"><small>&and;</small></span>";
            if (this.showNotes) {
                insertSymbol = "<abbr title=\"" + addText.getNote() + "\">" + insertSymbol + "</abbr>";
            }

            append2Transcription(addText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter) + preRendition
                    + prePlaceFormat + insertSymbol);
        } else {
            append2Transcription(addText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter));
        }

        propagate2FirstChild(addText);

        if (this.highlightIns) {
            append2Transcription(postPlaceFormat + postRendition);
        }

        propagate2NextSibling(addText);
    }

    @Override
    public void visit(DelText delText) {
        if (this.displayDel) {
            append2Transcription(delText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter)
                    + "<del><span style=\"color: rgb(128,128,128);\">");
            if (this.showNotes) {
                append2Transcription("<abbr title=\"" + delText.getNote() + "\">");
            }

            propagate2FirstChild(delText);

            if (this.showNotes) {
                append2Transcription("</abbr>");
            }

            append2Transcription("</span></del>");
        }

        propagate2NextSibling(delText);
    }

    @Override
    public void visit(SubstText substText) {
        if (this.displayDel && this.highlightSubst) {
            append2Transcription(substText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter)
                    + "<span style=\"color: rgb(0,0,255);\">[</span>");
        }

        propagate2FirstChild(substText);

        if (this.displayDel && this.highlightSubst) {
            append2Transcription("<span style=\"color: rgb(0,0,255);\">]" + "<sub>subst</sub></span>");
        }

        propagate2NextSibling(substText);
    }

    @Override
    public void visit(GapText gapText) {
        String gapValue = gapText.getGapValue();

        this.totalChar = this.totalChar + gapValue.length();
        for (ScholarInter inter : gapText.getInterps()) {
            Integer number = this.interpsChar.get(inter);
            number = number + gapValue.length();
            this.interpsChar.put(inter, number);
        }

        append2Transcription(gapText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter) + "<abbr title=\""
                + gapText.getReason().getDesc() + ", " + gapText.getExtent() + " " + gapText.getUnit() + "\">"
                + gapValue + "</abbr>");

        propagate2NextSibling(gapText);
    }

    @Override
    public void visit(UnclearText unclearText) {
        append2Transcription(unclearText.writeSeparator(this.displayDel, this.highlightSubst, this.fragInter)
                + "<span style=\"text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);\">"
                + "<abbr title=\"" + unclearText.getReason().getDesc() + "\">");

        propagate2FirstChild(unclearText);

        append2Transcription("</abbr>" + "</span>");

        propagate2NextSibling(unclearText);
    }

    @Override
    public void visit(NoteText noteText) {
        append2Transcription("<abbr title=\"");

        propagate2FirstChild(noteText);

        int number = 0;
        for (AnnexNote annexNote : noteText.getAnnexNoteSet()) {
            if (annexNote.getScholarInter() == this.fragInter) {
                number = annexNote.getNumber();
            }
        }

        append2Transcription("\">(" + number + ")</abbr>");

        propagate2NextSibling(noteText);

    }

    @Override
    public void visit(RefText refText) {
        propagate2FirstChild(refText);
        propagate2NextSibling(refText);
    }

    private String generatePreRendition(List<Rend> renditions) {
        String preRend = "";
        for (Rend rend : renditions) {
            // the order matters
            if (rend.getRend() == Rend.Rendition.RIGHT) {
                preRend = "<div class=\"text-right\">" + preRend;
            } else if (rend.getRend() == Rend.Rendition.LEFT) {
                preRend = "<div class=\"text-left\">" + preRend;
            } else if (rend.getRend() == Rend.Rendition.CENTER) {
                preRend = "<div class=\"text-center\">" + preRend;
            } else if (rend.getRend() == Rend.Rendition.BOLD) {
                preRend = preRend + "<strong>";
            } else if (rend.getRend() == Rend.Rendition.ITALIC) {
                preRend = preRend + "<em>";
            } else if (rend.getRend() == Rend.Rendition.RED) {
                preRend = preRend + "<span style=\"color: rgb(255,0,0);\">";
            } else if (rend.getRend() == Rend.Rendition.GREEN) {
                preRend = preRend + "<span style=\"color: rgb(0,255,0);\">";
            } else if (rend.getRend() == Rend.Rendition.UNDERLINED) {
                preRend = preRend + "<u>";
            } else if (rend.getRend() == Rend.Rendition.SUPERSCRIPT) {
                preRend = preRend + "<sup>";
            } else if (rend.getRend() == Rend.Rendition.SUBSCRIPT) {
                preRend = preRend + "<sub>";
            }
        }
        return preRend;
    }

    private String generatePostRendition(List<Rend> renditions) {
        String postRend = "";
        for (Rend rend : renditions) {
            if (rend.getRend() == Rend.Rendition.RIGHT) {
                postRend = postRend + "</div>";
            } else if (rend.getRend() == Rend.Rendition.LEFT) {
                postRend = postRend + "</div>";
            } else if (rend.getRend() == Rend.Rendition.CENTER) {
                postRend = postRend + "</div>";
            } else if (rend.getRend() == Rend.Rendition.BOLD) {
                postRend = "</strong>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.ITALIC) {
                postRend = "</em>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.RED) {
                postRend = "</span>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.GREEN) {
                postRend = "</span>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.UNDERLINED) {
                postRend = "</u>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.SUPERSCRIPT) {
                postRend = "</sup>" + postRend;
            } else if (rend.getRend() == Rend.Rendition.SUBSCRIPT) {
                postRend = "</sub>" + postRend;
            }
        }
        return postRend;
    }

}