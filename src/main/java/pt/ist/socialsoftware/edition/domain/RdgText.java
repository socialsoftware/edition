package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class RdgText extends RdgText_Base implements GraphElement {

	public enum RdgTextType {
		NO_TYPE, ORTHOGRAPHIC
	};

	public RdgText() {
		super();
		setType(RdgTextType.NO_TYPE);
	}

	public RdgText(TextPortion parent, Set<FragInter> fragInters) {
		parent.addChildText(this);
		setType(RdgTextType.NO_TYPE);

		for (FragInter inter : fragInters) {
			addFragInters(inter);
		}
	}

	@Override
	public Set<FragInter> getInterps() {
		return getFragIntersSet();
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public Boolean isFormat(Boolean displayDel, Boolean highlightSubst,
			FragInter fragInter) {
		return true;
	}

	@Override
	public RdgText getClosestRdg() {
		return this;
	}

	public void removeOnlyThis() {

		for (FragInter fragInter : getFragIntersSet()) {
			removeFragInters(fragInter);
		}

		deleteDomainObject();
	}

	@Override
	public void remove() {
		for (FragInter inter : getFragIntersSet()) {
			removeFragInters(inter);
		}

		super.remove();
	}

}
