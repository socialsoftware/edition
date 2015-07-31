package pt.ist.socialsoftware.edition.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
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

		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {
			inter.remove();
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
	public void edit(String acronym, String title, boolean pub,
			String fraginters) {
		setPub(pub);
		setTitle(title);
		setAcronym(acronym);
		updateVirtualEditionInters(fraginters);
	}

	@Atomic(mode = TxMode.WRITE)
	public void updateVirtualEditionInters(String fraginters) {

		String[] items = fraginters.split(";");
		List<String> fragInterList = Arrays.asList(items);
		List<String> newFragList = new ArrayList<String>();
		List<String> actualFragList = new ArrayList<String>();

		System.out.println("FRAGMENTS");
		// inicializa lista de frags
		for (String temp : fragInterList) {
			FragInter inter = FenixFramework.getDomainObject(temp);

			System.out.println(temp + " " + inter.getLastUsed().getExternalId()
					+ " " + inter.getTitle() + " " + inter.getSourceType());

			newFragList.add(inter.getLastUsed().getExternalId());
		}

		String fragVirtualInterId = "";
		System.out.println("VirtualEditionInter");
		// remove os fragmentos que não se encontram na nova lista
		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {

			System.out.println(inter.getExternalId() + " "
					+ inter.getLastUsed().getExternalId() + " "
					+ inter.getTitle() + " " + inter.getNumber());

			// fragVirtualInterId = inter.getFragment().getExternalId();

			System.out.println("V1");
			String id = inter.getLastUsed().getExternalId();
			System.out.println("V2");
			actualFragList.add(id);

			if (!newFragList.contains(id)) {
				System.out.println("-----------REMOVE "
						+ inter.getLastUsed().getExternalId() + " "
						+ inter.getTitle());
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

		System.out
				.println("VirtualEditionInter INDEX UPDATE -----------------");
		// actualiza indices dos fragmentos da edicao virtual
		int n = 0;
		for (VirtualEditionInter inter : getVirtualEditionIntersSet()) {

			fragVirtualInterId = inter.getLastUsed().getExternalId();

			if (newFragList.contains(fragVirtualInterId)) {
				n = newFragList.indexOf(fragVirtualInterId) + 1;
				inter.setNumber(n);
				System.out.println(inter.getTitle() + " " + n);
			} else {
				System.out.println("NOT " + inter.getTitle() + " "
						+ inter.getNumber());
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

	@Atomic(mode = TxMode.WRITE)
	public VirtualEditionInter createVirtualEditionInter(FragInter inter,
			int number) {
		VirtualEditionInter virtualInter = null;
		if (canAddFragInter(inter)) {
			virtualInter = new VirtualEditionInter(this, inter, number);
		}
		return virtualInter;
	}

	public boolean checkAccess(LdoDUser user) {
		if (getPub()) {
			return true;
		} else if (getParticipantSet().contains(user)) {
			return true;
		}

		return false;
	}
}
