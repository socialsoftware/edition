package pt.ist.socialsoftware.edition.ldod.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

import java.util.*;
import java.util.stream.Collectors;

public class VirtualEditionInter extends VirtualEditionInter_Base implements Comparable<VirtualEditionInter> {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEditionInter.class);

    public String getUrlId() {
        return getXmlId().replace(".", "_");
    }

    @Override
    public String getXmlId() {
        return getFragmentXmlId() + ".WIT.ED.VIRT." + getVirtualEdition().getAcronym() + "." + super.getXmlId();
    }

    public HeteronymDto getHeteronym() {
        return getLastUsed().getHeteronym();
    }

    public LdoDDateDto getLdoDDate() {
        return getLastUsed().getLdoDDate();
    }

    public VirtualEditionInter(Section section, VirtualEditionInter inter, int number) {
        setUses(inter);
        setUsesScholarInterId(null);

        setSection(section);
        setNumber(number);
        // needs to store the number of interpretations in this fragment for this
        // edition
        setXmlId(Integer.toString(getVirtualEdition().getVirtualEditionInterSetForFragment(getFragmentXmlId()).size()));
    }

    public VirtualEditionInter(Section section, ScholarInterDto scholarInterDto, int number) {
        setUses(null);
        setUsesScholarInterId(scholarInterDto.getXmlId());


        setSection(section);
        setNumber(number);
        // needs to store the number of interpretations in this fragment for this
        // edition
        setXmlId(Integer.toString(getVirtualEdition().getVirtualEditionInterSetForFragment(getFragmentXmlId()).size()));
    }

    public void remove() {

        for (VirtualEditionInter inter : getIsUsedBySet()) {
            inter.setUses(getUses());
            inter.setUsesScholarInterId(getUsesScholarInterId()); // set usesfraginter so that first level vei can now the fraginter they point too
        }

        setSection(null);

        setUses(null);

        for (Tag tag : getTagSet()) {
            tag.remove();
        }

        for (Annotation annotation : getAnnotationSet()) {
            annotation.remove();
        }

        getClassificationGameSet().stream().forEach(classificationGame -> classificationGame.remove());

        deleteDomainObject();
    }

    public String getShortName() {
        return getVirtualEdition().getAcronym();
    }

    public String getTitle() {
        return getUses() != null ? getUses().getTitle() : (new TextProvidesInterface()).getScholarInterTitle(getUsesScholarInterId());
    }

    public String getUsesXmlId() {
        if (getUses() == null) {
            return getUsesScholarInterId();
        } else {
            return getUses().getXmlId();
        }
    }

    @Override
    public int compareTo(VirtualEditionInter other) {
        if (this.getVirtualEdition() == other.getVirtualEdition()) {
            int diff = getNumber() - other.getNumber();
            int result = diff > 0 ? 1 : diff < 0 ? -1 : 0;
            if (result != 0) {
                return result;
            } else {
                String myTitle = getTitle();
                String otherTitle = other.getTitle();
                return myTitle.compareTo(otherTitle);
            }
        } else {
            return this.getVirtualEdition().getTitle().compareTo(other.getVirtualEdition().getTitle());
        }

    }

    public ScholarInterDto getLastUsed() {
        return getUses() != null ? getUses().getLastUsed() : (new TextProvidesInterface()).getScholarInter(getUsesScholarInterId());
    }

    public VirtualEdition getEdition() {
        return getVirtualEdition();
    }

    public String getReference() {
        return Integer.toString(getNumber());
    }

    // Foi alterado por causa das human annotations
    public Set<String> getHumanAnnotationContributorSet() {
        Set<String> contributors = new HashSet<>();
        for (Annotation annotation : getAnnotationSet()) {
            if (annotation instanceof HumanAnnotation) {
                contributors.add(annotation.getUser());
            }
        }
        return contributors;
    }

    public VirtualEdition getVirtualEdition() {
        return getSection().getRootSection().getVirtualEdition();
    }

    @Atomic(mode = TxMode.WRITE)
    public HumanAnnotation createHumanAnnotation(String quote, String text, String user, List<RangeJson> rangeList,
                                                 List<String> tagList) {
        logger.debug("createHumanAnnotation start:{}, startOffset:{}, end:{}, endOffset:{}",
                rangeList.get(0).getStart(), rangeList.get(0).getStartOffset(), rangeList.get(0).getEnd(),
                rangeList.get(0).getEndOffset());

        SimpleText startText = null;
        // startText =
        // getFragment().getTextPortion().getSimpleText(getLastUsed(), 0,
        // rangeList.get(0).getStartOffset());
        SimpleText endText = null;
        // endText = getFragment().getTextPortion().getSimpleText(getLastUsed(),
        // 0, rangeList.get(0).getEndOffset());

        HumanAnnotation annotation = new HumanAnnotation(this, startText, endText, quote, text, user);

        for (RangeJson rangeJson : rangeList) {
            new Range(annotation, rangeJson.getStart(), rangeJson.getStartOffset(), rangeJson.getEnd(),
                    rangeJson.getEndOffset());
        }

        for (String tag : tagList) {
            createTag(annotation.getUser(), tag, annotation);
        }

        return annotation;
    }

    // TODO: createAwareAnnotation
    @Atomic(mode = TxMode.WRITE)
    public AwareAnnotation createAwareAnnotation(String quote, String text, Citation citation,
                                                 List<RangeJson> rangeList) {
        logger.debug("createAwareAnnotation start:{}, startOffset:{}, end:{}, endOffset:{}",
                rangeList.get(0).getStart(), rangeList.get(0).getStartOffset(), rangeList.get(0).getEnd(),
                rangeList.get(0).getEndOffset());

        AwareAnnotation annotation = new AwareAnnotation(this, quote, text, citation);
        for (RangeJson rangeJson : rangeList) {
            new Range(annotation, rangeJson.getStart(), rangeJson.getStartOffset(), rangeJson.getEnd(),
                    rangeJson.getEndOffset());
        }
        return annotation;
    }

    @Atomic(mode = TxMode.WRITE)
    public void associate(String user, Set<String> categoryNames) {
        Set<String> purgedCategoryNames = categoryNames.stream().map(n -> Category.purgeName(n)).distinct()
                .collect(Collectors.toSet());

        getAssignedCategories(user).stream()
                .filter(c -> !purgedCategoryNames.contains(c.getNameInEditionContext(getVirtualEdition())))
                .forEach(c -> dissociate(user, c));

        Set<String> existingCategories = getAssignedCategories(user).stream()
                .map(c -> c.getNameInEditionContext(getVirtualEdition())).collect(Collectors.toSet());

        Set<String> toAssociate = purgedCategoryNames.stream().filter(cname -> !existingCategories.contains(cname))
                .collect(Collectors.toSet());

        for (String categoryName : toAssociate) {
            createTag(user, categoryName, null);
        }

    }

    // Foi alterado por causa das HumanAnnotation
    @Atomic(mode = TxMode.WRITE)
    public void dissociate(String user, Category category) {
        Set<Tag> tags = getTagSet().stream().filter(t -> t.getCategory() == category && t.getContributor().equals(user))
                .collect(Collectors.toSet());
        for (Tag tag : tags) {
            tag.remove();
        }

        Set<HumanAnnotation> annotations = getAnnotationSet().stream().filter(HumanAnnotation.class::isInstance)
                .map(HumanAnnotation.class::cast).filter(a -> a.getTagSet().isEmpty() && a.getText() == null)
                .collect(Collectors.toSet());
        for (HumanAnnotation annotation : annotations) {
            annotation.remove();
        }
    }

    public Set<Category> getCategories() {
        // return getTagSet().stream().map(t ->
        // t.getCategory()).collect(Collectors.toSet());
        Set<Category> categories = new HashSet<>();
        for (Tag tag : getTagSet()) {
            categories.add(tag.getCategory());
        }
        return categories;
    }

    public List<Category> getSortedCategories(VirtualEdition virtualEdition) {
        return getCategories().stream().filter(c -> c.getTaxonomy().getEdition() == virtualEdition)
                .collect(Collectors.toList());
    }

    public List<Category> getAssignedCategories() {
        return getAllDepthTags().stream().map(t -> t.getCategory()).distinct()
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());
    }

    public List<Category> getNonAssignedCategories(String user) {
        List<Category> interCategories = getAssignedCategories(user);

        List<Category> categories = getAllDepthCategories().stream().filter(c -> !interCategories.contains(c))
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());

        return categories;
    }

    public List<Category> getAssignedCategories(String user) {
        List<Category> categories = getAllDepthTags().stream().filter(t -> t.getContributor().equals(user))
                .map(t -> t.getCategory()).distinct()
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());

        return categories;
    }

    public Set<Category> getAllDepthCategories() {
        Set<Category> categories = null;
        if (getVirtualEdition().isPublicOrIsParticipant()) {
            categories = new HashSet<>(getVirtualEdition().getTaxonomy().getCategoriesSet());
        } else {
            categories = new HashSet<>();
        }

        if (getUses() != null) {
            categories.addAll(getUses().getAllDepthCategories());
        }

        return categories;
    }

    // Estava a dar erro
    /*
     * @Override public Set<HumanAnnotation> getAllDepthAnnotations() {
     * Set<HumanAnnotation> annotations = null; if
     * (getVirtualEdition().isPublicOrIsParticipant()) { annotations = new
     * HashSet<>(getAnnotationSet()); } else { annotations = new HashSet<>(); }
     *
     * annotations.addAll(getUses().getAllDepthAnnotations());
     *
     * return annotations; }
     */

    // Solução - a funcionar
    public Set<HumanAnnotation> getAllDepthHumanAnnotations() {
        Set<HumanAnnotation> annotations = null;
        if (getVirtualEdition().isPublicOrIsParticipant()) {
            annotations = new HashSet<>(getAnnotationSet().stream().filter(HumanAnnotation.class::isInstance)
                    .map(HumanAnnotation.class::cast).collect(Collectors.toSet()));
        } else {
            annotations = new HashSet<>();
        }

        if (getUses() != null) {
            annotations.addAll(getUses().getAllDepthHumanAnnotations());
        }

        return annotations;
    }

    // Solução para suportar os dois tipos de annotation
    public Set<Annotation> getAllDepthAnnotations() {
        Set<Annotation> annotations = null;
        if (getVirtualEdition().isPublicOrIsParticipant()) {
            annotations = new HashSet<>(getAnnotationSet());
        } else {
            annotations = new HashSet<>();
        }

        if (getUses() != null) {
            annotations.addAll(getUses().getAllDepthAnnotations());
        }

        return annotations;
    }

    public Set<Tag> getAllDepthTags() {
        Set<Tag> tags = null;

        if (getVirtualEdition().isPublicOrIsParticipant()) {
            tags = new HashSet<>(getTagSet());
        } else {
            tags = new HashSet<>();
        }


        if (getUses() != null) {
            tags.addAll(getUses().getAllDepthTags());
        }

        return tags;
    }

    public Set<String> getContributorSet(Category category) {
        return getAllDepthTags().stream().filter(t -> t.getCategory() == category).map(t -> t.getContributor())
                .collect(Collectors.toSet());
    }

    public Set<String> getContributorSet() {
        return getAllDepthTags().stream().map(t -> t.getContributor()).collect(Collectors.toSet());
    }

    public String getAllDepthCategoriesJSON() {
        ObjectMapper mapper = new ObjectMapper();

        List<CategoryDTO> categories = getAllDepthCategories().stream()
                .sorted((c1, c2) -> c1.compareInEditionContext(getVirtualEdition(), c2))
                .map(c -> new CategoryDTO(getVirtualEdition(), c)).collect(Collectors.toList());

        try {
            return mapper.writeValueAsString(categories);
        } catch (JsonProcessingException e) {
            throw new LdoDException("VirtualEditionInter::getAllDepthCategoriesJSON");
        }
    }

    private void createTag(String user, String categoryName, HumanAnnotation annotation) {
        if (categoryName.contains(".")) {
            String[] values = categoryName.split("\\.");
            VirtualEdition edition = VirtualModule.getInstance().getVirtualEdition(values[0]);
            if (edition.getTaxonomy().getCategory(values[1]) != null) {
                edition.getTaxonomy().createTag(this, values[1], annotation, user);
            } else {
                throw new LdoDException("Cannot create Category in an inherited Virtual Edition");
            }
        } else {
            getVirtualEdition().getTaxonomy().createTag(this, categoryName, annotation, user);
        }
    }

    public void updateTags(HumanAnnotation annotation, List<String> tags) {
        for (Tag tag : annotation.getTagSet()) {
            if (!tags.contains(tag.getCategory().getNameInEditionContext(getVirtualEdition()))) {
                tag.remove();
            }
        }

        List<String> purgedTags = tags.stream().map(n -> Category.purgeName(n)).distinct().collect(Collectors.toList());

        for (String tag : purgedTags) {
            if (!annotation.existsTag(tag, getVirtualEdition())) {
                createTag(annotation.getUser(), tag, annotation);
            }
        }
    }

    public Set<VirtualEdition> getUsedIn() {
        Set<VirtualEdition> editions = getIsUsedBySet().stream().flatMap(i -> i.getUsedIn().stream())
                .collect(Collectors.toSet());
        editions.add(getVirtualEdition());

        return editions;
    }

    public Set<Tag> getTagsCompleteInter() {
        Set<Tag> result = new HashSet<>(getAllDepthTags());
        result.removeAll(getAllDepthHumanAnnotations().stream().flatMap(t -> t.getTagSet().stream())
                .collect(Collectors.toSet()));
        return result;
    }

    public List<Section> getParentSectionsPath() {
        List<Section> result = new ArrayList<>();
        Section parent = getSection();
        while (parent != null) {
            result.add(0, parent);
            parent = parent.getParentSection();
        }
        return result;
    }

    public int getUsesDepth() {
        return getUses() != null ? getUses().getUsesDepth() + 1 : 1;
    }

    public int getNumberOfTimesCited() {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        return getLastUsed().getNumberOfTimesCited();
    }

    public int getNumberOfTimesCitedIncludingRetweets() {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

        return getLastUsed().getNumberOfTimesCitedIncludingRetweets();
    }

    public Set<VirtualEditionInter> getIsUsedByDepthSet() {
        Set<VirtualEditionInter> isUsedBy = new HashSet<>(getIsUsedBySet());
        for (VirtualEditionInter inter : getIsUsedBySet()) {
            isUsedBy.addAll(inter.getIsUsedByDepthSet());
        }
        return isUsedBy;
    }

    public String getFragmentXmlId() {
        return getLastUsed().getFragmentXmlId();
    }


    public VirtualEditionInter getNextNumberInter() {
        List<VirtualEditionInter> interps = new ArrayList<>(this.getVirtualEdition().getIntersSet());

        Collections.sort(interps);

        return findNextElementByNumber(interps);
    }

    public VirtualEditionInter getPrevNumberInter() {
        List<VirtualEditionInter> interps = new ArrayList<>(this.getVirtualEdition().getIntersSet());

        Collections.sort(interps, Collections.reverseOrder());

        return findNextElementByNumber(interps);
    }


    private VirtualEditionInter findNextElementByNumber(List<VirtualEditionInter> interps) {
        Boolean stopNext = false;
        for (VirtualEditionInter tmpInter : interps) {
            if (stopNext) {
                return tmpInter;
            }
            if (tmpInter.getNumber() == getNumber() && tmpInter == this) {
                stopNext = true;
            }
        }
        return interps.get(0);
    }

    public FragmentDto getFragmentDto() {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        return textProvidesInterface.getFragmentOfScholarInterDto(getLastUsed());
    }

}
