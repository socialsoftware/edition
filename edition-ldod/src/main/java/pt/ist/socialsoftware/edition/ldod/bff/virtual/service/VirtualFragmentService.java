package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.springframework.stereotype.Service;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

import java.util.Set;

@Service
public class VirtualFragmentService {

    private VirtualTaxonomyService taxonomyService;

    public VirtualEditionInterDto dissociate(String fragInterId, String categoryId) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        Category category = taxonomyService.checkCatNotNull(categoryId);
        LdoDUser user = LdoDUser.getAuthenticatedUser();
        inter.dissociate(user, category);
        return new VirtualEditionInterDto(inter);
    }

    public VirtualEditionInterDto associate(String fragInterId, Set<String> categoriesNames) {
        VirtualEditionInter inter = taxonomyService.checkVeInterNotNull(fragInterId);
        if (categoriesNames.isEmpty()) return new VirtualEditionInterDto(inter);
        inter.associate(LdoDUser.getAuthenticatedUser(), categoriesNames);
        return new VirtualEditionInterDto(inter);
    }

}
