package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.recommendation.VSMExpertEditionInterRecommender;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;

import java.util.*;
import java.util.stream.Collectors;

public class ExpertEdition extends ExpertEdition_Base implements Comparable<ExpertEdition> {
    public ExpertEdition(LdoD ldoD, String title, String author, String editor, LocalDate date) {
        setTitle(title);
        setAuthor(author);
        setEditor(editor);
        setDate(date);
        setPub(true);

        switch (editor) {
            case Edition.COELHO_EDITION_NAME:
                setAcronym(Edition.COELHO_EDITION_ACRONYM);
                break;
            case Edition.CUNHA_EDITION_NAME:
                setAcronym(Edition.CUNHA_EDITION_ACRONYM);
                break;
            case Edition.ZENITH_EDITION_NAME:
                setAcronym(Edition.ZENITH_EDITION_ACRONYM);
                break;
            case Edition.PIZARRO_EDITION_NAME:
                setAcronym(Edition.PIZARRO_EDITION_ACRONYM);
                break;
            default:
                assert false : "Nome de editor com erros: " + editor;
        }

        setLdoD4Expert(ldoD);
    }

    @Override
    public void remove() {
        setLdoD4Expert(null);
        getExpertEditionIntersSet().forEach(i -> i.remove());
        super.remove();
    }

    @Override
    public Edition.EditionType getSourceType() {
        return Edition.EditionType.EDITORIAL;
    }

    @Override
    public int compareTo(ExpertEdition other) {
        String myEditor = getEditor();
        String otherEditor = other.getEditor();

        if (myEditor.equals(otherEditor)) {
            return 0;
        } else if (myEditor.equals(Edition.COELHO_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(Edition.COELHO_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(Edition.CUNHA_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(Edition.CUNHA_EDITION_NAME)) {
            return 1;
        } else if (myEditor.equals(Edition.ZENITH_EDITION_NAME)) {
            return -1;
        } else if (otherEditor.equals(Edition.ZENITH_EDITION_NAME)) {
            return 1;
        } else {
            assert false : "To extend when new expert editions are include";
            return 0;
        }
    }

    String getEditorShortName() {
        if (getEditor().equals(Edition.COELHO_EDITION_NAME)) {
            return "Coelho";
        } else if (getEditor().equals(Edition.CUNHA_EDITION_NAME)) {
            return "Cunha";
        } else if (getEditor().equals(Edition.ZENITH_EDITION_NAME)) {
            return "Zenith";
        } else if (getEditor().equals(Edition.PIZARRO_EDITION_NAME)) {
            return "Pizarro";
        } else {
            assert false;
            return null;
        }
    }

    public List<ExpertEditionInter> getSortedInter4Frag(Fragment fragment) {
        List<ExpertEditionInter> interps = new ArrayList<>();

        for (FragInter inter : fragment.getFragmentInterSet()) {
            if (inter.getSourceType() == Edition.EditionType.EDITORIAL
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

    @Override
    public Set<FragInter> getIntersSet() {
        return new HashSet<>(getExpertEditionIntersSet());
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

    public List<InterIdDistancePairDto> getIntersByDistance(ExpertEditionInter expertEditionInter, WeightsDto weights) {
        List<ExpertEditionInter> inters = new ArrayList<>(getExpertEditionIntersSet());
        VSMExpertEditionInterRecommender recommender = new VSMExpertEditionInterRecommender();

        inters.remove(expertEditionInter);

        List<InterIdDistancePairDto> recommendedEdition = new ArrayList<>();

        List<Property> properties = weights.getProperties();
        for (ExpertEditionInter inter : inters) {
            recommendedEdition.add(new InterIdDistancePairDto(inter.getExternalId(),
                    recommender.calculateSimilarity(expertEditionInter, inter, properties)));
        }

        recommendedEdition = recommendedEdition.stream().sorted(Comparator.comparing(InterIdDistancePairDto::getDistance).reversed()).collect(Collectors.toList());
        recommendedEdition.add(0, new InterIdDistancePairDto(expertEditionInter.getExternalId(), 1.0d));

        return recommendedEdition;
    }


}
