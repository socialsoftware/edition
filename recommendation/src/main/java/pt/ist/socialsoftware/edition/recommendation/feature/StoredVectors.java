package pt.ist.socialsoftware.edition.recommendation.feature;

import pt.ist.socialsoftware.edition.recommendation.feature.properties.Property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StoredVectors {
    private static StoredVectors instance = new StoredVectors();

    // thread safe
    public static StoredVectors getInstance() {
        return instance;
    }

    public static void cleanCache() {
        instance = new StoredVectors();
    }

    private final Map<Class<? extends Property>, Map<String, double[]>> weights;

    private StoredVectors() {
        this.weights = new ConcurrentHashMap<>();
    }

    public boolean contains(Property property, String id) {
        if (this.weights.containsKey(property.getClass())) {
            if (this.weights.get(property.getClass()).containsKey(id)) {
                return true;
            }
        }
        return false;
    }

    public double[] get(Property property, String id) {
        if (this.weights.containsKey(property.getClass())) {
            if (this.weights.get(property.getClass()).containsKey(id)) {
                return this.weights.get(property.getClass()).get(id);
            }
        }
        return null;
    }

    public void put(Property property, String id, double[] weightCollection) {
        if (!this.weights.containsKey(property.getClass())) {
            this.weights.put(property.getClass(), new ConcurrentHashMap<>());
        }
        this.weights.get(property.getClass()).put(id, weightCollection);
    }

}
