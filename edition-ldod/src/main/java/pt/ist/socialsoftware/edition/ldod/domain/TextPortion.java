package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.ldod.domain.TextPortion_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public abstract class TextPortion extends TextPortion_Base implements GraphElement {
	private static Logger logger = LoggerFactory.getLogger(TextPortion.class);

	public enum VariationType {
		ORTHOGRAPHIC("orthographic"), SUBSTANTIVE("substantive"), STYLE("style"), PARAGRAPH("paragraph"), PUNCTUATION(
				"punctuation"), UNSPECIFIED("unspecified");

		private final String desc;

		VariationType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return this.desc;
		}
	};

	public TextPortion() {
		super();
		setNextText(null);
		setFragment(null);
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

	// it is a template method that is redefined in AppText, RdgGrpText, and
	// RdgText
	public TextPortion getNextDepthFirstText(FragInter inter) {
		TextPortion nextDepthFirstText = null;

		// check children
		nextDepthFirstText = getNextChildText(inter);
		if (nextDepthFirstText != null) {
			return nextDepthFirstText;
		}

		// check next
		nextDepthFirstText = getNextSibilingText(inter);
		if (nextDepthFirstText != null) {
			return nextDepthFirstText;
		}

		// check next of parent
		return getNextParentText(inter);
	}

	// it is redefined in AppText and RdgGrpText
	protected TextPortion getNextChildText(FragInter inter) {
		if (this.getInterps().contains(inter)) {
			TextPortion childText = getFirstChildText();
			if (childText != null) {
				if (childText.getInterps().contains(inter)) {
					return childText;
				} else {
					return childText.getNextDepthFirstText(inter);
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	// it is redefined in RdgGrpText and RdgText
	protected TextPortion getNextSibilingText(FragInter inter) {
		TextPortion nextText = getNextText();
		if (nextText != null) {
			if (nextText.getInterps().contains(inter)) {
				return nextText;
			} else {
				return nextText.getNextDepthFirstText(inter);
			}
		} else {
			return null;
		}
	}

	// it is redefined in RdgGrpText and RdgText
	protected TextPortion getNextParentText(FragInter inter) {
		TextPortion parentText = getParentText();
		if (parentText != null) {
			TextPortion nextText = getNextSibilingText(inter);
			if (nextText != null) {
				return nextText;
			}
			return parentText.getNextParentText(inter);
		} else {
			return null;
		}
	}

	// it is redefined in RdgGrpText and RdgText
	protected TextPortion getBacktrackingNextOfParentText(FragInter inter) {
		// check next
		TextPortion nextText = getNextSibilingText(inter);
		if (nextText != null) {
			return nextText;
		}

		return getNextParentText(inter);
	}

	protected boolean isTransitiveParent(TextPortion textPortion) {
		if (textPortion.getParentText() == null) {
			return false;
		}
		if (textPortion.getParentText() == this) {
			return true;
		}
		return isTransitiveParent(textPortion.getParentText());

	}

	@Override
	public abstract void accept(TextPortionVisitor visitor);

	public Set<FragInter> getInterps() {
		if (getParentText() == null) {
			return getFragment().getFragmentInterSet();
		} else {
			return getParentText().getInterps();
		}
	}

	// check if the object is responsible for formating, AddText, DelText, and
	// SegText
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst, FragInter fragInter) {
		return false;
	}

	// write the separator if this object is the first responsible for formating
	// a SimpleText
	public String writeSeparator(Boolean displayDel, Boolean highlightSubst, FragInter fragInter) {
		if ((getParentOfFirstText() != null)
				&& (getParentOfFirstText().isFormat(displayDel, highlightSubst, fragInter))) {
			return "";
		} else {
			return getSeparator(fragInter);
		}
	}

	public SimpleText getNextSimpleText(FragInter inter) {
		TextPortion nextText = getNextDepthFirstText(inter);
		if (nextText != null) {
			return nextText.getNextSimpleText(inter);
		}

		return null;
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

	public void putAppTextWithVariations(List<AppText> apps, List<FragInter> inters) {
		for (TextPortion text : getChildTextSet()) {
			text.putAppTextWithVariations(apps, inters);
		}
	}

	public boolean hasVariations(List<FragInter> inters) {
		return false;
	}

	public TextPortion getTopParent() {
		if (getParentText() != null) {
			return getParentText().getTopParent();
		} else {
			return this;
		}
	}

	public SimpleText getSimpleText(FragInter inter, int currentOffset, int offset) {
		logger.debug("getSimpleText currentOffset:{}, offset:{}", currentOffset, offset);

		if (getNextSimpleText(inter) != null) {
			return getNextSimpleText(inter).getSimpleText(inter, currentOffset, offset);
		} else {
			return null;
		}
	}
}
