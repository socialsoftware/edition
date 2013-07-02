package pt.ist.socialsoftware.edition.domain;


public class Rend extends Rend_Base {

	public enum Rendition {
		RIGHT("right"), LEFT("left"), CENTER("center"), BOLD("bold"), ITALIC(
				"i"), RED("red"), UNDERLINED("u");

		private String desc;

		Rendition(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public Rend(Rendition rend) {
		setRend(rend);
	}

}
