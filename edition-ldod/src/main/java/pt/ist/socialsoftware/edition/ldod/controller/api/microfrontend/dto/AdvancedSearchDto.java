package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.search.options.SearchOption;

public class AdvancedSearchDto {
	

	private boolean showEdition;
	private boolean showHeteronym;
	private boolean showDate;
	private boolean showLdoD;
	private boolean showSource;
	private boolean showSourceType;
	private boolean showTaxonomy;
	private int fragCount;
	private int interCount;
	private int fragCountNotAdded;
	private int interCountNotAdded;
	private int searchLenght;
	private SearchOption[] search;
	private List<FragInterDto> listResult;

	public AdvancedSearchDto(boolean showEdition, boolean showHeteronym, boolean showDate, boolean showLdoD,
			boolean showSource, boolean showSourceType, boolean showTaxonomy, int fragCount, int interCount,
			int fragCountNotAdded, int interCountNotAdded, Map<Fragment, Map<FragInter, List<SearchOption>>> results,
			SearchOption[] searchOptions, int length) {
		
			this.setShowEdition(showEdition);
		  this.setShowHeteronym(showHeteronym);
		  this.setShowDate(showDate);
		  this.setShowLdoD(showLdoD);
		  this.setShowSource(showSource);
		  this.setShowSourceType(showSourceType);
		  this.setShowTaxonomy(showTaxonomy);
		  this.setFragCount(fragCount);
		  this.setInterCount(interCount);
		  this.setFragCountNotAdded(fragCountNotAdded);
		  this.setInterCountNotAdded(interCountNotAdded);		  
		  List<FragInterDto> listFragmentAux = new ArrayList<FragInterDto>();
		  for (Map.Entry<Fragment, Map<FragInter, List<SearchOption>>> entry : results.entrySet()) {			  
				for(Map.Entry<FragInter, List<SearchOption>> innerEntry : entry.getValue().entrySet()) {
					listFragmentAux.add(new FragInterDto(entry.getKey(), innerEntry.getKey(), innerEntry.getValue()));
				}
			}
		  this.setListResult(listFragmentAux);
		  this.setSearch(searchOptions);
		  this.setSearchLenght(searchOptions.length);
	}

	public boolean isShowEdition() {
		return showEdition;
	}

	public void setShowEdition(boolean showEdition) {
		this.showEdition = showEdition;
	}

	public boolean isShowHeteronym() {
		return showHeteronym;
	}

	public void setShowHeteronym(boolean showHeteronym) {
		this.showHeteronym = showHeteronym;
	}

	public boolean isShowDate() {
		return showDate;
	}

	public void setShowDate(boolean showDate) {
		this.showDate = showDate;
	}

	public boolean isShowLdoD() {
		return showLdoD;
	}

	public void setShowLdoD(boolean showLdoD) {
		this.showLdoD = showLdoD;
	}

	public boolean isShowSource() {
		return showSource;
	}

	public void setShowSource(boolean showSource) {
		this.showSource = showSource;
	}

	public boolean isShowSourceType() {
		return showSourceType;
	}

	public void setShowSourceType(boolean showSourceType) {
		this.showSourceType = showSourceType;
	}

	public boolean isShowTaxonomy() {
		return showTaxonomy;
	}

	public void setShowTaxonomy(boolean showTaxonomy) {
		this.showTaxonomy = showTaxonomy;
	}

	public int getFragCount() {
		return fragCount;
	}

	public void setFragCount(int fragCount) {
		this.fragCount = fragCount;
	}

	public int getInterCount() {
		return interCount;
	}

	public void setInterCount(int interCount) {
		this.interCount = interCount;
	}

	public int getFragCountNotAdded() {
		return fragCountNotAdded;
	}

	public void setFragCountNotAdded(int fragCountNotAdded) {
		this.fragCountNotAdded = fragCountNotAdded;
	}

	public int getInterCountNotAdded() {
		return interCountNotAdded;
	}

	public void setInterCountNotAdded(int interCountNotAdded) {
		this.interCountNotAdded = interCountNotAdded;
	}

	public int getSearchLenght() {
		return searchLenght;
	}

	public void setSearchLenght(int searchLenght) {
		this.searchLenght = searchLenght;
	}

	public SearchOption[] getSearch() {
		return search;
	}

	public void setSearch(SearchOption[] search) {
		this.search = search;
	}

	public List<FragInterDto> getListResult() {
		return listResult;
	}

	public void setListResult(List<FragInterDto> listResult) {
		this.listResult = listResult;
	}
	
}
