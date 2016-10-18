package pt.ist.socialsoftware.edition.recommendation;

import java.util.HashMap;
import java.util.Map;

import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class StoredVectors {
	private static StoredVectors instance;

	public static StoredVectors getInstance() {
		if (instance == null) {
			instance = new StoredVectors();
		}
		return instance;
	}

	private final Map<Class<? extends Property>, Map<String, double[]>> weights;

	private StoredVectors() {
		weights = new HashMap<Class<? extends Property>, Map<String, double[]>>();
	}

	public boolean contains(Property property, String id) {
		if (weights.containsKey(property.getClass())) {
			if (weights.get(property.getClass()).containsKey(id)) {
				return true;
			}
		}
		return false;
	}

	public double[] get(Property property, String id) {
		if (weights.containsKey(property.getClass())) {
			if (weights.get(property.getClass()).containsKey(id)) {
				return weights.get(property.getClass()).get(id);
			}
		}
		return null;
	}

	public void put(Property property, String id, double[] weightCollection) {
		if (!weights.containsKey(property.getClass())) {
			weights.put(property.getClass(), new HashMap<String, double[]>());
		}
		weights.get(property.getClass()).put(id, weightCollection);
	}

}
