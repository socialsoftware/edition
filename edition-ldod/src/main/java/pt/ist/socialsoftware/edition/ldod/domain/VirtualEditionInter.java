package pt.ist.socialsoftware.edition.ldod.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VirtualEditionInter extends VirtualEditionInter_Base {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEditionInter.class);

    @Override
    public String getXmlId() {
        return getFragment().getXmlId() + ".WIT.ED.VIRT." + getVirtualEdition().getAcronym() + "." + super.getXmlId();
    }

    public VirtualEditionInter(Section section, FragInter inter, int number) {
        setFragment(inter.getFragment());
        setHeteronym(null);
        setLdoDDate(null);
        setSection(section);
        setNumber(number);
        setUses(inter);
        // needs to store the number of interpretations in this fragment for this
        // edition
        setXmlId(Integer.toString(getFragment().getNumberOfInter4Edition(getVirtualEdition())));
    }

    @Override
    public void remove() {
        for (VirtualEditionInter inter : getIsUsedBySet()) {
            inter.setUses(getUses());
        }

        setFragment(null);

        setSection(null);

        setUses(null);

        for (Tag tag : getTagSet()) {
            tag.remove();
        }

        for (Annotation annotation : getAnnotationSet()) {
            annotation.remove();
        }

        getClassificationGameSet().stream().forEach(g -> g.remove());

        super.remove();
    }

    @Override
    public String getShortName() {
        return getVirtualEdition().getAcronym();
    }

    @Override
    public String getTitle() {
        return getUses().getTitle();
    }

    @Override
    public EditionType getSourceType() {
        return EditionType.VIRTUAL;
    }

    public int compareVirtualEditionInter(VirtualEditionInter other) {
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

    @Override
    public boolean belongs2Edition(Edition edition) {
        return getVirtualEdition() == edition;
    }

    @Override
    public FragInter getLastUsed() {
        return getUses().getLastUsed();
    }

    @Override
    public Edition getEdition() {
        return getVirtualEdition();
    }

    @Override
    public List<FragInter> getListUsed() {
        List<FragInter> listUses = getUses().getListUsed();
        listUses.add(0, getUses());
        return listUses;
    }

    @Override
    public String getReference() {
        return Integer.toString(getNumber());
    }

    // Foi alterado por causa das human annotations
    public Set<LdoDUser> getHumanAnnotationContributorSet() {
        Set<LdoDUser> contributors = new HashSet<>();
        for (Annotation annotation : getAnnotationSet()) {
            if (annotation instanceof HumanAnnotation) {
                contributors.add(((HumanAnnotation) annotation).getUser());
            }
        }
        return contributors;
    }

    public VirtualEdition getVirtualEdition() {
        return getSection().getRootSection().getVirtualEdition();
    }

    @Atomic(mode = TxMode.WRITE)
    public HumanAnnotation createHumanAnnotation(String quote, String text, LdoDUser user, List<RangeJson> rangeList,
                                                 List<String> tagList, String contents) {
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

        HumanAnnotation annotation = new HumanAnnotation(this, startText, endText, quote, text, user, contents);

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
    public void associate(LdoDUser user, Set<String> categoryNames) {
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
    public void dissociate(LdoDUser user, Category category) {
        Set<Tag> tags = getTagSet().stream().filter(t -> t.getCategory() == category && t.getContributor() == user)
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
        return getAllDepthTags().stream().map(Tag_Base::getCategory).distinct()
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());
    }

    public List<Category> getNonAssignedCategories(LdoDUser user) {
        List<Category> interCategories = getAssignedCategories(user);

        return getAllDepthCategories().stream().filter(c -> !interCategories.contains(c))
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());
    }

    public List<Category> getAssignedCategories(LdoDUser user) {

        return getAllDepthTags().stream().filter(t -> t.getContributor() == user)
                .map(Tag_Base::getCategory).distinct()
                .sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
                .collect(Collectors.toList());
    }

    @Override
    public Set<Category> getAllDepthCategories() {
        Set<Category> categories = null;
        if (getVirtualEdition().checkAccess()) {
            categories = new HashSet<>(getVirtualEdition().getTaxonomy().getCategoriesSet());
        } else {
            categories = new HashSet<>();
        }

        categories.addAll(getUses().getAllDepthCategories());

        return categories;
    }

    // Estava a dar erro
    /*
     * @Override public Set<HumanAnnotation> getAllDepthAnnotations() {
     * Set<HumanAnnotation> annotations = null; if
     * (getVirtualEdition().checkAccess()) { annotations = new
     * HashSet<>(getAnnotationSet()); } else { annotations = new HashSet<>(); }
     *
     * annotations.addAll(getUses().getAllDepthAnnotations());
     *
     * return annotations; }
     */

    // Solução - a funcionar
    @Override
    public Set<HumanAnnotation> getAllDepthHumanAnnotations() {
        Set<HumanAnnotation> annotations = null;
        if (getVirtualEdition().checkAccess()) {
            annotations = new HashSet<>(getAnnotationSet().stream().filter(HumanAnnotation.class::isInstance)
                    .map(HumanAnnotation.class::cast).collect(Collectors.toSet()));
        } else {
            annotations = new HashSet<>();
        }

        annotations.addAll(getUses().getAllDepthHumanAnnotations());

        return annotations;
    }

    // Solução para suportar os dois tipos de annotation
    @Override
    public Set<Annotation> getAllDepthAnnotations() {
        Set<Annotation> annotations = null;
        if (getVirtualEdition().checkAccess()) {
            annotations = new HashSet<>(getAnnotationSet());
        } else {
            annotations = new HashSet<>();
        }

        annotations.addAll(getUses().getAllDepthAnnotations());

        return annotations;
    }

    @Override
    public Set<Tag> getAllDepthTags() {
        Set<Tag> tags = null;

        if (getVirtualEdition().checkAccess()) {
            tags = new HashSet<>(getTagSet());
        } else {
            tags = new HashSet<>();
        }

        tags.addAll(getUses().getAllDepthTags());

        return tags;
    }

    public Set<LdoDUser> getContributorSet(Category category) {
        return getAllDepthTags().stream().filter(t -> t.getCategory() == category).map(t -> t.getContributor())
                .collect(Collectors.toSet());
    }

    public Set<LdoDUser> getContributorSet() {
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

    private void createTag(LdoDUser user, String categoryName, HumanAnnotation annotation) {
        if (categoryName.contains(".")) {
            String[] values = categoryName.split("\\.");
            VirtualEdition edition = (VirtualEdition) LdoD.getInstance().getEdition(values[0]);
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
        List<String> purgedTags = tags.stream().map(n -> Category.purgeName(n)).distinct().collect(Collectors.toList());

        for (Tag tag : annotation.getTagSet()) {
            if (!purgedTags.contains(tag.getCategory().getNameInEditionContext(getVirtualEdition()))) {
                tag.remove();
            }
        }

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

    @Override
    public int getUsesDepth() {
        return getUses().getUsesDepth() + 1;
    }

    // Is it this way? (this method doesn't take into account the retweets)
    public int getNumberOfTimesCited() {
        return this.getLastUsed().getInfoRangeSet().size();
    }

    // this methods takes into account the number of retweets
    public int getNumberOfTimesCitedIncludingRetweets() {
        Set<InfoRange> infoRanges = this.getLastUsed().getInfoRangeSet();
        int count = infoRanges.size();
        for (InfoRange infoRange : infoRanges) {
            count += infoRange.getCitation().getNumberOfRetweets();
        }
        return count;
    }
}
