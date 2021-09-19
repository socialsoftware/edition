package pt.ist.socialsoftware.edition.virtual.domain;

import org.apache.commons.lang.StringUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import java.util.*;

public class Section extends Section_Base implements Comparable<Section> {
    public static String DEFAULT = "default";

    public Section(VirtualEdition virtualEdition, String title, int number) {
        setTitle(title);
        setVirtualEdition(virtualEdition);
        setNumber(number);
    }

    public Section(Section section, String title, int number) {
        setParentSection(section);
        setTitle(title);
        setNumber(number);
    }

    public Set<VirtualEditionInter> getAllDepthVirtualEditionInterSet() {
        Set<VirtualEditionInter> inters = new HashSet<>();

        // Add section's inters
        Set<VirtualEditionInter> virtualEditionInterSet = getVirtualEditionInterSet();
        if (virtualEditionInterSet != null) {
            inters.addAll(virtualEditionInterSet);
        }

        // add subsection's inters
        Set<Section> subSectionSet = getSubSectionsSet();
        if (subSectionSet != null) {
            for (Section section : subSectionSet) {
                inters.addAll(section.getAllDepthVirtualEditionInterSet());
            }
        }
        return inters;
    }

    @Override
    public int compareTo(Section section) {
        return ((Integer) this.getNumber()).compareTo(section.getNumber());
    }

    @Atomic(mode = TxMode.WRITE)
    public Section createSection(String title, int number) {
        Section subSection = new Section(this, title, number);
        return subSection;
    }

    public boolean isRootSection() {
        return getParentSection() == null;
    }

    public Section getRootSection() {
        return isRootSection() ? this : getParentSection().getRootSection();
    }

    public void clear() {
        for (Section section : getSubSectionsSet()) {
            section.clear();
        }

        for (VirtualEditionInter inter : getVirtualEditionInterSet()) {
            inter.setSection(null);
        }

        setParentSection(null);
        setVirtualEdition(null);

        deleteDomainObject();
    }

    public void remove() {
        for (Section section : getSubSectionsSet()) {
            section.remove();
        }

        for (VirtualEditionInter inter : getVirtualEditionInterSet()) {
            inter.remove();
        }

        setParentSection(null);
        setVirtualEdition(null);

        deleteDomainObject();
    }

    public Section getSection(String title) {
        for (Section section : getSubSectionsSet()) {
            if (section.getTitle().equals(title)) {
                return section;
            }
        }

        return null;
    }

    public Section createSection(String title) {
        int number = getSubSectionsSet().size();
        return createSection(title, number);
    }

    @Atomic(mode = TxMode.WRITE)
    public void addVirtualEditionInter(VirtualEditionInter virtualEditionInter, int i) {
        virtualEditionInter.setNumber(i);
        virtualEditionInter.setSection(this);
    }

    public boolean isLeaf() {
        if (getSubSectionsSet() == null) {
            return true;
        } else if (getSubSectionsSet().size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getDepth() {
        if (isLeaf()) {
            return 1;
        } else {
            int max = 0;
            for (Section section : getSubSectionsSet()) {
                int depth = section.getDepth();
                if (max < depth) {
                    max = depth;
                }
            }
            return 1 + max;
        }
    }

    public String print(int i) {
        String result = StringUtils.repeat("\t", i) + getNumber() + ":" + getTitle() + "\n";
        for (Section section : getSubSectionsSet()) {
            result = result + section.print(i + 1);
        }
        for (VirtualEditionInter inter : getVirtualEditionInterSet()) {
            result = result + StringUtils.repeat("\t", i + 1) + inter.getNumber() + ":" + inter.getTitle() + "\n";
        }
        return result;
    }

    public List<Section> getSortedSubSections() {
        List<Section> sortedList = new ArrayList<>(getSubSectionsSet());
        Collections.sort(sortedList);
        return sortedList;
    }

    public List<VirtualEditionInter> getSortedInters() {
        List<VirtualEditionInter> sortedList = new ArrayList<>(getVirtualEditionInterSet());
        Collections.sort(sortedList);
        return sortedList;
    }

    @Override
    public VirtualEdition getVirtualEdition() {
        if (isRootSection()) {
            return super.getVirtualEdition();
        } else {
            return getRootSection().getVirtualEdition();
        }
    }

    public void clearEmptySections() {
        for (Section section : getSubSectionsSet()) {
            section.clearEmptySections();
        }

        for (Section section : getSubSectionsSet()) {
            if (section.getAllDepthVirtualEditionInterSet().size() == 0) {
                section.remove();
            }
        }
    }

}