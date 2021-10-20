package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

public class ReadingRecommendationDto {
	
	private List<String> read = new ArrayList<>();
	private double heteronymWeight = 0.0;
	private double dateWeight = 0.0;
	private double textWeight = 1.0;
	private double taxonomyWeight = 0.0;
	private ExpertEditionInterDto prevRecommendation;
	private String currentInterpretation;
	
	public ReadingRecommendationDto(ReadingRecommendation readingRecommendation) {
		this.setHeteronymWeight(readingRecommendation.getHeteronymWeight());
		this.setDateWeight(readingRecommendation.getDateWeight());
		this.setTextWeight(readingRecommendation.getTextWeight());
		this.setTaxonomyWeight(readingRecommendation.getTaxonomyWeight());
		if(readingRecommendation.getPrevRecommendation() != null) {
			this.setPrevRecommendation(new ExpertEditionInterDto(readingRecommendation.getPrevRecommendation()));
		}
		this.setCurrentInterpretation(readingRecommendation.getCurrentInterpretation());
		this.setRead(readingRecommendation.getRead());
	}

	public ReadingRecommendationDto(double d, double e, double f, double g) {
		this.setHeteronymWeight(d);
		this.setDateWeight(e);
		this.setTextWeight(f);
		this.setTaxonomyWeight(g);
	}

	public List<String> getRead() {
		return read;
	}

	public void setRead(List<String> read) {
		this.read = read;
	}

	public double getHeteronymWeight() {
		return heteronymWeight;
	}

	public void setHeteronymWeight(double heteronymWeight) {
		this.heteronymWeight = heteronymWeight;
	}

	public double getDateWeight() {
		return dateWeight;
	}

	public void setDateWeight(double dateWeight) {
		this.dateWeight = dateWeight;
	}

	public double getTextWeight() {
		return textWeight;
	}

	public void setTextWeight(double textWeight) {
		this.textWeight = textWeight;
	}

	public double getTaxonomyWeight() {
		return taxonomyWeight;
	}

	public void setTaxonomyWeight(double taxonomyWeight) {
		this.taxonomyWeight = taxonomyWeight;
	}

	public ExpertEditionInterDto getPrevRecommendation() {
		return prevRecommendation;
	}

	public void setPrevRecommendation(ExpertEditionInterDto prevRecommendation) {
		this.prevRecommendation = prevRecommendation;
	}

	public String getCurrentInterpretation() {
		return currentInterpretation;
	}

	public void setCurrentInterpretation(String currentInterpretation) {
		this.currentInterpretation = currentInterpretation;
	}
}
