package pt.ist.socialsoftware.edition.text.feature.generators;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.text.domain.*;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.text.domain.ScholarInter;

import java.util.*;
import java.util.stream.Collectors;

public class HtmlWriter2CompInters implements TextPortionVisitor {
    private final Map<ScholarInter, String> transcriptionsMap = new HashMap<>();
    private final Map<ScholarInter, Integer> transcriptionsLengthMap = new HashMap<>();
    private final List<ScholarInter> interps;

    private Boolean lineByLine = false;
    private Boolean showSpaces = false;

    public Boolean getShowSpaces() {
        return this.showSpaces;
    }

    private String lineByLineTranscription = "";

    public HtmlWriter2CompInters(List<ScholarInterDto> interps) {
        this.interps = interps.stream()
                .map(scholarInterDto -> (ScholarInter) FenixFramework.getDomainObject(scholarInterDto.getExternalId()))
                .collect(Collectors.toList());
    }

    public HtmlWriter2CompInters(List<String> externalIds, boolean optional) {
        this.interps = externalIds.stream()
                .map(externalId -> (ScholarInter) FenixFramework.getDomainObject(externalId))
                .collect(Collectors.toList());
    }

    public void write(Boolean lineByLine, Boolean showSpaces) {
        this.lineByLine = lineByLine;
        this.showSpaces = showSpaces;

        for (ScholarInter inter : this.interps) {
            this.transcriptionsMap.put(inter, "");
            this.transcriptionsLengthMap.put(inter, 0);
        }

        visit((AppText) this.interps.iterator().next().getFragment().getTextPortion());
    }

    public String getTranscription(ScholarInterDto interDto) {
        return getTranscription((ScholarInter) FenixFramework.getDomainObject(interDto.getExternalId()));
    }

    public String getTranscription(String externalId) {
        return getTranscription((ScholarInter) FenixFramework.getDomainObject(externalId));
    }

    public String getTranscription(ScholarInter inter) {
        return this.transcriptionsMap.get(inter);
    }

    public String getTranscriptionLineByLine() {
        // add the last line
        for (ScholarInter inter : this.interps) {
            this.lineByLineTranscription = this.lineByLineTranscription + this.transcriptionsMap.get(inter) + "<br>";
            this.transcriptionsMap.put(inter, "");
            this.transcriptionsLengthMap.put(inter, 0);
        }

        return this.lineByLineTranscription;
    }

    @Override
    public void visit(AppText appText) {
        alignSpaces(appText);

        generateLineByLine(appText);

        propagate2FirstChild(appText);

        alignSpaces(appText);

        propagate2NextSibling(appText);
    }

    private void generateLineByLine(AppText appText) {
        if (this.lineByLine && (appText.getInterps().containsAll(this.interps))) {
            int lineLength = 66;

            int longestLength = 0;
            for (ScholarInter inter : this.interps) {
                longestLength = Math.max(longestLength, this.transcriptionsLengthMap.get(inter));
            }

            if (longestLength >= lineLength) {
                for (ScholarInter inter : this.interps) {
                    this.lineByLineTranscription = this.lineByLineTranscription + this.transcriptionsMap.get(inter) + "<br>";
                    this.transcriptionsMap.put(inter, "");
                    this.transcriptionsLengthMap.put(inter, 0);
                }
            }
        }
    }

    private void alignSpaces(AppText appText) {
        if (this.showSpaces) {
            Set<ScholarInter> appInterps = new HashSet<>(this.interps);
            appInterps.retainAll(appText.getInterps());

            int longestLength = 0;
            for (ScholarInter inter : appInterps) {
                longestLength = Math.max(longestLength, this.transcriptionsLengthMap.get(inter));
            }

            for (ScholarInter inter : appInterps) {
                String addSpace = "";
                int diff = longestLength - this.transcriptionsLengthMap.get(inter);
                for (int i = 0; i < diff; i++) {
                    addSpace = addSpace + "&nbsp;";
                }
                String newTranscription = this.transcriptionsMap.get(inter) + addSpace;

                this.transcriptionsMap.put(inter, newTranscription);
                this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + diff);
            }
        }
    }

    @Override
    public void visit(RdgGrpText rdgGrpText) {
        propagate2FirstChild(rdgGrpText);

        propagate2NextSibling(rdgGrpText);
    }

    @Override
    public void visit(RdgText rdgText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(rdgText.getInterps());

        if (!intersection.isEmpty()) {

            Boolean color = false;

            int size = this.interps.size();
            String colorCode = "";
            if (intersection.size() < size) {
                color = true;
                int colorValue = 255 - (255 / size) * (size - intersection.size() - 1);
                colorCode = "<span style=\"background-color: rgb(0," + colorValue + ",255);\">";
            }

            for (ScholarInter inter : intersection) {
                String separator = rdgText.writeSeparator(true, false, inter);

                String newTranscription = this.transcriptionsMap.get(inter) + separator;

                if (color) {
                    newTranscription = newTranscription + colorCode;
                }

                this.transcriptionsMap.put(inter, newTranscription);
                this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + separator.length());
            }

            propagate2FirstChild(rdgText);

            if (color) {
                for (ScholarInter inter : intersection) {
                    String newTranscription = this.transcriptionsMap.get(inter) + "</span>";
                    this.transcriptionsMap.put(inter, newTranscription);
                }
            }

        }

        propagate2NextSibling(rdgText);
    }

    @Override
    public void visit(ParagraphText paragraphText) {
        propagate2FirstChild(paragraphText);

        propagate2NextSibling(paragraphText);
    }

    @Override
    public void visit(SegText segText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(segText.getInterps());

        for (ScholarInter inter : intersection) {
            String separator = segText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator;
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + separator.length());
        }

        propagate2FirstChild(segText);

        propagate2NextSibling(segText);
    }

    @Override
    public void visit(SimpleText simpleText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(simpleText.getInterps());

        String value = simpleText.getValue();

        for (ScholarInter inter : intersection) {
            String separator = simpleText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator + value;
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter,
                    this.transcriptionsLengthMap.get(inter) + value.length() + separator.length());
        }

        propagate2NextSibling(simpleText);
    }

    @Override
    public void visit(LbText lbText) {
        propagate2NextSibling(lbText);
    }

    @Override
    public void visit(PbText pbText) {
        propagate2NextSibling(pbText);
    }

    @Override
    public void visit(SpaceText spaceText) {
        propagate2NextSibling(spaceText);
    }

    @Override
    public void visit(AddText addText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(addText.getInterps());

        for (ScholarInter inter : intersection) {
            String separator = addText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator + "<ins>";
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + separator.length());
        }

        propagate2FirstChild(addText);

        for (ScholarInter inter : intersection) {
            String newTranscription = this.transcriptionsMap.get(inter) + "</ins>";
            this.transcriptionsMap.put(inter, newTranscription);
        }

        propagate2NextSibling(addText);
    }

    @Override
    public void visit(DelText delText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(delText.getInterps());

        for (ScholarInter inter : intersection) {
            String separator = delText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator + "<del>";
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + separator.length());
        }

        propagate2FirstChild(delText);

        for (ScholarInter inter : intersection) {
            String newTranscription = this.transcriptionsMap.get(inter) + "</del>";
            this.transcriptionsMap.put(inter, newTranscription);
        }

        propagate2NextSibling(delText);
    }

    @Override
    public void visit(SubstText substText) {
        propagate2FirstChild(substText);

        propagate2NextSibling(substText);
    }

    @Override
    public void visit(GapText gapText) {
        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(gapText.getInterps());

        String value = gapText.getGapValue();

        for (ScholarInter inter : intersection) {
            String separator = gapText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator + value;
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter,
                    this.transcriptionsLengthMap.get(inter) + value.length() + separator.length());
        }

        propagate2NextSibling(gapText);
    }

    @Override
    public void visit(UnclearText unclearText) {

        Set<ScholarInter> intersection = new HashSet<>(this.interps);
        intersection.retainAll(unclearText.getInterps());

        for (ScholarInter inter : intersection) {
            String separator = unclearText.writeSeparator(true, false, inter);
            String newTranscription = this.transcriptionsMap.get(inter) + separator
                    + "<span style=\"text-shadow: black 0.0em 0.0em 0.1em; -webkit-filter: blur(0.005em);\">";
            this.transcriptionsMap.put(inter, newTranscription);
            this.transcriptionsLengthMap.put(inter, this.transcriptionsLengthMap.get(inter) + separator.length());
        }

        propagate2FirstChild(unclearText);

        for (ScholarInter inter : intersection) {
            String newTranscription = this.transcriptionsMap.get(inter) + "</span>";
            this.transcriptionsMap.put(inter, newTranscription);
        }

        propagate2NextSibling(unclearText);
    }

    @Override
    public void visit(AltText altText) {
        propagate2NextSibling(altText);
    }

    @Override
    public void visit(NoteText noteText) {
        propagate2NextSibling(noteText);
    }

    @Override
    public void visit(RefText refText) {
        propagate2FirstChild(refText);
        propagate2NextSibling(refText);
    }

}
