package pt.ist.socialsoftware.edition.ldod.domain.experteditioninter
//package pt.ist.socialsoftware.edition.ldod.domain.experteditioninter
//
//import pt.ist.socialsoftware.edition.ldod.SpockRollbackTestAbstractClass
//import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface
//import pt.ist.socialsoftware.edition.user.domain.User
//import pt.ist.socialsoftware.edition.user.domain.UserModule
//import pt.ist.socialsoftware.edition.virtual.domain.VirtualEdition
//import pt.ist.socialsoftware.edition.virtual.domain.VirtualModule
//
//
//class RemoveMethodSpockTest extends SpockRollbackTestAbstractClass {
//    def ldoD
//    def feTextRequiresInterface
//    def user
//    def expertEdition
//    def virtualEditionOne
//    def virtualEditionTwo
//    def fragment
//    def expertInter
//    def virtualInter
//    def sourceInter
//
//    def populate4Test() {
////        text = TextModule.getInstance()
//        feTextRequiresInterface = new FeTextRequiresInterface();
//        ldoD = VirtualModule.getInstance();
//
////        fragment = new Fragment(text, "Title", "xmlId")
////        expertEdition = text.getJPCEdition()
//
//        fragment = feTextRequiresInterface.createFragment("Title", "xmlId");
//        expertEdition = feTextRequiresInterface.getExpertEditionByAcronym("JPC")
//
//        user = new User(UserModule.getInstance(), "username", "password", "firstName", "lastName", "email")
//    }
//
//    def 'delete expert inter'() {
//        given: 'an expert interpretation'
////        expertInter = new ExpertEditionInter()
////        expertInter.setFragment(fragment)
////        expertInter.setExpertEdition(expertEdition)
////        expertInter.setXmlId("xmlId")
//        expertInter = feTextRequiresInterface.createExpertInter(fragment.getXmlId(), expertEdition.getAcronym(), "xmlId")
//        and: 'a virtual edition containing another interpretation'
//        virtualEditionOne = new VirtualEdition(ldoD, user.getUsername(), "test1", "test1", null, true, expertEdition.getAcronym())
//        and: 'another virtual edition using the previous virtual edition'
//        virtualEditionTwo = new VirtualEdition(ldoD, user.getUsername(), "test2", "test2", null, true, virtualEditionOne.getAcronym())
//
//        when: 'the expert edition interpretation is removed'
//        expertInter.remove()
//
//        then: 'the three interpretations are removed from the fragment'
////        fragment.getScholarInterSet().size() == 0
//        fragment.getScholarInterDtoSet().size() == 0
//        and: 'from each of the editions'
////        expertEdition.getIntersSet().size() == 0
//        expertEdition.getExpertEditionInters().size() == 0
//        virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
//        virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
//    }
//
//    def 'delete source inter'() {
//        given: 'an source interpretation'
////        sourceInter = new SourceInter()
////        sourceInter.setFragment(fragment)
////        sourceInter.setXmlId("sssId")
//        sourceInter = feTextRequiresInterface.createSourceInter(fragment.getXmlId(), "sssId")
//        and: 'a virtual edition containing another interpretation'
//        virtualEditionOne = new VirtualEdition(ldoD, user.getUsername(), "test1", "test1", null, true, null)
//        virtualInter = virtualEditionOne.createVirtualEditionInter(sourceInter, 1)
//        and: 'another virtual edition using the previous virtual edition'
//        virtualEditionTwo = new VirtualEdition(ldoD, user.getUsername(), "test2", "test2", null, true, virtualEditionOne.getAcronym())
//
//        when: 'the source edition interpretation is removed'
//        sourceInter.remove()
//
//        then: 'the three interpretations are removed from the fragment'
//        fragment.getScholarInterDtoSet().size() == 0
//        and: 'from each of the editions'
//        virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
//        virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
//    }
//}
