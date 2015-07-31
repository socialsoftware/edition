package pt.ist.socialsoftware.edition.recommendation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.ist.socialsoftware.edition.recommendation.properties.StorableProperty;

public class StoredVectors {
	private static StoredVectors instance;

	public static StoredVectors getInstance() {
		if (instance == null){
			instance = new StoredVectors();
		}
		return instance;
	}

	private final Map<Class<? extends StorableProperty>, Map<String, Collection<Double>>> weights;

	private StoredVectors() {
		weights = new HashMap<Class<? extends StorableProperty>, Map<String, Collection<Double>>>();
	}

	public boolean contains(StorableProperty property, String id) {
		if(weights.containsKey(property.getClass())) {
			if(weights.get(property.getClass()).containsKey(id)) {
				return true;
			}
		}
		return false;
	}

	public Collection<Double> get(StorableProperty property, String id) {
		if(weights.containsKey(property.getClass())) {
			if(weights.get(property.getClass()).containsKey(id)) {
				return Collections.unmodifiableCollection(weights.get(property.getClass()).get(id));
			}
		}
		return null;
	}

	public void put(StorableProperty property, String id, Collection<Double> weightCollection) {
		if(!weights.containsKey(property.getClass())) {
			weights.put(property.getClass(), new HashMap<String, Collection<Double>>());
		}
		weights.get(property.getClass()).put(id, weightCollection);
	}

}
