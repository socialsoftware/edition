package pt.ist.socialsoftware.edition.virtual.domain;


import pt.ist.socialsoftware.edition.notification.utils.LdoDException;

public abstract class SocialMediaCriteria extends SocialMediaCriteria_Base {

//    protected void init(VirtualEdition edition, Class<?> clazz) {
//        checkUniqueCriteriaType(edition, clazz);
//        setVirtualEdition(edition);
//    }
//
//    protected void checkUniqueCriteriaType(VirtualEdition edition, Class<?> clazz) {
//        if (edition.getCriteriaSet().stream().filter(clazz::isInstance).findFirst().isPresent()) {
//            throw new LdoDException("THIS CRITERION ALREADY EXISTS!!");
//        }
//    }
//
//    public void remove() {
//        setVirtualEdition(null);
//
//        deleteDomainObject();
//    }

    public void init(VirtualEdition edition, Class<?> clazz) {
        checkUniqueCriteriaType(edition, clazz);
        setVirtualEdition(edition);
    }

    public void checkUniqueCriteriaType(VirtualEdition edition, Class<?> clazz) {
        if (edition.getCriteriaSet().stream().filter(clazz::isInstance).findFirst().isPresent()) {
            throw new LdoDException("THIS CRITERION ALREADY EXISTS!!");
        }
    }

    public void remove() {
        setVirtualEdition(null);

        deleteDomainObject();
    }

}
