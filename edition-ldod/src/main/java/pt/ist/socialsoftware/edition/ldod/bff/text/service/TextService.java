package pt.ist.socialsoftware.edition.ldod.bff.text.service;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.ManuscriptSourceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.PrintedSourceDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextService {



    public List<FragmentDto> getFragments() {

        return LdoD.getInstance().getFragmentsSet().stream()
                .map(FragmentDto::new)
                .collect(Collectors.toList());
    }

    public List<SourceInterDto> getSources() {

        return LdoD.getInstance().getFragmentsSet()
                .stream()
                .flatMap(fragment -> fragment.getSourcesSet().stream()).sorted()
                .map(source -> isManuscript(source)
                        ? new ManuscriptSourceDto((ManuscriptSource) source)
                        : new PrintedSourceDto((PrintedSource) source))
                .collect(Collectors.toList());

    }

    private boolean isManuscript(Source source) {
        return source.getType().equals(Source.SourceType.MANUSCRIPT);
    }

}
