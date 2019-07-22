package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.api.event.Event;
import pt.ist.socialsoftware.edition.ldod.api.event.EventInterface;
import pt.ist.socialsoftware.edition.ldod.api.event.EventTagRemove;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;

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
        EventInterface eventInterface = new EventInterface();
        eventInterface.publish(new EventTagRemove(getInter().getXmlId(), getCategory().getUrlId()));

        setInter(null);
        if (getCategory() != null && getCategory().getTaxonomy().getOpenAnnotation()
                && getCategory().getTagSet().size() == 1) {
            Category category = getCategory();
            setCategory(null);
            category.remove();
        } else {
            setCategory(null);
        }
        setAnnotation(null);

        deleteDomainObject();
    }

    @Override
    public int compareTo(Tag other) {
        return this.getCategory().getName().compareTo(other.getCategory().getName());
    }

}
