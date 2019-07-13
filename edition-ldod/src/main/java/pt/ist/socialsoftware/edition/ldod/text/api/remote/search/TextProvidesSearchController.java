package pt.ist.socialsoftware.edition.ldod.text.api.remote.search;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.ManuscriptSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.feature.options.TypescriptSearchOption;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.*;
import pt.ist.socialsoftware.edition.ldod.text.api.remote.search.dto.AuthoralForSearchDto;
import pt.ist.socialsoftware.edition.ldod.text.api.remote.search.dto.DatesForSearchDto;
import pt.ist.socialsoftware.edition.ldod.text.api.remote.search.dto.ExpertEditionForSearchDto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/search")
public class TextProvidesSearchController {
    private static final Logger logger = LoggerFactory.getLogger(TextProvidesSearchController.class);

    TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    /*
     * EditionController Sets all the empty boxes to null instead of the empty string ""
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/getEditions")
    @ResponseBody
    public Map<String, String> getEditions() {
        Map<String, String> editions = new LinkedHashMap<>();

        for (ExpertEditionDto expertEdition : this.textProvidesInterface.getSortedExpertEditionsDto()) {

            editions.put(expertEdition.getAcronym(), expertEdition.getEditor());
        }
        return editions;
    }

    @RequestMapping(value = "/getEdition")
    @ResponseBody
    public ExpertEditionForSearchDto getEdition(@RequestParam(value = "edition", required = true) String acronym) {
        logger.debug("getEdition");
        List<ScholarInterDto> scholarInterDtos = this.textProvidesInterface.getExpertEditionScholarInterDtoList(acronym);

        Map<String, String> heteronyms = new HashMap<>();
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (ScholarInterDto scholarInterDto : scholarInterDtos) {
            HeteronymDto heteronymDto = scholarInterDto.getHeteronym();
            if (!heteronyms.containsKey(heteronymDto.getName())) {
                heteronyms.put(heteronymDto.getName(), heteronymDto.getXmlId());
            }

            LdoDDateDto ldoDDateDto = scholarInterDto.getLdoDDate();
            if (ldoDDateDto != null) {
                beginDate = getIsBeforeDate(beginDate, ldoDDateDto.getDate());
                endDate = getIsAfterDate(endDate, ldoDDateDto.getDate());
            }
        }
        ExpertEditionForSearchDto expertEditionForSearchDto = new ExpertEditionForSearchDto(acronym);
        if (heteronyms.size() > 0) {
            expertEditionForSearchDto.setHeteronyms(heteronyms);
        }
        if (endDate != null && beginDate != null) {
            expertEditionForSearchDto.setBeginDate(beginDate.getYear());
            expertEditionForSearchDto.setEndDate(endDate.getYear());
        }
        return expertEditionForSearchDto;
    }

    @RequestMapping(value = "/getPublicationsDates")
    @ResponseBody
    public DatesForSearchDto getPublicationsDates() {
        logger.debug("getPublicationsDates");
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (FragmentDto fragment : this.textProvidesInterface.getFragmentDtoSet()) {
            for (SourceDto source : fragment.getSourcesSet()) {
                if (source.getType().equals(Source.SourceType.PRINTED)) {
                    if (source.getLdoDDate() != null) {
                        beginDate = getIsBeforeDate(beginDate, source.getLdoDDate());
                        endDate = getIsAfterDate(endDate, source.getLdoDDate());
                    }
                }
            }
        }
        DatesForSearchDto datesForSearchDto = new DatesForSearchDto();
        if (endDate != null && beginDate != null) {
            datesForSearchDto.setBeginDate(beginDate.getYear());
            datesForSearchDto.setEndDate(endDate.getYear());
        }
        return datesForSearchDto;
    }

    private AuthoralForSearchDto getAuthoralDates(String mode) {
        AuthoralForSearchDto json = new AuthoralForSearchDto();
        LocalDate beginDate = null;
        LocalDate endDate = null;
        ManuscriptSource.Medium[] values = ManuscriptSource.Medium.values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getDesc();
        }
        for (Fragment frag : TextModule.getInstance().getFragmentsSet()) {
            for (ScholarInter scholarInter : frag.getScholarInterSet()) {
                if (!scholarInter.isExpertInter()) {
                    Source.SourceType type = ((SourceInter) scholarInter).getSource().getType();
                    if (type.equals(Source.SourceType.MANUSCRIPT)) {
                        ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
                        if (mode.equals(ManuscriptSearchOption.MANUSCRIPTID)
                                && source.getHandNoteSet().isEmpty()
                                || (mode.equals(TypescriptSearchOption.TYPESCRIPT)
                                && source.getTypeNoteSet().isEmpty())) {
                            break;
                        }
                        if (source.getLdoDDate() != null) {
                            beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
                            endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
                        }
                    }
                }
            }
        }
        json.setMediums(array);
        DatesForSearchDto dates = new DatesForSearchDto();
        if (endDate != null && beginDate != null) {
            dates.setBeginDate(beginDate.getYear());
            dates.setEndDate(endDate.getYear());
        }
        json.setDates(dates);
        return json;
    }

    @RequestMapping(value = "/getManuscriptsDates")
    @ResponseBody
    public AuthoralForSearchDto getManuscript() {
        logger.debug("getManuscript");
        return getAuthoralDates(ManuscriptSearchOption.MANUSCRIPTID);
    }

    @RequestMapping(value = "/getDactiloscriptsDates")
    @ResponseBody
    public AuthoralForSearchDto getDatiloscript() {
        return getAuthoralDates(TypescriptSearchOption.TYPESCRIPT);
    }

    @RequestMapping(value = "/getHeteronyms")
    @ResponseBody
    public Map<String, String> getHeteronyms() {
        Map<String, String> heteronyms = new HashMap<>();
        for (Heteronym heteronym : TextModule.getInstance().getHeteronymsSet()) { //TODO: change this so it gets heteronym info from the interface
            heteronyms.put(heteronym.getName(), heteronym.getXmlId());
        }
        return heteronyms;
    }

    @RequestMapping(value = "/getDates")
    @ResponseBody
    public DatesForSearchDto getDates() {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (Fragment fragment : TextModule.getInstance().getFragmentsSet()) {
            for (ScholarInter scholarInter : fragment.getScholarInterSet()) {
                if (scholarInter.getLdoDDate() != null) {
                    beginDate = getIsBeforeDate(beginDate, scholarInter.getLdoDDate().getDate());
                    endDate = getIsAfterDate(endDate, scholarInter.getLdoDDate().getDate());
                }
            }
            for (Source source : fragment.getSourcesSet()) {
                if (source.getLdoDDate() != null) {
                    beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
                    endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
                }
            }
        }
        DatesForSearchDto dates = new DatesForSearchDto();
        if (endDate != null && beginDate != null && endDate != beginDate) {
            dates.setBeginDate(beginDate.getYear());
            dates.setEndDate(endDate.getYear());
        }
        return dates;
    }

    private LocalDate getIsBeforeDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else if (date1.isBefore(date2)) {
            return date1;
        } else {
            return date2;
        }
    }

    private LocalDate getIsAfterDate(LocalDate date1, LocalDate date2) {
        if (date1 == null) {
            return date2;
        } else if (date2 == null) {
            return date1;
        } else if (date1.isAfter(date2)) {
            return date1;
        } else {
            return date2;
        }
    }

}