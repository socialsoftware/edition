package pt.ist.socialsoftware.edition.recommendation.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.recommendation.StoredVectors;

public abstract class StorableProperty extends Property {
	private static Logger logger = LoggerFactory.getLogger(StorableProperty.class);

	public StorableProperty(double weight) {
		super(weight);
	}

	private double[] applyWeights(double[] vector, double weight) {
		double[] result = new double[vector.length];
		if (weight == 1.0) {
			return vector;
		} else if (weight == 0.0) {
			return result;
		} else {
			for (int i = 0; i < vector.length; i++) {
				result[i] = vector[i] * weight;
			}
			return result;
		}
	}

	@Override
	public final double[] loadProperty(VirtualEditionInter virtualEditionInter) {
		String externalId = virtualEditionInter.getLastUsed().getExternalId();
		double[] vector = StoredVectors.getInstance().get(this, externalId);
		if (vector == null) {
			vector = extractVector(virtualEditionInter);
			StoredVectors.getInstance().put(this, externalId, vector);
		}
		return applyWeights(vector, getWeight());
	}

	@Override
	public final double[] loadProperty(Fragment fragment) {
		double[] vector = StoredVectors.getInstance().get(this, fragment.getExternalId());
		if (vector == null) {
			vector = extractVector(fragment);
			StoredVectors.getInstance().put(this, fragment.getExternalId(), vector);
		}
		return applyWeights(vector, getWeight());
	}

}