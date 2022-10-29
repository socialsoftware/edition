package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos;

public class GenerateTopicsDto {
    private int numTopics;
    private int numWords;
    private int thresholdCategories;
    private int numIterations;

    public GenerateTopicsDto() {
    }

    public int getNumTopics() {
        return numTopics;
    }

    public void setNumTopics(int numTopics) {
        this.numTopics = numTopics;
    }

    public int getNumWords() {
        return numWords;
    }

    public void setNumWords(int numWords) {
        this.numWords = numWords;
    }

    public int getThresholdCategories() {
        return thresholdCategories;
    }

    public void setThresholdCategories(int thresholdCategories) {
        this.thresholdCategories = thresholdCategories;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }
}
