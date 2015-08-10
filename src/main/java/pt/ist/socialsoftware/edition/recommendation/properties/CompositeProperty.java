package pt.ist.socialsoftware.edition.recommendation.properties;

public abstract class CompositeProperty extends StorableProperty {
	protected final HeteronymProperty heteronymProperty;
	protected final DateProperty dateProperty;

	public CompositeProperty() {
		super();
		heteronymProperty = new HeteronymProperty();
		dateProperty = new DateProperty();
	}

	public CompositeProperty(double weight) {
		super(weight);
		heteronymProperty = new HeteronymProperty(1.);
		dateProperty = new DateProperty(1.);
	}

}
