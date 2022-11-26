package pt.ist.socialsoftware.edition.ldod.bff.text.service;

import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragInterRequestBodyDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.SurfaceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.*;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.sources.ManuscriptSourceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.sources.SourceDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TextService {


    public List<FragmentDto> getFragments() {
        return LdoD.getInstance().getFragmentsSet().stream().map(fragment -> new FragmentDto(fragment, getExpertsMap(fragment), getFragmentSortedInterSources(fragment))).collect(Collectors.toList());
    }


    public List<SourceDto> getSources() {
        return LdoD.getInstance().getFragmentsSet().stream().flatMap(fragment -> fragment.getSourcesSet().stream()).sorted().map(source -> isManuscript(source) ? new ManuscriptSourceDto((ManuscriptSource) source, getSourceInters(source)) : new SourceDto(source, getSourceInters(source))).collect(Collectors.toList());
    }

    public List<EditorialInterDto> getEditionByAcrn(String acronym) {
        ExpertEdition expertEdition = LdoD.getInstance().getExpertEdition(acronym);
        return expertEdition != null
                ? LdoD.getInstance().getExpertEdition(acronym).getSortedInterps()
                .stream()
                .map(fragInter -> new EditorialInterDto((ExpertEditionInter) fragInter))
                .collect(Collectors.toList())
                : new ArrayList<>();
    }


    public FragmentDto getFragmentByXmlId(String xmlId) {
        Fragment fragment = getFragment(xmlId);
        return new FragmentDto(fragment, getExpertsMapWithPrevNex(fragment), getFragmentSortedInterSources(fragment));
    }

    public FragmentDto getFragmentInter(String xmlId, String urlId, FragInterRequestBodyDto bodyDto) {
        Fragment fragment = getFragment(xmlId);
        FragInter fragInter = getFragInter(urlId, fragment);
        return getFragmentDto4OneInter(bodyDto, fragment, fragInter);

    }

    public FragmentDto getFragmentInters(String xmlId, FragInterRequestBodyDto bodyDto, String interExternalId) {
        Fragment fragment = getFragment(xmlId);
        FragInter fragInter = FenixFramework.getDomainObject(interExternalId);
        return getFragmentDto4OneInter(bodyDto, fragment, fragInter);


    }

    private FragmentDto getFragmentDto4OneInter(FragInterRequestBodyDto bodyDto, Fragment fragment, FragInter fragInter) {
        PlainHtmlWriter4OneInter writer = getPlainHtmlWriter4OneInter(fragInter, bodyDto);
        return FragmentDto.FragmentDtoBuilder.aFragmentDto(fragment, getExpertsMapWithPrevNex(fragment), getFragmentSortedInterSources(fragment)).inters(Collections.singletonList(getSpecificFragInterClass(fragInter))).transcription(Collections.singletonList(writer.getTranscription())).build();
    }

    public FragmentDto getFragmentInters(String xmlId, FragInterRequestBodyDto bodyDto, List<String> selectedInters) {
        Fragment fragment = getFragment(xmlId);

        List<FragInter> fragInters = selectedInters.stream().map(s -> (FragInter) FenixFramework.getDomainObject(s)).collect(Collectors.toList());
        HtmlWriter2CompInters writer = getPlainHtmlWriter2CompInters(fragInters, bodyDto);

        return FragmentDto.FragmentDtoBuilder.aFragmentDto(fragment, getExpertsMapWithPrevNex(fragment), getFragmentSortedInterSources(fragment)).inters(fragInters.stream().map(this::getSpecificFragInterClass).collect(Collectors.toList())).transcription(writer.getLineByLine() ? Collections.singletonList(writer.getTranscriptionLineByLine()) : fragInters.stream().map(writer::getTranscription).collect(Collectors.toList())).variations(getVariations(fragInters, fragment)).build();

    }

    private List<List<String>> getVariations(List<FragInter> inters, Fragment fragment) {
        Map<FragInter, HtmlWriter4Variations> variations = inters.stream().collect(Collectors.toMap(i -> i, HtmlWriter4Variations::new));
        List<AppText> apps = new ArrayList<>();
        fragment.getTextPortion().putAppTextWithVariations(apps, inters);
        Collections.reverse(apps);
        return apps.stream().map(appText -> inters.stream().map(inter -> variations.get(inter).getAppTranscription(appText)).collect(Collectors.toList())).collect(Collectors.toList());
    }

    private PlainHtmlWriter4OneInter getPlainHtmlWriter4OneInter(FragInter fragInter, FragInterRequestBodyDto body) {
        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(fragInter.getLastUsed());
        if (fragInter.getSourceType().equals(Edition.EditionType.EDITORIAL)) writeSingleEditorialInter(writer, body);
        else writeSingleSourceInter(writer, body);
        return writer;
    }

    private HtmlWriter2CompInters getPlainHtmlWriter2CompInters(List<FragInter> inters, FragInterRequestBodyDto body) {
        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);
        writer.write(inters.size() > 2 || body.isLine(), body.isAlign());
        return writer;
    }

    private void writeSingleEditorialInter(PlainHtmlWriter4OneInter writer, FragInterRequestBodyDto fragInterRequestBodyDto) {
        writer.write(fragInterRequestBodyDto.isDiff());
    }

    private void writeSingleSourceInter(PlainHtmlWriter4OneInter writer, FragInterRequestBodyDto body) {
        writer.write(body.isDiff(), body.isDel(), body.isIns(), body.isSub(), body.isNote(), body.isFac(), FenixFramework.getDomainObject(body.getPbText()));
    }

    private boolean isManuscript(Source source) {
        return source.getType().equals(Source.SourceType.MANUSCRIPT);
    }

    private Fragment getFragment(String xmlId) {
        return checkFragmentExists(xmlId, LdoD.getInstance().getFragmentByXmlId(xmlId));
    }

    private FragInter getFragInter(String urlId, Fragment fragment) {
        return checkFragInterExists(urlId, fragment.getFragInterByUrlId(urlId));
    }

    private List<SourceInterDto> getSourceInters(Source source) {
        return source.getSourceIntersSet().stream().map(sourceInter -> isManuscript(sourceInter.getSource()) ? new ManuscriptSourceInterDto(sourceInter) : new PrintedSourceInterDto(sourceInter)).collect(Collectors.toList());
    }

    private List<SourceInterDto> getFragmentSortedInterSources(Fragment fragment) {
        return fragment.getSortedSourceInter().stream().map(sourceInter -> isManuscript(sourceInter.getSource()) ? new ManuscriptSourceInterDto(sourceInter, getSurfaceDtoList(sourceInter)) : new PrintedSourceInterDto(sourceInter, getSurfaceDtoList(sourceInter))).collect(Collectors.toList());
    }


    private Map<String, List<EditorialInterDto>> getExpertsMap(Fragment fragment) {
        return fragment.getExpertEditionInterSet().stream().collect(Collectors.groupingBy(inter -> inter.getEdition().getAcronym(), Collectors.mapping(EditorialInterDto::new, Collectors.toList())));
    }

    private Map<String, List<EditorialInterDto>> getExpertsMapWithPrevNex(Fragment fragment) {
        return fragment.getExpertEditionInterSet().stream().collect(Collectors.groupingBy(inter -> inter.getEdition().getAcronym(), Collectors.mapping(this::getEditorialInterDto, Collectors.toList())));
    }


    private EditorialInterDto getEditorialInterDto(ExpertEditionInter expertEditionInter) {
        ExpertEditionInter prev = (ExpertEditionInter) expertEditionInter.getEdition().getPrevNumberInter(expertEditionInter, expertEditionInter.getNumber());
        ExpertEditionInter next = (ExpertEditionInter) expertEditionInter.getEdition().getNextNumberInter(expertEditionInter, expertEditionInter.getNumber());
        return EditorialInterDto.EditorialInterDtoBuilder.anEditorialInterDto(expertEditionInter).prevXmlId(prev.getFragment().getXmlId()).prevUrlId(prev.getUrlId()).nextXmlId(next.getFragment().getXmlId()).nextUrlId(next.getUrlId()).build();
    }

    private List<SurfaceDto> getSurfaceDtoList(SourceInter inter) {
        Facsimile fac = inter.getSource().getFacsimile();
        return fac != null ? fac.getSurfaces().stream().map(SurfaceDto::new).collect(Collectors.toList()) : null;
    }

    private SourceInterDto getSpecificSourceInterClass(SourceInter inter, List<SurfaceDto> surfaceDtoList) {
        return isManuscript(inter.getSource()) ? new ManuscriptSourceInterDto(inter, surfaceDtoList) : new PrintedSourceInterDto(inter, surfaceDtoList);
    }


    private FragInterDto getSpecificFragInterClass(FragInter inter) {
        return inter instanceof SourceInter ? getSpecificSourceInterClass(((SourceInter) inter), getSurfaceDtoList((SourceInter) inter)) : new EditorialInterDto((ExpertEditionInter) inter);
    }


    private Fragment checkFragmentExists(String xmlId, Fragment fragment) {
        if (fragment == null) throw new LdoDException(String.format("Fragment %s does not exist", xmlId));
        return fragment;
    }

    private FragInter checkFragInterExists(String urlId, FragInter inter) {
        if (inter == null) throw new LdoDException(String.format("Inter %s does not exist", urlId));
        return inter;
    }
}
