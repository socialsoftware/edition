package pt.ist.socialsoftware.edition.recommendation.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
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
			return new ArrayList<Double>(collection);
		} else if (weight == 0.0) {
			return new ArrayList<Double>(getDefaultVector());
		} else {
			List<Double> list = new ArrayList<Double>(collection);
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
	public final Collection<Double> loadProperty(VirtualEditionInter virtualEditionInter) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, virtualEditionInter.getExternalId());
		if (collection == null) {
			collection = extractVector(virtualEditionInter);
			StoredVectors.getInstance().put(this, virtualEditionInter.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}

	@Override
	public final Collection<Double> loadProperty(Fragment fragment) {
		Collection<Double> collection = StoredVectors.getInstance().get(this, fragment.getExternalId());
		if (collection == null) {
			collection = extractVector(fragment);
			StoredVectors.getInstance().put(this, fragment.getExternalId(), collection);
		}
		return applyWeights(collection, getWeight());
	}

}