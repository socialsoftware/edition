package pt.ist.socialsoftware.edition.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.recommendation.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class VirtualEdition extends VirtualEdition_Base {
	private static Logger logger = LoggerFactory.getLogger(VirtualEdition.class);

	public static String ACRONYM_PREFIX = "LdoD-";

	@Override
	public void setAcronym(String acronym) {
		if (acronym.split("\\s+").length != 1) {
			throw new LdoDException("acronym");
		}
		if (acronym.contains(".")) {
			throw new LdoDException("acronym");
		}

		// cannot change acronym of the archive edition
		if (getAcronym() == null || !getAcronym().equals(ARCHIVE_EDITION_ACRONYM)) {
			super.setAcronym(acronym);
		}
	}

	public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym, String title, LocalDate date, Boolean pub,
			Edition usedEdition) {
		setLdoD4Virtual(ldod);
		new Member(this, participant, MemberRole.ADMIN, true);
		setAcronym(acronym);
		setTitle(title);
		setDate(date);
		setPub(pub);
		setTaxonomy(new Taxonomy());
		createSection(Section.DEFAULT, 0);
		if (usedEdition != null) {
			for (FragInter inter : usedEdition.getIntersSet()) {
				createVirtualEditionInter(inter, inter.getNumber());
			}
		}
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		// delete directory and all its files if it exists
		String path = PropertiesManager.getProperties().getProperty("corpus.dir");
		File directory = new File(path + getExternalId());
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				// Unable to delete directory
				e.printStackTrace();
			}
		}

		setLdoD4Virtual(null);

		getTaxonomy().remove();

		getMemberSet().stream().forEach(m -> m.remove());

		for (LdoDUser user : getSelectedBySet()) {
			removeSelectedBy(user);
		}

		for (Section section : getSectionsSet()) {
			section.remove();
		}

		for (RecommendationWeights weights : getRecommendationWeightsSet()) {
			weights.remove();
		}

		super.remove();
	}

	@Override
	public void setPub(Boolean pub) {
		if (!pub) {
			Set<LdoDUser> participants = getParticipantSet();
			for (LdoDUser user : getSelectedBySet()) {
				if (!participants.contains(user)) {
					this.removeSelectedBy(user);
				}
			}
		}
		super.setPub(pub);
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.VIRTUAL;
	}

	public List<VirtualEditionInter> getSortedInter4Frag(Fragment fragment) {
		List<VirtualEditionInter> interps = new ArrayList<>();

		for (FragInter inter : fragment.getFragmentInterSet()) {
			if ((inter.getSourceType() == EditionType.VIRTUAL)
					&& ((VirtualEditionInter) inter).getVirtualEdition() == this) {
				interps.add((VirtualEditionInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;
	}

	// determines if the fragment can have more interpretations for this virtual
	// edition, deals with the the case of a fragment having two interpretations
	// for the same expert edition
	public Boolean canAddFragInter(FragInter addInter) {
		Fragment fragment = addInter.getFragment();
		FragInter usedAddInter = addInter.getLastUsed();
		for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
			if (inter.getFragment() == fragment) {
				FragInter usedInter = inter.getLastUsed();
				if (isSameInterpretation(usedAddInter, usedInter)) {
					return false;
				}

				if (atLeastOneIsSourceInterpretation(usedAddInter, usedInter)) {
					return false;
				}

				if (belongToDifferentExpertEditions(usedAddInter, usedInter)) {
					return false;
				}

				ExpertEdition expertEdition = ((ExpertEditionInter) usedInter).getExpertEdition();
				int numberOfInter4Expert = fragment.getNumberOfInter4Edition(expertEdition);
				int numberOfInter4Virtual = fragment.getNumberOfInter4Edition(this);
				return numberOfInter4Expert > numberOfInter4Virtual;
			}
		}
		return true;
	}

	private boolean belongToDifferentExpertEditions(FragInter usedAddInter, FragInter usedInter) {
		ExpertEdition addExpertEdition = ((ExpertEditionInter) usedAddInter).getExpertEdition();
		ExpertEdition expertEdition = ((ExpertEditionInter) usedInter).getExpertEdition();
		return (addExpertEdition != expertEdition);
	}

	public boolean atLeastOneIsSourceInterpretation(FragInter usedAddInter, FragInter usedInter) {
		return (usedInter instanceof SourceInter) || (usedAddInter instanceof SourceInter);
	}

	public boolean isSameInterpretation(FragInter usedAddInter, FragInter usedInter) {
		return (usedAddInter == usedInter);
	}

	public int getMaxFragNumber() {
		int max = 0;
		for (FragInter inter : getAllDepthVirtualEditionInters()) {
			max = (inter.getNumber() > max) ? inter.getNumber() : max;
		}

		return max;
	}

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<>(getAllDepthVirtualEditionInters());
	}

	@Override
	public String getReference() {
		return getAcronym();
	}

	public String getShortAcronym() {
		return getAcronym().substring(ACRONYM_PREFIX.length());
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(String acronym, String title, boolean pub, boolean openManagement, boolean openVocabulary,
			boolean openAnnotation) {
		setPub(pub);
		setTitle(title);
		setAcronym(acronym);
		getTaxonomy().edit(openManagement, openVocabulary, openAnnotation);
	}

	@Atomic(mode = TxMode.WRITE)
	public void updateVirtualEditionInters(String fraginters) {
		logger.debug("updateVirtualEditionInters fragintters:{}", fraginters);

		List<String> fragInterList = Arrays.stream(fraginters.trim().split(";")).map(item -> item.trim())
				.filter(item -> !item.equals("")).collect(Collectors.toList());
		List<String> newFragList = new ArrayList<>();
		List<String> actualFragList = new ArrayList<>();

		// inicializa lista de frags
		for (String temp : fragInterList) {
			FragInter inter = FenixFramework.getDomainObject(temp);

			logger.debug("updateVirtualEditionInters temp:{} interLastUsed:{} interTitle:{} interSourceType:{}", temp,
					inter.getLastUsed().getExternalId(), inter.getTitle(), inter.getSourceType());

			newFragList.add(inter.getLastUsed().getExternalId());
		}

		String fragVirtualInterId = "";
		// remove os fragmentos que não se encontram na nova lista
		for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {

			System.out.println(inter.getExternalId() + " " + inter.getLastUsed().getExternalId() + " "
					+ inter.getTitle() + " " + inter.getNumber());

			// fragVirtualInterId = inter.getFragment().getExternalId();

			System.out.println("V1");
			String id = inter.getLastUsed().getExternalId();
			System.out.println("V2");
			actualFragList.add(id);

			if (!newFragList.contains(id)) {
				inter.remove();
			}
		}

		// adicionar os novos fragmentos
		/*
		 * int i = 0; for (String fragId : newFragList) { if
		 * (!actualFragList.contains(fragId)) { FragInter inter =
		 * FenixFramework.getDomainObject(fragInterList .get(i));
		 * VirtualEditionInter addInter = createVirtualEditionInter(inter, i); }
		 * i++; }
		 */

		System.out.println("V3");

		int i = 0;
		for (String fragInter : newFragList) {
			System.out.println("V4 " + fragInter);
			if (!actualFragList.contains(fragInter)) {
				System.out.println("V5 " + fragInter);
				FragInter inter = FenixFramework.getDomainObject(fragInter);
				createVirtualEditionInter(inter, i + 1);
				System.out.println("V6 " + fragInter);
			}
			i++;
		}

		System.out.println("VirtualEditionInter INDEX UPDATE -----------------");
		// actualiza indices dos fragmentos da edicao virtual
		int n = 0;
		for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {

			fragVirtualInterId = inter.getLastUsed().getExternalId();

			if (newFragList.contains(fragVirtualInterId)) {
				n = newFragList.indexOf(fragVirtualInterId) + 1;
				inter.setNumber(n);
				System.out.println(inter.getTitle() + " " + n);
			} else {
				System.out.println("NOT " + inter.getTitle() + " " + inter.getNumber());
			}

		}

		System.out.println("UPDATE2");

		/*
		 * int i = 1;
		 * 
		 * StringTokenizer st = new StringTokenizer(fraginters, ";"); while
		 * (st.hasMoreElements()) { String fraginterId =
		 * st.nextElement().toString(); if (fraginterId.length() > 0) {
		 * FragInter inter = FenixFramework.getDomainObject(fraginterId);
		 * VirtualEditionInter addInter = createVirtualEditionInter(inter, i);
		 * i++; }
		 * 
		 * }
		 */

	}

	// Default section
	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(FragInter inter, int number) {
		VirtualEditionInter virtualInter = null;

		if (canAddFragInter(inter)) {
			if (getSectionsSet().isEmpty()) {
				Section section = new Section(this, Section.DEFAULT, 0);
				virtualInter = new VirtualEditionInter(section, inter, number);
				section.addVirtualEditionInter(virtualInter);
				addSections(section);
			} else {
				Section section = getSectionsSet().iterator().next();
				virtualInter = new VirtualEditionInter(section, inter, number);
			}
		}

		return virtualInter;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(Section section, FragInter inter, int number) {
		VirtualEditionInter virtualInter = null;
		if (canAddFragInter(inter)) {
			virtualInter = new VirtualEditionInter(section, inter, number);
			section.addVirtualEditionInter(virtualInter);
			addSections(section);
		}
		return virtualInter;
	}

	public boolean checkAccess() {
		return getPub() || getParticipantSet().contains(LdoDUser.getAuthenticatedUser());
	}

	public List<VirtualEditionInter> getAllDepthVirtualEditionInters() {
		List<VirtualEditionInter> inters = new ArrayList<>();
		for (Section section : getSectionsSet()) {
			inters.addAll(section.getAllDepthVirtualEditionInterSet());
		}
		Collections.sort(inters);
		return inters;
	}

	@Atomic(mode = TxMode.WRITE)
	public Section createSection(String title, int number) {
		Section section = new Section(this, title, number);
		return section;
	}

	public boolean hasMultipleSections() {
		return getSectionsSet().size() > 1;
	}

	public Section getSection(String title) {
		for (Section section : getSectionsSet()) {
			if (section.getTitle().equals(title)) {
				return section;
			}
		}
		return null;
	}

	public Section createSection(String title) {
		int number = getSectionsSet().size();
		return createSection(title, number);
	}

	public int getSectionDepth() {
		int max = 0;
		for (Section section : getSectionsSet()) {
			int depth = section.getDepth();
			if (max < depth) {
				max = depth;
			}
		}
		return max;
	}

	public List<Section> getSortedSections() {
		List<Section> sortedList = new ArrayList<>(getSectionsSet());
		Collections.sort(sortedList);
		return sortedList;
	}

	@Atomic(mode = TxMode.WRITE)
	public void clearEmptySections() {
		for (Section section : getSectionsSet()) {
			section.clearEmptySections();
		}

		for (Section section : getSectionsSet()) {
			if (section.getAllDepthVirtualEditionInterSet().size() == 0) {
				section.remove();
			}
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void removeMember(LdoDUser user) {
		getMemberSet().stream().filter(m -> m.getUser() == user).forEach(m -> m.remove());
		removeSelectedBy(user);
	}

	@Atomic(mode = TxMode.WRITE)
	public void addMember(LdoDUser user, MemberRole role, boolean active) {
		if (!getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().isPresent()) {
			new Member(this, user, role, active);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void cancelParticipationSubmission(LdoDUser user) {
		Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst()
				.orElse(null);
		if (member != null) {
			member.remove();
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void addApprove(LdoDUser user) {
		Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser() == user).findFirst()
				.orElse(null);
		if (member != null) {
			member.setActive(true);
		}
	}

	@Atomic(mode = TxMode.WRITE)
	public void switchRole(LdoDUser user) {
		Member member = getMemberSet().stream().filter(m -> m.getUser() == user).findFirst().orElse(null);
		if (member != null) {
			if (member.getRole().equals(MemberRole.ADMIN)) {
				member.setRole(MemberRole.MEMBER);
			} else {
				member.setRole(MemberRole.ADMIN);
			}
		}
	}

	public Set<LdoDUser> getParticipantSet() {
		return getMemberSet().stream().filter(m -> m.getActive()).map(m -> m.getUser()).collect(Collectors.toSet());
	}

	public Set<Member> getActiveMemberSet() {
		return getMemberSet().stream().filter(m -> m.getActive()).collect(Collectors.toSet());
	}

	public Set<LdoDUser> getAdminSet() {
		return getMemberSet().stream().filter(m -> m.getRole().equals(MemberRole.ADMIN) && m.getActive())
				.map(m -> m.getUser()).collect(Collectors.toSet());
	}

	public Set<Member> getAdminMemberSet() {
		return getMemberSet().stream().filter(m -> m.getRole().equals(MemberRole.ADMIN) && m.getActive())
				.collect(Collectors.toSet());
	}

	public Set<LdoDUser> getPendingSet() {
		return getMemberSet().stream().filter(m -> !m.getActive()).map(m -> m.getUser()).collect(Collectors.toSet());
	}

	public Set<Member> getPendingMemberSet() {
		return getMemberSet().stream().filter(m -> !m.getActive()).collect(Collectors.toSet());
	}

	public boolean canRemoveMember(LdoDUser actor, LdoDUser user) {
		MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser() == actor).map(m -> m.getRole())
				.findFirst().get();

		if (roleActor.equals(MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
			return true;
		}

		if (roleActor.equals(MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && actor != user) {
			return true;
		}

		if (roleActor.equals(MemberRole.MEMBER) && actor == user) {
			return true;
		}

		return false;
	}

	public boolean canSwitchRole(LdoDUser actor, LdoDUser user) {
		MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser() == actor).map(m -> m.getRole())
				.findFirst().get();

		if (roleActor.equals(MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
			return true;
		}

		if (roleActor.equals(MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && actor != user) {
			return true;
		}

		return false;
	}

	public List<VirtualEditionInter> generateRecommendation(VirtualEditionInter inter,
			RecommendationWeights recommendationWeights) {
		List<VirtualEditionInter> inters = getAllDepthVirtualEditionInters();
		VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();

		inters.remove(inter);

		List<Property> properties = recommendationWeights.getPropertiesWithStoredWeights();
		List<VirtualEditionInter> recommendedEdition = new ArrayList<>();
		recommendedEdition.add(inter);
		recommendedEdition.addAll(recommender.getMostSimilarItemsAsList(inter, inters, properties));
		return recommendedEdition;
	}

	public Set<Category> getAllDepthCategories() {
		Set<Category> result = new HashSet<>(getTaxonomy().getCategoriesSet());

		Edition usedEdition = getUses();
		while (usedEdition instanceof VirtualEdition) {
			result.addAll(((VirtualEdition) usedEdition).getTaxonomy().getCategoriesSet());
			usedEdition = ((VirtualEdition) usedEdition).getUses();
		}

		return result;
	}

	public List<Category> getAllDepthSortedCategories() {
		return getAllDepthCategories().stream().sorted((c1, c2) -> c1.getName().compareTo(c2.getName()))
				.collect(Collectors.toList());
	}

}