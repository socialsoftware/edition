package pt.ist.socialsoftware.edition.ldod.bff.annotations;

import org.springframework.stereotype.Service;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.HumanAnnotation;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;

import java.util.stream.Collectors;

@Service
public class AnnotationService {

    private Object checkObjectNotNull(String externalId) {
        Object obj = FenixFramework.getDomainObject(externalId);
        if (obj == null)
            throw new LdoDException(String.format(Message.ELEMENT_NOT_FOUND.getLabel(), externalId));
        return obj;
    }

    public AnnotationsDto getAnnotations(String externalId) {
        VirtualEditionInter veInter = (VirtualEditionInter) checkObjectNotNull(externalId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        return new AnnotationsDto(
                veInter.getAllDepthAnnotations().stream().map(ann -> new AnnotationDto(ann, LdoDUser.getAuthenticatedUser())).collect(Collectors.toList()),
                veInter.getAllDepthCategories().stream()
                        .sorted((c1, c2) -> c1.compareInEditionContext(veInter.getVirtualEdition(), c2))
                        .map(c -> c.getNameInEditionContext(veInter.getVirtualEdition())).collect(Collectors.toList()),
                veInter.getVirtualEdition().getTaxonomy().getOpenVocabulary(),
                veInter.getVirtualEdition().getTaxonomy().canManipulateAnnotation(user)
        );
    }


    public AnnotationsDto createAnnotation(AnnotationDto annDto, String veExternalId) {
        VirtualEditionInter veInter = (VirtualEditionInter) checkObjectNotNull(veExternalId);
        VirtualEdition ve = veInter.getVirtualEdition();
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        if (!HumanAnnotation.canCreate(ve, user)) throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());
        veInter.createHumanAnnotation(annDto.getQuote(), annDto.getText(), user,
                annDto.getRanges(), annDto.getTagList(), annDto.getContents());
        return getAnnotations(veExternalId);
    }

    public AnnotationsDto updateAnnotation(AnnotationDto annDto, String externalId) {
        HumanAnnotation annotation = (HumanAnnotation) checkObjectNotNull(externalId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        if (!annotation.canUpdate(user)) throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());
        annotation.update(annDto.getText(), annDto.getTagList(), annDto.getContents());
        return getAnnotations(annotation.getVirtualEditionInter().getExternalId());

    }

    public AnnotationsDto deleteAnnotation(String veExternalId, String externalId) {
        HumanAnnotation annotation = (HumanAnnotation) checkObjectNotNull(externalId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        if (!annotation.canDelete(user)) throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());
        annotation.remove();
        return getAnnotations(veExternalId);

    }


}
