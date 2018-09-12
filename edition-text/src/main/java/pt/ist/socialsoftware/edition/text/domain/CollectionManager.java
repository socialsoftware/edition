package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.fenixframework.FenixFramework;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionManager extends CollectionManager_Base {

    public static CollectionManager getInstance() { return FenixFramework.getDomainRoot().getCollectionManager(); }
    
    public CollectionManager() {
        FenixFramework.getDomainRoot().setCollectionManager(this);
        setNullEdition(new NullEdition());
    }


    public List<Heteronym> getSortedHeteronyms() {
        return getHeteronymsSet().stream().sorted(Comparator.comparing(Heteronym::getName))
                .collect(Collectors.toList());
    }

    public List<ExpertEdition> getSortedExpertEdition() {
        return getExpertEditionsSet().stream().sorted().collect(Collectors.toList());
    }

    public Edition getEdition(String acronym) {
        for (Edition edition : getExpertEditionsSet()) {
            if (edition.getAcronym().toUpperCase().equals(acronym.toUpperCase())) {
                return edition;
            }
        }

        return null;
//        FIXME Take this to Virtual
//        return getVirtualEdition(acronym);
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

    public ExpertEdition getJPCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.COELHO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getTSCEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.CUNHA_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getRZEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.ZENITH_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

    public ExpertEdition getJPEdition() {
        return getExpertEditionsSet().stream().filter(ve -> ve.getAcronym().equals(Edition.PIZARRO_EDITION_ACRONYM))
                .findFirst().orElse(null);
    }

}
