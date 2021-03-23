package pt.ist.socialsoftware.edition.recommendation.api.dto;


import pt.ist.socialsoftware.edition.recommendation.feature.properties.*;

import java.util.ArrayList;
import java.util.List;

public class WeightsDto {
    private float heteronymWeight;
    private float dateWeight;
    private float textWeight;
    private float taxonomyWeight;

    public WeightsDto() {
    }

    public WeightsDto(float heteronymWeight, float dateWeight, float textWeight, float taxonomyWeight) {
        this.heteronymWeight = heteronymWeight;
        this.dateWeight = dateWeight;
        this.textWeight = textWeight;
        this.taxonomyWeight = taxonomyWeight;
    }

    public List<Property> getProperties() {
        List<Property> result = new ArrayList<>();
        if (getHeteronymWeight() > 0.0) {
            result.add(new HeteronymProperty(getHeteronymWeight()));
        }
        if (getDateWeight() > 0.0) {
            result.add(new DateProperty(getDateWeight()));
        }
        if (getTextWeight() > 0.0) {
            result.add(new TextProperty(getTextWeight()));
        }

        return result;
    }

    public List<Property> getProperties(String editionAcronym) {
        List<Property> result = getProperties();

        if (getTaxonomyWeight() > 0.0) {
            result.add(new TaxonomyProperty(getTaxonomyWeight(), editionAcronym,
                    Property.PropertyCache.OFF));
        }

        return result;
    }


    public float getHeteronymWeight() {
        return this.heteronymWeight;
    }

    public void setHeteronymWeight(float heteronymWeight) {
        this.heteronymWeight = heteronymWeight;
    }

    public float getDateWeight() {
        return this.dateWeight;
    }

    public void setDateWeight(float dateWeight) {
        this.dateWeight = dateWeight;
    }

    public float getTextWeight() {
        return this.textWeight;
    }

    public void setTextWeight(float textWeight) {
        this.textWeight = textWeight;
    }

    public float getTaxonomyWeight() {
        return this.taxonomyWeight;
    }

    public void setTaxonomyWeight(float taxonomyWeight) {
        this.taxonomyWeight = taxonomyWeight;
    }

}
