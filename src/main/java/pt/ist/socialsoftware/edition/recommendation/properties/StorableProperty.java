package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.recommendation.StoredVectors;

public abstract class StorableProperty extends Property {

	public StorableProperty() {
		super();
	}

	public StorableProperty(double weight) {
		super(weight);
	}

	private Collection<Double> applyWeights(Collection<Double> collection, double weight) {
		if (weight == 1.0) {
			return new ArrayList<>(collection);
		} else if (weight == 0.0) {
			return new ArrayList<>(getDefaultVector());
		} else {
			List<Double> list = new ArrayList<>(collection);
			int size = list.size();
			Double value;
			for (int i = 0; i < size; i++) {
				value = list.get(i);
				list.set(i, weight * value);
			}
			return list;
		}
	}

	@Override
	public final Collection<Double> visit(ExpertEditionInter expertEditionInter) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, expertEditionInter.getExternalId());
		if (collection == null) {
			collection = extractVector(expertEditionInter);
			StoredVectors.getInstance().put(this, expertEditionInter.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}

	@Override
	public final Collection<Double> visit(Fragment fragment) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, fragment.getExternalId());
		if (collection == null) {
			collection = extractVector(fragment);
			StoredVectors.getInstance().put(this, fragment.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}

	@Override
	public final Collection<Double> visit(Source source) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, source.getExternalId());
		if (collection == null) {
			collection = extractVector(source);
			StoredVectors.getInstance().put(this, source.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}

	@Override
	public final Collection<Double> visit(SourceInter sourceInter) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, sourceInter.getExternalId());
		if (collection == null) {
			collection = extractVector(sourceInter);
			StoredVectors.getInstance().put(this, sourceInter.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}
}