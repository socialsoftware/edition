package pt.ist.socialsoftware.edition.ldod.domain.experteditioninter

import org.joda.time.LocalDate

import pt.ist.socialsoftware.edition.ldod.domain.*

import pt.ist.socialsoftware.edition.ldod.SpockRollbackTestAbstractClass

class RemoveMethodSpockTest extends SpockRollbackTestAbstractClass  {
	def ldoD
	def user
	def expertEdition
	def virtualEdition
	def fragment
	def expertInter
	def virtualInter
	
	def populate4Test() {
		ldoD = LdoD.getInstance();
		
		fragment = new Fragment(ldoD, "Title", "xmlId")
		
		expertEdition = ldoD.getJPCEdition()
		
		user = new LdoDUser(ldoD, "username", "password", "firstName", "lastName", "email")
	}

	def 'delete expert inter'() {
		given: 'an expert interpretation'
		expertInter = new ExpertEditionInter()
		expertInter.setFragment(fragment)
		expertInter.setExpertEdition(expertEdition)
		and: 'a virtual edition containing another interpretation'
		virtualEdition = new VirtualEdition(ldoD, user, "test1", "test1", null, true, expertEdition)
		and: 'another virtual edition using the previous virtual edition'
		new VirtualEdition(ldoD, user, "test2", "test2", null, true, virtualEdition)
		
		when: 'the expert edition interpretation is removed'
		expertInter.remove()

		then: 'the three interpretations are removed from the fragment'
		fragment.getFragmentInterSet().size() == 0
	}

}
