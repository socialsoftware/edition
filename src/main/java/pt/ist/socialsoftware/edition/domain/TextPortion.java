package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public abstract class TextPortion extends TextPortion_Base implements
		GraphElement {

	public enum VariationType {
		ORTHOGRAPHIC("orthographic"), SUBSTANTIVE("substantive"), UNSPECIFIED(
				"unspecified");

		private String desc;

		VariationType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public TextPortion() {
		super();
		setNextText(null);
	}

	@Override
	public void addChildText(TextPortion child) {
		super.addChildText(child);

		if (getFirstChildText() == null) {
			child.setPrevText(null);
			child.setNextText(null);
			setFirstChildText(child);
			setLastChildText(child);
		} else {
			child.setPrevText(getLastChildText());
			child.setNextText(null);
			setLastChildText(child);
		}
	}

	@Override
	abstract public void accept(TextTreeVisitor visitor);

	public Set<FragInter> getInterps() {
		if (getParentText() == null) {
			return getFragment().getFragmentInterSet();
		} else {
			return getParentText().getInterps();
		}
	}

	// check if the object is responsible for formating, AddText, DelText, and
	// SegText
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		return false;
	}

	// write the separator if this object is the first responsible for formating
	// a SimpleText
	public String writeSeparator(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		if ((getParentOfFirstText() != null)
				&& (getParentOfFirstText().isFormat(displayDel, highlightSubst,
						fragInter))) {
			return "";
		} else {
			return getSeparator(fragInter);
		}
	}

	public SimpleText getNextSimpleText(FragInter inter) {
		SimpleText nextSimpleText = null;

		// check children
		if ((nextSimpleText == null) && (getFirstChildText() != null)
				&& (getFirstChildText().getInterps().contains(inter))) {
			nextSimpleText = getFirstChildText().getNextSimpleText(inter);
		}

		// check next
		if ((nextSimpleText == null) && (getNextText() != null)) {
			nextSimpleText = super.getNextText().getNextSimpleText(inter);
		}

		// check next of parent
		if ((nextSimpleText == null) && (getNextOfParentText() != null)) {
			nextSimpleText = getNextOfParentText().getNextSimpleText(inter);
		}

		return nextSimpleText;
	}

	private TextPortion getNextOfParentText() {
		if (getParentText() == null) {
			return null;
		} else if (getParentText().getNextText() != null) {
			return getParentText().getNextText();
		} else {
			return getParentText().getNextOfParentText();
		}
	}

	// returns the value of getBreakWord() of the closest predecessor lbText of
	// a simpleText and true if there is another simpleText between
	public Boolean getBreakWord() {
		if (getPrevText() != null) {
			return getPrevText().getBreakWord();
		}

		if (getParentText() != null) {
			return getParentText().getBreakWord();
		}

		return true;
	}

	// goes through the tree of text to find the next instance of SimpleText and
	// requests the separator
	public String getSeparator(FragInter inter) {
		SimpleText simpleText = getNextSimpleText(inter);

		if (simpleText != null) {
			return simpleText.getSeparator(inter);
		} else {
			return "";
		}
	}

	public RdgText getClosestRdg() {
		return getParentText().getClosestRdg();
	}

	public void remove() {
		for (TextPortion text : getChildTextSet()) {
			text.remove();
		}

		for (PhysNote physNote : getPhysNoteSet()) {
			removePhysNote(physNote);
		}

		for (Rend rend : getRendSet()) {
			rend.remove();
		}

		setFragment(null);
		setPrevText(null);
		setNextText(null);
		setParentOfFirstText(null);
		setParentOfLastText(null);
		setParentText(null);

		deleteDomainObject();
	}

	public String getNote() {
		return null;
	}

}
