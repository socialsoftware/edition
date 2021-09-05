package pt.ist.socialsoftware.edition.search.feature.options;

import pt.ist.socialsoftware.edition.notification.dtos.virtual.VirtualEditionDto;
import pt.ist.socialsoftware.edition.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.search.api.dto.VirtualEditionSearchOptionDto;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class VirtualEditionSearchOption extends SearchOption {
    private final String virtualEditionAcronym;
    private final boolean inclusion;
    private final String username;

    public VirtualEditionSearchOption(VirtualEditionSearchOptionDto virtualEditionSearchOptionDto) {
        this.inclusion = virtualEditionSearchOptionDto.isInclusion();
        this.virtualEditionAcronym = virtualEditionSearchOptionDto.getVirtualEditionAcronym();
        this.username = virtualEditionSearchOptionDto.getUsername();
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.VIRTUAL_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        Set<String> virtualEditionsAcronyms = searchRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(this.username).stream()
                .map(VirtualEditionDto::getAcronym).collect(Collectors.toSet());


        if (this.inclusion) {
            if (this.virtualEditionAcronym.equals(ALL) && virtualEditionsAcronyms.stream().anyMatch(virtualEditionAcronym -> searchRequiresInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEditionAcronym))) {
                return true;
            }
            if (searchRequiresInterface.getVirtualEditionAcronymByVirtualEditionInterXmlId(inter.getXmlId()).equals(this.virtualEditionAcronym)
                    && virtualEditionsAcronyms.stream().anyMatch(virtualEdition -> searchRequiresInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEdition))) {
                return true;
            }
        } else {
            if (this.virtualEditionAcronym.equals(ALL)) {
                return false;
            }
            if (!searchRequiresInterface.getVirtualEditionAcronymByVirtualEditionInterXmlId(inter.getXmlId()).equals(this.virtualEditionAcronym)
                    && virtualEditionsAcronyms.stream().anyMatch(virtualEdition -> searchRequiresInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEdition))) {
                return true;
            }
        }
        return false;
    }

}
