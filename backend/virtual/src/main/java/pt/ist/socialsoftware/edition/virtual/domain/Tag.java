package pt.ist.socialsoftware.edition.virtual.domain;


import pt.ist.socialsoftware.edition.notification.event.EventInterface;
import pt.ist.socialsoftware.edition.notification.event.EventTagRemove;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.virtual.api.VirtualEventPublisher;
import pt.ist.socialsoftware.edition.virtual.config.BeanUtil;


public class Tag extends Tag_Base implements Comparable<Tag> {

    public Tag init(VirtualEdition virtualEdition, VirtualEditionInter inter, String categoryName,
                    HumanAnnotation annotation, String user) {
        setInter(inter);
        Taxonomy taxonomy = virtualEdition.getTaxonomy();
        Category category = taxonomy.getCategory(Category.purgeName(categoryName));
        if (category == null) {
            if (taxonomy.getOpenVocabulary()) {
                category = taxonomy.createCategory(categoryName);
            } else {
                throw new LdoDException("Cannot create Category using Closed Vocabulary");
            }
        }
        setCategory(category);
        setAnnotation(annotation);
        setContributor(user);

        return this;
    }

    public Tag init(VirtualEditionInter inter, Category category, String user) {
        setInter(inter);
        setCategory(category);
        setAnnotation(null);
        setContributor(user);

        return this;
    }

    public void remove() {
        String InterXmlId = getInter().getXmlId();
        String categoryXmlId = getCategory().getUrlId();

        setInter(null);

        if (getCategory() != null && getCategory().getTaxonomy().getOpenAnnotation()
                && getCategory().getTagSet().size() == 1) {
            Category category = getCategory();
            setCategory(null);
            category.remove();
        }

        setCategory(null);

        setAnnotation(null);

//        EventInterface.getInstance().publish(new EventTagRemove(InterXmlId, categoryXmlId));
        VirtualEventPublisher virtualEventPublisher = BeanUtil.getBean(VirtualEventPublisher.class);
        virtualEventPublisher.publishEvent(new EventTagRemove(InterXmlId, categoryXmlId));


        deleteDomainObject();
    }

    @Override
    public int compareTo(Tag other) {
        return this.getCategory().getName().compareTo(other.getCategory().getName());
    }

}
