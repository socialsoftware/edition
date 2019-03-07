package pt.ist.socialsoftware.edition.ldod.domain.experteditioninter


import pt.ist.socialsoftware.edition.ldod.domain.*

import pt.ist.socialsoftware.edition.ldod.SpockRollbackTestAbstractClass

class RemoveMethodSpockTest extends SpockRollbackTestAbstractClass  {
	def ldoD
	def user
	def expertEdition
	def virtualEditionOne
	def virtualEditionTwo
	def fragmentOne
	def fragmentTwo
	def expertInterOne
	def expertInterTwo
	def sourceInter
	def virtualInter

	def populate4Test() {
		ldoD = LdoD.getInstance();
		
		fragmentOne = new Fragment(ldoD, "Title", "xmlId1")
		fragmentTwo = new Fragment(ldoD, "Titulo", "xmlId2")

		expertEdition = ldoD.getJPCEdition()
		
		user = new LdoDUser(ldoD, "username", "password", "firstName", "lastName", "email")
	}

	def 'delete expert inter'() {
		given: 'an expert interpretation'
		expertInterOne = new ExpertEditionInter()
		expertInterOne.setFragment(fragmentOne)
		expertInterOne.setExpertEdition(expertEdition)
        expertInterOne.setXmlId("xmlId")
		and: 'a virtual edition containing another interpretation'
		virtualEditionOne = new VirtualEdition(ldoD, user, "test1", "test1", null, true, expertEdition)
		and: 'another virtual edition using the previous virtual edition'
		virtualEditionTwo = new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEditionOne)
		
		when: 'the expert edition interpretation is removed'
		expertInterOne.remove()

		then: 'the three interpretations are removed from the fragment'
		fragmentOne.getFragmentInterSet().size() == 0
		and: 'from each of the editions'
		expertEdition.getIntersSet().size() == 0
		virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
		virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
	}

    def 'delete source inter'() {
        given: 'an source interpretation'
        sourceInter = new SourceInter()
        sourceInter.setFragment(fragmentOne)
        sourceInter.setXmlId("sssId")
        and: 'a virtual edition containing another interpretation'
        virtualEditionOne = new VirtualEdition(ldoD, user, "test1", "test1", null, true, null)
        virtualInter =  virtualEditionOne.createVirtualEditionInter(sourceInter,1)
        and: 'another virtual edition using the previous virtual edition'
        virtualEditionTwo = new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEditionOne)

        when: 'the source edition interpretation is removed'
        sourceInter.remove()

        then: 'the three interpretations are removed from the fragment'
        fragmentOne.getFragmentInterSet().size() == 0
        and: 'from each of the editions'
        virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
        virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
    }

	/*def 'delete multiple expert inter'() {
		given: 'two expert interpretations'
		expertInterOne = new ExpertEditionInter()
		expertInterOne.setFragment(fragmentOne)
		expertInterOne.setExpertEdition(expertEdition)
		expertInterOne.setXmlId("xml1")
		expertInterTwo = new ExpertEditionInter()
		expertInterTwo.setFragment(fragmentTwo)
		expertInterTwo.setExpertEdition(expertEdition)
		expertInterTwo.setXmlId("xml2")

		and: 'a virtual edition containing another interpretation'
		virtualEditionOne = new VirtualEdition(ldoD, user, "test1", "test1", null, true, expertEdition)
		and: 'another virtual edition using the previous virtual edition'
		virtualEditionTwo = new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEditionOne)

		when: "the first interpretation is removed"
		expertInterOne.remove()

		then: "the two virtual interpretations based on it are also removed"
		fragmentOne.getFragmentInterSet().size() == 0
		fragmentTwo.getFragmentInterSet().size() == 3
		and: 'from each of the editions'
		virtualEditionOne.getAllDepthVirtualEditionInters().size() == 3
		virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 3
	}*/


}
