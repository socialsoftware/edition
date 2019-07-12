package pt.ist.socialsoftware.edition.ldod.frontend.search;

import org.joda.time.LocalDate;
import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.AdvancedSearchResultDto;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchDto;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.HeteronymDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.LdoDDateDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.SourceDto;

import java.util.List;
import java.util.Map;

public class SearchFrontendRequiresInterface {
    // Requires from Text Module
    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public String getFragmentTitle(String fragmentXmlId) {
        return this.textProvidesInterface.getFragmentTitle(fragmentXmlId);
    }

    public List<ScholarInterDto> getExpertEditionScholarInterDtoList(String acronym) {
        return this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);
    }

    public boolean isExpertInter(String xmlId) {
        return this.textProvidesInterface.isExpertInter(xmlId);
    }

    public boolean hasTypeNoteSet(String xmlId) {
        return this.textProvidesInterface.getSourceOfSourceInter(xmlId).hasTypeNoteSet();
    }

    public boolean hasHandNoteSet(String xmlId) {
        return this.textProvidesInterface.getSourceOfSourceInter(xmlId).hasHandNoteSet();
    }

    public String getSourceInterType(String xmlId) {
        return this.textProvidesInterface.getSourceInterType(xmlId);
    }

    public SourceDto getSourceOfSourceInter(String xmlId) {
        return this.textProvidesInterface.getSourceOfSourceInter(xmlId);
    }

    public String getExpertEditionEditor(String xmlId) {
        return this.textProvidesInterface.getExpertEditionEditor(xmlId);
    }

    public LocalDate getScholarInterDate(String xmlId) {
        LdoDDateDto date = this.textProvidesInterface.getScholarInterDate(xmlId);
        return date != null ? date.getDate() : null;
    }

    public FragInterDto.InterType getTypeOfFragInter(String xmlId) {
        TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
        ScholarInterDto scholarInterDto = textProvidesInterface.getScholarInter(xmlId);
        if (scholarInterDto != null) {
            return scholarInterDto.getType();
        } else {
            return FragInterDto.InterType.VIRTUAL;
        }
    }

    public String getHeteronymName(String xmlId) {
        HeteronymDto heteronym = this.textProvidesInterface.getScholarInterHeteronym(xmlId);
        if (heteronym != null) {
            return heteronym.getName();
        }
        return null;
    }


    // Requires from Search Module
    SearchProvidesInterface searchProvidesInterface = new SearchProvidesInterface();

    public Map<String, List<ScholarInterDto>> getSimpleSearch(String params) {
        return this.searchProvidesInterface.simpleSearch(params);
    }

    public AdvancedSearchResultDto advancedSearch(SearchDto search) {
        return this.searchProvidesInterface.advancedSearch(search);
    }
}
