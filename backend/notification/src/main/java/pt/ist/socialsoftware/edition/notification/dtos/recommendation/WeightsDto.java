package pt.ist.socialsoftware.edition.notification.dtos.recommendation;


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
