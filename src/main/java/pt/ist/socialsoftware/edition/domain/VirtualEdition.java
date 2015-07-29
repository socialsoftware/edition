package pt.ist.socialsoftware.edition.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;

public class VirtualEdition extends VirtualEdition_Base {

	public VirtualEdition(LdoD ldod, LdoDUser participant, String acronym,
			String title, LocalDate date, Boolean pub, Edition usedEdition) {
		setLdoD4Virtual(ldod);
		addParticipant(participant);
		setAcronym(acronym);
		setTitle(title);
		setDate(date);
		setPub(pub);
		createSection("Default", 0);
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
		String path = PropertiesManager.getProperties().getProperty(
				"corpus.dir");
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

		for (LdoDUser user : getParticipantSet()) {
			removeParticipant(user);
		}

		for (LdoDUser user : getSelectedBySet()) {
			removeSelectedBy(user);
		}

		for(Section section : getSectionsSet()) {
			section.remove();
		}

		for(RecommendationWeights weights : getRecommendationWeightsSet()) {
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
		List<VirtualEditionInter> interps = new ArrayList<VirtualEditionInter>();

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
		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {
			if (inter.getFragment() == fragment) {
				FragInter usedInter = inter.getLastUsed();
				if (usedAddInter == usedInter) {
					return false;
				}
				if ((usedInter instanceof SourceInter)
						|| (usedAddInter instanceof SourceInter)) {
					return false;
				}

				ExpertEdition addExpertEdition = ((ExpertEditionInter) usedAddInter)
						.getExpertEdition();
				ExpertEdition expertEdition = ((ExpertEditionInter) usedInter)
						.getExpertEdition();
				if (addExpertEdition != expertEdition) {
					return false;
				} else {
					int numberOfInter4Expert = fragment
							.getNumberOfInter4Edition(expertEdition);
					int numberOfInter4Virtual = fragment
							.getNumberOfInter4Edition(this);
					return numberOfInter4Expert > numberOfInter4Virtual;
				}
			}
		}
		return true;
	}

	public int getMaxFragNumber() {
		int max = 0;
		for (FragInter inter : getVirtualEditionIntersSet()) {
			max = (inter.getNumber() > max) ? inter.getNumber() : max;
		}

		return max;
	}

	@Override
	public Set<FragInter> getIntersSet() {
		return new HashSet<FragInter>(getVirtualEditionIntersSet());
	}

	@Override
	public String getReference() {
		return getAcronym();
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(String acronym, String title, boolean pub) {
		setPub(pub);
		setTitle(title);
		setAcronym(acronym);
	}

	// Default section
	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(FragInter inter, int number) {
		VirtualEditionInter virtualInter = null;
		if(getSectionsSet().isEmpty()) {
			if(canAddFragInter(inter)) {
				Section section = new Section(this, "Default", 0);
				virtualInter = new VirtualEditionInter(section, inter, number);
				section.addVirtualEditionInter(virtualInter);
				addSections(section);
			}
		} else {
			if(canAddFragInter(inter)) {
				Section section = getSectionsSet().iterator().next();
				virtualInter = new VirtualEditionInter(section, inter, number);
			}
		}
		return virtualInter;
	}

	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(Section section, FragInter inter, int number) {
		VirtualEditionInter virtualInter = null;
		if(canAddFragInter(inter)) {
			virtualInter = new VirtualEditionInter(section, inter, number);
			section.addVirtualEditionInter(virtualInter);
			addSections(section);
		}
		return virtualInter;
	}

	public boolean checkAccess(LdoDUser user) {
		if(getPub()) {
			return true;
		} else if(getParticipantSet().contains(user)) {
			return true;
		}

		return false;
	}

	public List<VirtualEditionInter> getVirtualEditionIntersSet() {
		List<VirtualEditionInter> inters = new ArrayList<>();
		for(Section section : getSectionsSet()) {
			inters.addAll(section.getInterSet());
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
		for(Section section : getSectionsSet()) {
			if(section.getTitle().equals(title)) {
				return section;
			}
		}

		return null;
	}

	public Section createSection(String title) {
		int number = getSectionsSet().size();
		return createSection(title, number);
	}

	public int getDepth() {
		int max = 0;
		for(Section section : getSectionsSet()) {
			int depth = section.getDepth();
			if(max < depth)
				max = depth;
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
		for(Section section : getSectionsSet()) {
			if(section.size() == 0) {
				section.remove();
			}
		}
	}

	public void print() {
		for(Section section : getSectionsSet()) {
			section.print();
		}

	}

}