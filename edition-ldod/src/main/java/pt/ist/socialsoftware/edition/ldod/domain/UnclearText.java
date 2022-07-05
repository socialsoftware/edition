package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.UnclearText_Base;
import pt.ist.socialsoftware.edition.ldod.generators.TextPortionVisitor;

public class UnclearText extends UnclearText_Base {

	public enum UnclearReason {
		IRRELEVANT("irrelevant"), ILLEGIABLE("illegible"), BACKGROUND_NOISE(
				"background noise"), ECCENTRIC_DUCTUS("eccentric ductus"), INK_BLOT(
				"ink blot"), FADED("faded"), NONSPECIFIED("nonspecified");

		private final String desc;

		UnclearReason(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public UnclearText(TextPortion parent, UnclearReason reason) {
		parent.addChildText(this);

		setReason(reason);
	}

	@Override
	public void accept(TextPortionVisitor visitor) {
		visitor.visit(this);
	}
}
