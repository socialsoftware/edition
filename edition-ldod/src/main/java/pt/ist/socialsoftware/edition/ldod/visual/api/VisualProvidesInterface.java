package pt.ist.socialsoftware.edition.ldod.visual.api;

import pt.ist.socialsoftware.edition.ldod.dto.InterIdDistancePairDto;
import pt.ist.socialsoftware.edition.ldod.dto.WeightsDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionFragmentsDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.EditionInterListDto;
import pt.ist.socialsoftware.edition.ldod.visual.api.dto.FragmentDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class VisualProvidesInterface {
    private final VisualRequiresInterface visualRequiresInterface = new VisualRequiresInterface();

    public List<EditionInterListDto> getExpertEditionsAndPublicVirtualEditions() {
        List<EditionInterListDto> result = new ArrayList<>(this.visualRequiresInterface.getEditionInterListDto());
        result.addAll(this.visualRequiresInterface.getPublicVirtualEditionInterListDto());
        return result;
    }

    public EditionFragmentsDto getEditionFragmentsForAcronym(String acronym) {
        EditionFragmentsDto editionFragments = new EditionFragmentsDto();
        ExpertEditionDto expertEdition = this.visualRequiresInterface.getExpertEditionDto(acronym);
        if (expertEdition != null) {
            editionFragments.setCategories(new ArrayList<>());

            List<FragmentDto> fragments = new ArrayList<>();

            this.visualRequiresInterface.getExpertEditionScholarInterDtoList(acronym).stream().sorted(Comparator.comparing(ScholarInterDto::getTitle))
                    .forEach(inter -> {
                        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getXmlId());
                        writer.write(false);
                        FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

                        fragments.add(fragment);
                    });

            editionFragments.setFragments(fragments);

            return editionFragments;

        } else {
            VirtualEditionDto virtualEdition = this.visualRequiresInterface.getVirtualEdition(acronym);
            if (virtualEdition != null) {
                editionFragments.setCategories(virtualEdition.getSortedCategorySet());

                List<FragmentDto> fragments = new ArrayList<>();

                virtualEdition.getVirtualEditionInterDtoSet().stream().sorted(Comparator.comparing(VirtualEditionInterDto::getTitle))
                        .forEach(inter -> {
                            PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed().getXmlId());
                            writer.write(false);
                            FragmentDto fragment = new FragmentDto(inter, writer.getTranscription());

                            fragments.add(fragment);
                        });

                editionFragments.setFragments(fragments);

                return editionFragments;

            }
            return null;
        }
    }

    public List<Map.Entry<String, Double>> getInterTFIDFTerms(String interId) {
        ScholarInterDto scholarInterDto = this.visualRequiresInterface.getScholarInterByExternalIdOfInter(interId);

        return this.visualRequiresInterface.getScholarInterTermFrequency(scholarInterDto);
    }

    public List<InterIdDistancePairDto> getIntersByDistance(String externalId, WeightsDto weights) {
        return this.visualRequiresInterface.getIntersByDistance(externalId, weights);
    }

}
