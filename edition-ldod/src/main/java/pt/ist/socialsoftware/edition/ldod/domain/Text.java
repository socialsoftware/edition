package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.FenixFramework;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Text extends Text_Base {
    public static Logger logger = LoggerFactory.getLogger(Text.class);

    public static Text getInstance() {
        return FenixFramework.getDomainRoot().getText();
    }

    public Text() {
        FenixFramework.getDomainRoot().setText(this);
        setNullEdition(new NullEdition());
    }

    public void remove() {
        getNullEdition().remove();
        getExpertEditionsSet().forEach(e -> e.remove());
        getFragmentsSet().forEach(f -> f.remove());
        getHeteronymsSet().forEach(h -> h.remove());

        setRoot(null);

        deleteDomainObject();

    }

    public List<Heteronym> getSortedHeteronyms() {
        return getHeteronymsSet().stream().sorted((h1, h2) -> h1.getName().compareTo(h2.getName()))
                .collect(Collectors.toList());
    }

    public List<ExpertEdition> getSortedExpertEdition() {
        return getExpertEditionsSet().stream().sorted().collect(Collectors.toList());
    }

    public ExpertEdition getExpertEdition(String acronym) {
        for (ExpertEdition edition : getExpertEditionsSet()) {
            if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
                return edition;
            }
        }

        return null;
    }

    public ExpertEdition getJPCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(ExpertEdition.COELHO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getTSCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(ExpertEdition.CUNHA_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getRZEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(ExpertEdition.ZENITH_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getJPEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(ExpertEdition.PIZARRO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public Fragment getFragmentByXmlId(String target) {
        for (Fragment fragment : getFragmentsSet()) {
            if (fragment.getXmlId().equals(target)) {
                return fragment;
            }
        }
        return null;
    }

    public Set<SourceInter> getFragmentRepresentatives() {
        return getFragmentsSet().stream().map(f -> f.getRepresentativeSourceInter()).collect(Collectors.toSet());
    }

    public ScholarInter getScholarInterByXmlId(String scholarInterId) {
        return getFragmentsSet().stream().filter(fragment -> fragment.getScholarInterByXmlId(scholarInterId) != null)
                .map(fragment -> fragment.getScholarInterByXmlId(scholarInterId)).findAny().orElse(null);
    }

}
