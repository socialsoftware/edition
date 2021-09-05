package pt.ist.socialsoftware.edition.text.domain;

import org.joda.time.LocalDate;


import java.util.*;
import java.util.stream.Collectors;

public class ExpertEdition extends ExpertEdition_Base implements Comparable<ExpertEdition> {
    public static final String COELHO_EDITION_ACRONYM = "JPC";
    public static final String CUNHA_EDITION_ACRONYM = "TSC";
    public static final String ZENITH_EDITION_ACRONYM = "RZ";
    public static final String PIZARRO_EDITION_ACRONYM = "JP";
    public static final String COELHO_EDITION_NAME = "Jacinto do Prado Coelho";
    public static final String CUNHA_EDITION_NAME = "Teresa Sobral Cunha";
    public static final String ZENITH_EDITION_NAME = "Richard Zenith";
    public static final String PIZARRO_EDITION_NAME = "Jerónimo Pizarro";

    public ExpertEdition(TextModule text, String title, String author, String editor, LocalDate date) {
        setTitle(title);
        setAuthor(author);
        setEditor(editor);
        setDate(date);
        setPub(true);

        switch (editor) {
            case COELHO_EDITION_NAME:
                setAcronym(COELHO_EDITION_ACRONYM);
                break;
            case CUNHA_EDITION_NAME:
                setAcronym(CUNHA_EDITION_ACRONYM);
                break;
            case ZENITH_EDITION_NAME:
                setAcronym(ZENITH_EDITION_ACRONYM);
                break;
            case PIZARRO_EDITION_NAME:
                setAcronym(PIZARRO_EDITION_ACRONYM);
                break;
            default:
                assert false : "Nome de editor com erros: " + editor;
        }
        setTextModule4Expert(text);
    }

    public void remove() {
        setTextModule4Expert(null);
        getExpertEditionIntersSet().forEach(i -> i.remove());
        deleteDomainObject();
    }

    public boolean isExpertEdition() {
        return true;
    }

    @Override
    public int compareTo(ExpertEdition other) {
        String myEditor = getEditor();
        String otherEditor = other.getEditor();

        if (myEditor.equals(otherEditor)) {
            return 0;
        } else if (myEditor.equals(COELHO_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(COELHO_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(CUNHA_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(CUNHA_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(ZENITH_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(ZENITH_EDITION_NAME)) {
            return 1;
        } else {
            assert false : "To extend when new expert editions are include";
            return 0;
        }
    }


    public String getEditorShortName() {
        if (getEditor().equals(COELHO_EDITION_NAME)) {
            return "Coelho";
        } else if (getEditor().equals(CUNHA_EDITION_NAME)) {
            return "Cunha";
        } else if (getEditor().equals(ZENITH_EDITION_NAME)) {
            return "Zenith";
        } else if (getEditor().equals(PIZARRO_EDITION_NAME)) {

            return "Pizarro";
        } else {
            assert false;
            return null;
        }
    }

    public List<ExpertEditionInter> getSortedInter4Frag(Fragment fragment) {
        List<ExpertEditionInter> interps = new ArrayList<>();


        for (ScholarInter inter : fragment.getScholarInterSet()) {
            if (inter.isExpertInter()
                    && ((ExpertEditionInter) inter).getExpertEdition() == this) {
                interps.add((ExpertEditionInter) inter);
            }
        }

        Collections.sort(interps);

        return interps;

    }

    public ExpertEditionInter getNextHeteronymInter(ExpertEditionInter inter, Heteronym heteronym) {
        List<ExpertEditionInter> interps = new ArrayList<>(getExpertEditionIntersSet());

        Collections.sort(interps);

        return findNextElementByHeteronym(inter, heteronym, interps);
    }

    public ExpertEditionInter getPrevHeteronymInter(ExpertEditionInter inter, Heteronym heteronym) {
        List<ExpertEditionInter> interps = new ArrayList<>(getExpertEditionIntersSet());

        Collections.sort(interps, Collections.reverseOrder());

        return findNextElementByHeteronym(inter, heteronym, interps);
    }

    private ExpertEditionInter findNextElementByHeteronym(ExpertEditionInter inter, Heteronym heteronym,
                                                          List<ExpertEditionInter> interps) {
        Boolean stopNext = false;
        for (ExpertEditionInter tmpInter : interps) {
            if (stopNext) {
                return tmpInter;
            }
            if (tmpInter.getHeteronym() == heteronym && tmpInter == inter) {
                stopNext = true;
            }
        }
        return interps.get(0);
    }

    public Set<ExpertEditionInter> getIntersSet() {
        return new HashSet<>(getExpertEditionIntersSet());
    }

    public List<ExpertEditionInter> getSortedInterps() {
        return getIntersSet().stream().map(ExpertEditionInter.class::cast).sorted().collect(Collectors.toList());
    }

    public ExpertEditionInter getFragInterByUrlId(String urlId) {
        return getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().orElse(null);
    }

    public ExpertEditionInter getFragInterByXmlId(String xmlId) {
        return getIntersSet().stream().filter(i -> i.getXmlId().equals(xmlId)).findFirst().orElse(null);
    }

    @Override
    public String getReference() {
        return getEditor();
    }

    public ExpertEditionInter getFirstInterpretation() {
        List<ExpertEditionInter> interps = new ArrayList<>(getExpertEditionIntersSet());

        Collections.sort(interps);

        return interps.get(0);
    }

}
