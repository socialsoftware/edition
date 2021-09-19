package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.text.config.BeanUtil;
import pt.ist.socialsoftware.edition.text.api.TextEventPublisher;


import java.util.*;


public class Fragment extends Fragment_Base implements Comparable<Fragment> {
    private static final Logger logger = LoggerFactory.getLogger(Fragment.class);


    public enum PrecisionType {
        HIGH("high"), MEDIUM("medium"), LOW("low"), UNKNOWN("unknown");

        private final String desc;

        PrecisionType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public Fragment(TextModule text, String title, String xmlId) {
        setTextModule(text);
        setTitle(title);
        setXmlId(xmlId);
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {

//        EventInterface.getInstance().publish(new Event(Event.EventType.FRAGMENT_REMOVE, this.getXmlId()));
        TextEventPublisher eventPublisher = BeanUtil.getBean(TextEventPublisher.class);
        eventPublisher.publishEvent(new Event(Event.EventType.FRAGMENT_REMOVE, this.getXmlId()));

        setTextModule(null);

        getTextPortion().remove();

        for (ScholarInter inter : getScholarInterSet()) {
            inter.remove();
        }

        for (Source source : getSourcesSet()) {
            source.remove();
        }

        for (RefText ref : getRefTextSet()) {
            // the reference is removed
            ref.remove();
        }

        // TODO: it does not belong to this model
        getCitationSet().stream().forEach(c -> c.remove());

        deleteDomainObject();
    }

	/*public List<ScholarInter> getSortedInterps() {
		List<ScholarInter> interps = new ArrayList<>(getScholarInterSet());

		Collections.sort(interps);

		return interps;
	}*/

    public List<SourceInter> getSortedSourceInter() {
        List<SourceInter> interps = new ArrayList<>();

        for (ScholarInter inter : getScholarInterSet()) {
            if (!inter.isExpertInter()) {
                interps.add((SourceInter) inter);
            }
        }

        Collections.sort(interps);

        return interps;
    }

    public Set<ExpertEditionInter> getExpertEditionInterSet() {
        Set<ExpertEditionInter> result = new HashSet<>();
        for (ScholarInter inter : getScholarInterSet()) {
            if (inter.isExpertInter()) {
                result.add((ExpertEditionInter) inter);
            }
        }
        return result;
    }

    public Set<ExpertEditionInter> getExpertEditionInters(ExpertEdition expertEdition) {
        // return getScholarInterSet().stream().filter(inter ->
        // inter.getEdition() == expertEdition)
        // .map(ExpertEditionInter.class::cast).collect(Collectors.toSet());

        Set<ExpertEditionInter> result = new HashSet<>();
        for (ScholarInter inter : getScholarInterSet()) {
            if (inter.getEdition() == expertEdition) {
                result.add((ExpertEditionInter) inter);
            }
        }
        return result;
    }

    public Surface getSurface(String xmlId) {
        for (Source source : getSourcesSet()) {
            if (source.getFacsimile() != null) {
                for (Surface surface : source.getFacsimile().getSurfaces()) {
                    if (xmlId.equals(surface.getXmlId())) {
                        return surface;
                    }
                }
            }
        }
        return null;
    }

    public ScholarInter getScholarInterByXmlId(String xmlId) {
        for (ScholarInter inter : getScholarInterSet()) {
            if (xmlId.equals(inter.getXmlId())) {
                return inter;
            }
        }
        return null;
    }

    public ScholarInter getScholarInterByUrlId(String urlId) {
        for (ScholarInter inter : getScholarInterSet()) {
            if (urlId.equals(inter.getUrlId())) {
                return inter;
            }
        }
        return null;
    }

    public SourceInter getRepresentativeSourceInter() {
        // get the last one, since it is ordered, it will be printed, or
        // dactiloscript, or manuscript
        List<SourceInter> sourceInters = getSortedSourceInter();
        return sourceInters.get(sourceInters.size() - 1);
    }

    @Override
    public int compareTo(Fragment fragment) {
        return this.getXmlId().compareTo(fragment.getXmlId());
    }

    public Citation getCitationById(long id) {
        return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
    }
}
