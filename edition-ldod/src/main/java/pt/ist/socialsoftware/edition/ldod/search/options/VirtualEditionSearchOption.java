package pt.ist.socialsoftware.edition.ldod.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.ist.socialsoftware.edition.ldod.api.user.UserInterface;
import pt.ist.socialsoftware.edition.ldod.api.virtual.VirtualInterface;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualModule;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class VirtualEditionSearchOption extends SearchOption {

    private final String virtualEdition;
    private final boolean inclusion;

    public VirtualEditionSearchOption(@JsonProperty("inclusion") String inclusion,
                                      @JsonProperty("edition") String virtualEdition) {
        if (inclusion.equals("in")) {
            this.inclusion = true;
        } else {
            this.inclusion = false;
        }
        this.virtualEdition = virtualEdition;
    }

    @Override
    public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
        return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.VIRTUAL_INTER)
                .filter(i -> verifiesSearchOption(i));
    }

    public boolean verifiesSearchOption(SearchableElement inter) {
        Set<String> virtualEditions = VirtualModule.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub())
                .map(VirtualEdition::getAcronym).collect(Collectors.toSet());

        UserInterface userInterface = new UserInterface();
        String user = userInterface.getAuthenticatedUser();
        if (user != null) {
            virtualEditions.addAll(VirtualModule.getInstance().getSelectedVirtualEditionsByUser(user).stream()
                    .map(VirtualEdition::getAcronym).collect(Collectors.toSet()));
        }

        VirtualInterface virtualInterface = new VirtualInterface();
        if (this.inclusion) {
            if (this.virtualEdition.equals(ALL) && virtualEditions.stream().anyMatch(virtualEdition -> virtualInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEdition))) {
                return true;
            }
            if (virtualInterface.getAcronymWithVeiId(inter.getXmlId()).equals(this.virtualEdition)
                    && virtualEditions.stream().anyMatch(virtualEdition -> virtualInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEdition))) {
                return true;
            }
        } else {
            if (this.virtualEdition.equals(ALL)) {
                return false;
            }
            if (!virtualInterface.getAcronymWithVeiId(inter.getXmlId()).equals(this.virtualEdition)
                    && virtualEditions.stream().anyMatch(virtualEdition -> virtualInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEdition))) {
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
