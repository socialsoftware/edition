package pt.ist.socialsoftware.edition.ldod.frontend.search;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Medium;
import pt.ist.socialsoftware.edition.search.api.dto.AuthoralForSearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.DatesForSearchDto;
import pt.ist.socialsoftware.edition.search.api.dto.ExpertEditionForSearchDto;
import pt.ist.socialsoftware.edition.text.api.dto.*;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchRemoteController {
    private static final Logger logger = LoggerFactory.getLogger(SearchRemoteController.class);

    private static final String MANUSCRIPTID = "manus";
    private static final String TYPESCRIPT = "datil";


    FeSearchRequiresInterface feSearchRequiresInterface = new FeSearchRequiresInterface();

    @RequestMapping(value = "/getEditions")
    @ResponseBody
    public Map<String, String> getEditions() {
        Map<String, String> editions = new LinkedHashMap<>();

        for (ExpertEditionDto expertEdition : this.feSearchRequiresInterface.getSortedExpertEditionsDto()) {

            editions.put(expertEdition.getAcronym(), expertEdition.getEditor());
        }
        return editions;
    }

    @RequestMapping(value = "/getVirtualEditions")
    @ResponseBody
    public Map<String, String> getVirtualEditions(Model model) {
        return this.feSearchRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(this.feSearchRequiresInterface.getAuthenticatedUser()).stream()
                .collect(Collectors.toMap(VirtualEditionDto::getAcronym, VirtualEditionDto::getTitle));
    }

    @RequestMapping(value = "/getEdition")
    @ResponseBody
    public ExpertEditionForSearchDto getEdition(@RequestParam(value = "edition", required = true) String acronym) {
        logger.debug("getEdition");
        List<ScholarInterDto> scholarInterDtos = this.feSearchRequiresInterface.getExpertEditionScholarInterDtoList(acronym);

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
        for (FragmentDto fragment : this.feSearchRequiresInterface.getFragmentDtoSet()) {
            for (SourceDto source : fragment.getSourcesSet()) {
                if (source.getType().getDesc().equals("printed")) {
                    if (source.getLdoDDate() != null) {
                        beginDate = getIsBeforeDate(beginDate, source.getLdoDDate().getDate());
                        endDate = getIsAfterDate(endDate, source.getLdoDDate().getDate());
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

    @RequestMapping(value = "/getManuscriptsDates")
    @ResponseBody
    public AuthoralForSearchDto getManuscript() {
        logger.debug("getManuscript");
        return getAuthoralDates(MANUSCRIPTID);
    }

    @RequestMapping(value = "/getDactiloscriptsDates")
    @ResponseBody
    public AuthoralForSearchDto getDatiloscript() {
        return getAuthoralDates(TYPESCRIPT);
    }

    private AuthoralForSearchDto getAuthoralDates(String mode) {
        AuthoralForSearchDto authoralForSearchDto = new AuthoralForSearchDto();
        LocalDate beginDate = null;
        LocalDate endDate = null;
        Medium[] values = Medium.values();
        String[] array = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].getDesc();
        }
        for (FragmentDto frag : this.feSearchRequiresInterface.getFragmentDtoSet()) {
            for (ScholarInterDto scholarInter : frag.getScholarInterDtoSet()) {
                if (scholarInter.isSourceInter()) {
                    String type = scholarInter.getSourceDto().getType().getDesc();
                    if (type.equals("manuscript")) {
                        SourceDto source = scholarInter.getSourceDto();
                        if (mode.equals(MANUSCRIPTID)
                                && !source.hasHandNoteSet()
                                || (mode.equals(TYPESCRIPT)
                                && !source.hasTypeNoteSet())) {
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
        authoralForSearchDto.setMediums(array);
        DatesForSearchDto dates = new DatesForSearchDto();
        if (endDate != null && beginDate != null) {
            dates.setBeginDate(beginDate.getYear());
            dates.setEndDate(endDate.getYear());
        }
        authoralForSearchDto.setDates(dates);
        return authoralForSearchDto;
    }

    @RequestMapping(value = "/getHeteronyms")
    @ResponseBody
    public Map<String, String> getHeteronyms() {
        Map<String, String> heteronyms = new HashMap<>();
        for (HeteronymDto heteronym : this.feSearchRequiresInterface.getHeteronymDtoSet()) {
            heteronyms.put(heteronym.getName(), heteronym.getXmlId());
        }
        return heteronyms;
    }

    @RequestMapping(value = "/getDates")
    @ResponseBody
    public DatesForSearchDto getDates() {
        LocalDate beginDate = null;
        LocalDate endDate = null;
        for (FragmentDto fragment : this.feSearchRequiresInterface.getFragmentDtoSet()) {
            for (ScholarInterDto scholarInter : fragment.getScholarInterDtoSet()) {
                if (scholarInter.getLdoDDate() != null) {
                    beginDate = getIsBeforeDate(beginDate, scholarInter.getLdoDDate().getDate());
                    endDate = getIsAfterDate(endDate, scholarInter.getLdoDDate().getDate());
                }
            }
            for (SourceDto source : fragment.getSourcesSet()) {
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