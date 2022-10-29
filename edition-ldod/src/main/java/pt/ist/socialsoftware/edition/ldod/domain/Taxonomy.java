package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.TopicDTO;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;

import java.util.*;
import java.util.stream.Collectors;

public class Taxonomy extends Taxonomy_Base {
    private static Logger logger = LoggerFactory.getLogger(Taxonomy.class);

    public Taxonomy() {
        setOpenManagement(false);
        setOpenVocabulary(true);
        setOpenAnnotation(false);
    }

    public String getName() {
        return getEdition().getTitle();
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        setEdition(null);

        for (Category category : getCategoriesSet()) {
            category.remove();
        }

        deleteDomainObject();
    }

    public Set<Tag> getTagSet(VirtualEditionInter inter) {
        Set<Tag> set = new HashSet<>();
        for (Tag tag : inter.getTagSet()) {
            if (tag.getCategory().getTaxonomy() == this) {
                set.add(tag);
            }
        }
        return set;
    }

    public List<VirtualEditionInter> getSortedFragInter() {
        Set<VirtualEditionInter> set = new HashSet<>();
        for (Category category : getCategoriesSet()) {
            for (Tag tag : category.getTagSet()) {
                set.add(tag.getInter());
            }
        }
        List<VirtualEditionInter> list = new ArrayList<>(set);
        Collections.sort(list);

        return list;
    }

    public List<Category> getSortedCategories() {
        return getCategoriesSet().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
                .collect(Collectors.toList());
    }

    public List<Tag> getSortedTags(VirtualEdition virtualEdition) {
        return getCategoriesSet().stream().flatMap(c -> c.getTagSet().stream())
                .filter(t -> t.getInter().getVirtualEdition() == virtualEdition).distinct().sorted()
                .collect(Collectors.toList());
    }

    public Set<LdoDUser> getTagContributorSet(VirtualEditionInter inter) {
        Set<LdoDUser> contributors = new HashSet<>();
        for (Tag tag : getTagSet(inter)) {
            contributors.add(tag.getContributor());
        }
        return contributors;
    }

    public Category getCategory(String name) {
        for (Category category : getCategoriesSet()) {
            if (name.equals(category.getName())) {
                return category;
            }
        }
        return null;
    }

    public Category getCategoryByUrlId(String urlId) {
        for (Category category : getCategoriesSet()) {
            if (urlId.equals(category.getUrlId())) {
                return category;
            }
        }
        return null;
    }


    public List<VirtualEdition> getUsedIn() {


        return getEdition()
                .getAllDepthVirtualEditionInters()
                .stream()
                .flatMap(inter -> inter.getUsedIn().stream())
                .distinct().filter(ve -> !ve.equals(getEdition()))
                .sorted(Comparator.comparing(Edition_Base::getAcronym))
                .collect(Collectors.toList());

    }

    @Atomic(mode = TxMode.WRITE)
    public Category merge(List<Category> categories) {

        String name = categories.stream().map(c -> c.getName()).collect(Collectors.joining(" "));

        while (getCategory(name) != null) {
            name = name + "1";
        }

        Category category = new Category().init(this, name);

        categories.stream().flatMap(c -> c.getTagSet().stream()).forEach(t -> category.addTag(t));

        categories.stream().forEach(c -> c.remove());

        return category;
    }

    @Atomic(mode = TxMode.WRITE)
    public Category extract(Category category, Set<VirtualEditionInter> inters) {
        String suffix = "_Extracted";
        String newName = category.getName() + suffix;
        while (getCategory(newName) != null) {
            newName = newName + suffix;
        }

        Category newCategory = new Category().init(this, newName);

        for (VirtualEditionInter inter : inters) {
            inter.getTagSet().stream().filter(t -> t.getCategory() == category)
                    .forEach(t -> t.setCategory(newCategory));
        }

        return newCategory;
    }


    public Tag createTag(VirtualEditionInter virtualEditionInter, String categoryName, HumanAnnotation annotation,
                         LdoDUser ldoDUser) {
        if (!getOpenVocabulary() && getCategory(categoryName) == null) {
            throw new LdoDException("Tag name does not exist in taxonomy with closed vocabulary");
        }
        return new Tag().init(this.getEdition(), virtualEditionInter, categoryName, annotation, ldoDUser);
    }

    @Atomic(mode = TxMode.WRITE)
    public Category createCategory(String name) {
        return new Category().init(this, name);
    }

    public void createGeneratedCategories(TopicListDTO topicList) {
        if (topicList.getTopics() == null) return;
        LdoDUser user = LdoD.getInstance().getUser(topicList.getUsername());
        createGeneratedCategories(topicList.getTopics(), user);
    }

    @Atomic(mode = TxMode.WRITE)
    public void createGeneratedCategories(List<TopicDTO> topicList, LdoDUser user) {
        topicList.forEach(topic -> {
            Category category = new Category();
            category.init(this, topic.getName());
            topic.getInters().forEach(topicPercentage ->
                    new Tag().init(FenixFramework.getDomainObject(topicPercentage.getExternalId()), category, user));
        });
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete(List<Category> categories) {
        categories.forEach(Category::remove);
    }

    @Atomic(mode = TxMode.WRITE)
    public void edit(boolean openManagement, boolean openVocabulary, boolean openAnnotation) {
        setOpenManagement(openManagement);
        setOpenVocabulary(openVocabulary);
        setOpenAnnotation(openAnnotation);
    }

    public boolean canManipulateAnnotation(LdoDUser user) {
        if (user != null && getOpenAnnotation()) {
            return true;
        } else {
            return getEdition().getParticipantSet().contains(user);
        }
    }

    public boolean canManipulateTaxonomy(LdoDUser user) {
        if (getOpenManagement()) {
            return getEdition().getParticipantSet().contains(user);
        } else {
            return getEdition().getAdminSet().contains(user);
        }
    }
}
