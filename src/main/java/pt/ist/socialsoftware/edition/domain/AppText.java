package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.socialsoftware.edition.visitors.TextTreeVisitor;

public class AppText extends AppText_Base {

	public AppText(TextPortion parent, VariationType type) {
		if (parent != null) {
			parent.addChildText(this);
		}

		setType(type);
	}

	public AppText(VariationType type) {
		setParentText(null);

		setType(type);
	}

	@Override
	public void accept(TextTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public void getAppText(List<AppText> apps) {
		apps.add(this);
	}

}
