package pt.ist.socialsoftware.edition.ldod.domain.experteditioninter

import pt.ist.socialsoftware.edition.ldod.domain.*

import pt.ist.socialsoftware.edition.ldod.SpockRollbackTestAbstractClass

class RemoveMethodSpockTest extends SpockRollbackTestAbstractClass  {
	def ldoD
	def text
	def user
	def expertEdition
	def virtualEditionOne
	def virtualEditionTwo
	def fragment
	def expertInter
	def virtualInter
	def sourceInter
	
	def populate4Test() {
		ldoD = LdoD.getInstance();
		text = Text.getInstance()
		
		fragment = new Fragment(text, "Title", "xmlId")

		expertEdition = text.getJPCEdition()
		
		user = new LdoDUser(ldoD, "username", "password", "firstName", "lastName", "email")
	}

	def 'delete expert inter'() {
		given: 'an expert interpretation'
		expertInter = new ExpertEditionInter()
		expertInter.setFragment(fragment)
		expertInter.setExpertEdition(expertEdition)
		expertInter.setXmlId("xmlId")
		and: 'a virtual edition containing another interpretation'
		virtualEditionOne = new VirtualEdition(ldoD, user, "test1", "test1", null, true, expertEdition)
		and: 'another virtual edition using the previous virtual edition'
		virtualEditionTwo = new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEditionOne)
		
		when: 'the expert edition interpretation is removed'
		expertInter.remove()

		then: 'the three interpretations are removed from the fragment'
		fragment.getFragmentInterSet().size() == 0
		and: 'from each of the editions'
		expertEdition.getIntersSet().size() == 0
		virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
		virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
	}

    def 'delete source inter'() {
        given: 'an source interpretation'
        sourceInter = new SourceInter()
        sourceInter.setFragment(fragment)
        sourceInter.setXmlId("sssId")
        and: 'a virtual edition containing another interpretation'
        virtualEditionOne = new VirtualEdition(ldoD, user, "test1", "test1", null, true, null)
        virtualInter =  virtualEditionOne.createVirtualEditionInter(sourceInter,1)
        and: 'another virtual edition using the previous virtual edition'
        virtualEditionTwo = new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEditionOne)

        when: 'the source edition interpretation is removed'
        sourceInter.remove()

        then: 'the three interpretations are removed from the fragment'
        fragment.getFragmentInterSet().size() == 0
        and: 'from each of the editions'
        virtualEditionOne.getAllDepthVirtualEditionInters().size() == 0
        virtualEditionTwo.getAllDepthVirtualEditionInters().size() == 0
    }
}
