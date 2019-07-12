package pt.ist.socialsoftware.edition.ldod.search.feature.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.search.api.SearchRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.search.api.dto.SearchableElementDto;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionDto;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class VirtualEditionSearchOption extends SearchOption {

    private final String virtualEditionAcronym;
    private final boolean inclusion;

    public VirtualEditionSearchOption(@JsonProperty("inclusion") String inclusion,
                                      @JsonProperty("edition") String virtualEditionAcronym) {
        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }
        this.virtualEditionAcronym = virtualEditionAcronym;
    }

    @Override
    public Stream<SearchableElementDto> search(Stream<SearchableElementDto> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElementDto.Type.VIRTUAL_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElementDto inter) {
        SearchRequiresInterface searchRequiresInterface = new SearchRequiresInterface();

        String username = searchRequiresInterface.getAuthenticatedUser();

        Set<String> virtualEditionsAcronyms = searchRequiresInterface.getPublicVirtualEditionsOrUserIsParticipant(username).stream()
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

    @Override
    public String toString() {
        return null;
    }

}
