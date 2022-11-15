package pt.ist.socialsoftware.edition.ldod.bff.reading;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.reading.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.bff.reading.dto.ReadingExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.bff.reading.dto.RecommendedInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadingService {
    public List<ExpertEditionDto> getExpertEditions() {
        return LdoD.getInstance().getSortedExpertEdition()
                .stream()
                .map(ExpertEditionDto::new)
                .collect(Collectors.toList());
    }


    public ReadingExpertEditionDto getExpertEditionInterByAcronym(String acronym,
                                                                  String xmlId,
                                                                  String urlId,
                                                                  ReadingRecommendation recommendation) {
        Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
        ExpertEditionInter editionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(fragment.getFragInterByUrlId(urlId));
        writer.write(false);

        List<ExpertEditionDto> editions = LdoD.getInstance().getSortedExpertEdition()
                .stream()
                .map(edition -> ExpertEditionDto
                        .ExpertEditionDtoBuilder
                        .anExpertEditionDto(edition)
                        .transcription(edition.getAcronym().equals(acronym) ? writer.getTranscription() : null)
                        .editorialInters(edition.getSortedInter4Frag(fragment)
                                .stream()
                                .map(this::getEditorialInterDto)
                                .collect(Collectors.toList())
                        ).build())
                .collect(Collectors.toList());
        return new ReadingExpertEditionDto(editions, getRecommendedInters(editionInter.getExternalId(), recommendation));
    }

    private List<RecommendedInterDto> getRecommendedInters(String interExternalId, ReadingRecommendation recommendation) {
        return recommendation
                .getNextRecommendations(interExternalId)
                .stream()
                .map(RecommendedInterDto::new)
                .collect(Collectors.toList());
    }

    private EditorialInterDto getEditorialInterDto(ExpertEditionInter inter) {
        FragInter prev = inter.getEdition().getPrevNumberInter(inter, inter.getNumber());
        FragInter next = inter.getEdition().getNextNumberInter(inter, inter.getNumber());
        return EditorialInterDto
                .EditorialInterDtoBuilder
                .anEditorialInterDto(((FragInter) inter))
                .title(inter.getTitle())
                .nextXmlId(next.getFragment().getXmlId())
                .prevXmlId(prev.getFragment().getXmlId())
                .prevUrlId(prev.getUrlId())
                .nextUrlId(next.getUrlId())
                .build();
    }


}

