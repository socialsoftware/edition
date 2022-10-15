package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.export.WriteVirtualEditionsToFile;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionFragmentsTEIImport;
import pt.ist.socialsoftware.edition.ldod.loaders.VirtualEditionsTEICorpusImport;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDLoadException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VirtualAdminService {
    public List<VirtualEditionDto> getVirtualEditionsSorted() {
        return LdoD.getInstance().getVirtualEditionsSet()
                .stream()
                .sorted(Comparator.comparing(Edition_Base::getAcronym))
                .map(ve -> VirtualEditionDto
                        .VirtualEditionDtoBuilder
                        .aVirtualEditionDto(ve)
                        .activeMembers(ve.getParticipantSet().stream().map(LdoDUser::getUsername).collect(Collectors.toList()))
                        .annotations(ve.getAnnotationTextList().stream().map(StringEscapeUtils::unescapeHtml).collect(Collectors.toList()))
                        .categories(ve.getAllDepthSortedCategories().stream().map(Category_Base::getName).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteVirtualEdition(String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null)
            throw new LdoDException(String.format(Message.VIRTUAL_EDITION_INVALID.getLabel(), externalId));
        virtualEdition.remove();
    }

    public Map<String, byte[]> exportVirtualEditions() {
        try {
            return Collections.singletonMap("xmlData", new WriteVirtualEditionsToFile().exportOutputStream().toByteArray());
        } catch (IOException e) {
            throw new LdoDException(e.getMessage());
        }
    }

    public MainResponseDto uploadVirtualCorpus(MultipartFile file) {
        VirtualEditionsTEICorpusImport loader = new VirtualEditionsTEICorpusImport();
        try {
            loader.importVirtualEditionsCorpus(file.getInputStream());
        } catch (IOException e) {
            throw new LdoDException(String.format(Message.INVALID_FILE.getLabel(), file.getName()));
        }
        return new MainResponseDto(true, String.format(Message.VIRTUAL_CORPUS_LOADED.getLabel(), file.getOriginalFilename()));
    }


    public MainResponseDto uploadVirtualFragments(MultipartFile[] files) {
        VirtualEditionFragmentsTEIImport loader = new VirtualEditionFragmentsTEIImport();
        int total = 0;
        for (MultipartFile file : files) {
            try {
                loader.importFragmentFromTEI(file.getInputStream());
                total++;
            } catch (LdoDLoadException ex) {
                //TODO give information whom virtual fragments weren't uploaded and why
            } catch (IOException e) {
                return new MainResponseDto(false, String.format(Message.INVALID_FILE.getLabel(), file.getName()));
            } catch (LdoDException ex) {
                return new MainResponseDto(false, ex.getMessage());
            }
        }
        return new MainResponseDto(true, String.format(Message.VIRTUAL_EDITION_FRAGS_IMPORTED.getLabel(), total));
    }

    public void deleteAllVirtualEditions() {
        LdoD.getInstance().getVirtualEditionsSet().forEach(VirtualEdition::remove);
    }
}
