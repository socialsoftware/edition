package pt.ist.socialsoftware.edition.ldod.bff.text.service;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.ldod.bff.dtos.MainResponseDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.UploadFragmentDto;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.export.ExpertEditionTEIExport;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEICorpus;
import pt.ist.socialsoftware.edition.ldod.loaders.LoadTEIFragments;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TextAdminService {


    private boolean isFileConsistent(MultipartFile file) {
        return file == null || file.isEmpty();
    }

    private boolean isFileConsistent(MultipartFile[] file) {
        return file != null && Arrays.stream(file).findAny().isPresent();
    }

    private MainResponseDto getResponseDto(boolean status, String msg) {
        return new MainResponseDto(status, msg);
    }

    public MainResponseDto loadTEICorpusService(MultipartFile file) {
        if (isFileConsistent(file))
            return getResponseDto(false, String.format(Message.INVALID_FILE.getLabel(), file.getName()));
        try {
            new LoadTEICorpus().loadTEICorpus(file.getInputStream());
            return getResponseDto(true, String.format(Message.TEI_CORPUS_LOADED.getLabel(), file.getName()));
        } catch (IOException e) {
            return getResponseDto(false, String.format(Message.INVALID_FILE.getLabel(), file.getName()));
        } catch (LdoDException ldodE) {
            return getResponseDto(false, ldodE.getMessage());
        }

    }

    private void loadTEIFragment(MultipartFile file, List<UploadFragmentDto> uploadFragmentDtoList) throws IOException {
        new LoadTEIFragments().loadFragmentsStepByStep(file.getInputStream(), uploadFragmentDtoList);
    }

    public List<UploadFragmentDto> uploadTEIFragments(MultipartFile[] files) {
        if (!isFileConsistent(files))
            throw new LdoDException("Invalid files");

        List<UploadFragmentDto> uploadFragmentDtoList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                loadTEIFragment(file, uploadFragmentDtoList);
            } catch (IOException e) {
                throw new LdoDException(e.getMessage());
            }
        }
        return uploadFragmentDtoList;
    }

    public List<UploadFragmentDto> uploadOrOverwriteTEIFragment(MultipartFile file) {
        if (isFileConsistent(file))
            throw new LdoDException("Invalid file");
        try {
            return new LoadTEIFragments().loadFragmentsAtOnce(file.getInputStream());

        } catch (IOException e) {
            throw new LdoDException(e.getMessage());
        }
    }

    public List<UploadFragmentDto> getFragmentsSet() {
        return LdoD.getInstance().getFragmentsSet()
                .stream()
                .map(fragment -> new UploadFragmentDto(fragment, false, false))
                .collect(Collectors.toList());
    }

    public MainResponseDto removeFragment(String externalId) {
        Optional<Fragment> fragment = LdoD.getInstance().getFragmentsSet().stream().filter(frag -> frag.getExternalId().equals(externalId)).findFirst();
        if (fragment.isPresent()) {
            fragment.get().remove();
            return getResponseDto(true, String.format("Fragment with id %s removed", externalId));
        }
        return getResponseDto(false, String.format("No fragment with id %s", externalId));
    }

    public Map<String, byte[]> exportFragments(List<String> externalIds) {

        Map<Fragment, Set<FragInter>> FragsIntersToExport = LdoD.getInstance().getFragmentsSet()
                .stream()
                .filter(fragment -> externalIds.contains(fragment.getExternalId()))
                .collect(Collectors.toMap(Function.identity(), fragment -> fragment
                        .getFragmentInterSet()
                        .stream()
                        .filter(inter -> inter.getSourceType() != Edition.EditionType.VIRTUAL)
                        .collect(Collectors.toSet())));


        ExpertEditionTEIExport teiGenerator = new ExpertEditionTEIExport();
        teiGenerator.generate(FragsIntersToExport);
        try {
            return Collections.singletonMap("xmlData", IOUtils.toByteArray(IOUtils.toInputStream(teiGenerator.getXMLResult(), "UTF-8")));
        } catch (IOException ex) {
            throw new LdoDException("IOError writing file to output stream");
        }
    }

    public Map<String, byte[]> exportAllFragments() {
        return exportFragments(LdoD.getInstance().getFragmentsSet().stream().map(AbstractDomainObject::getExternalId).collect(Collectors.toList()));
    }

    public Map<String, byte[]> exportRandomFrags() {
        int size = LdoD.getInstance().getFragmentsSet().size();
        if (size >= 3) return exportFragments(
                IntStream.of(new Random().ints(3, 0, size).toArray())
                        .mapToObj(index -> new ArrayList<>(LdoD.getInstance().getFragmentsSet()).get(index))
                        .map(AbstractDomainObject::getExternalId)
                        .collect(Collectors.toList()));
        throw new LdoDException("Not enough Fragments");
    }

    public void removeFragments() {
        LdoD.getInstance().getFragmentsSet().forEach(Fragment::remove);
    }
}
