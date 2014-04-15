package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class RefText extends RefText_Base {

	public enum RefType {
		GRAPHIC("graphic"), WITNESS("witness");

		private final String desc;

		RefType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public RefText(TextPortion parent, RefType type, String target) {
		parent.addChildText(this);
		setType(type);
		setTarget(target);
		setSurface(null);
		setFragInter(null);
	}

	@Override
	public void remove() {
		setSurface(null);
		setFragInter(null);

		super.remove();
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

}
