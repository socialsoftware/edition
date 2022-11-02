package pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.fragment;


import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.VirtualEditionInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;

import java.util.List;

public class VirtualFragmentNavDto {

    private List<VirtualEditionInterDto> inters;

    private String veExternalId;

    private String interExternalId;
    private String acronym;
    private boolean member;
    private boolean canAddInter;

    public VirtualFragmentNavDto(VirtualEdition edition, FragInter inter) {
        setVeExternalId(edition.getExternalId());
        setAcronym(edition.getAcronym());
        setMember(edition.getParticipantList().contains(LdoDUser.getAuthenticatedUser()));
        setInterExternalId(inter != null ? inter.getExternalId() : null);
        setCanAddInter(inter != null ? edition.canAddFragInter(inter) : false);
    }

    public List<VirtualEditionInterDto> getInters() {
        return inters;
    }

    public String getVeExternalId() {
        return veExternalId;
    }

    public void setVeExternalId(String veExternalId) {
        this.veExternalId = veExternalId;
    }

    public String getInterExternalId() {
        return interExternalId;
    }

    public void setInterExternalId(String interExternalId) {
        this.interExternalId = interExternalId;
    }

    public boolean isCanAddInter() {
        return canAddInter;
    }

    public void setCanAddInter(boolean canAddInter) {
        this.canAddInter = canAddInter;
    }

    public void setInters(List<VirtualEditionInterDto> inters) {
        this.inters = inters;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }


    public static final class VirtualFragmentNavDtoBuilder {
        private final VirtualFragmentNavDto virtualFragmentNavDto;

        private VirtualFragmentNavDtoBuilder(VirtualEdition ed, FragInter inter) {
            virtualFragmentNavDto = new VirtualFragmentNavDto(ed, inter);
        }

        public static VirtualFragmentNavDtoBuilder aVirtualFragmentNavDto(VirtualEdition ed, FragInter inter) {
            return new VirtualFragmentNavDtoBuilder(ed, inter);
        }

        public VirtualFragmentNavDtoBuilder inters(List<VirtualEditionInterDto> inters) {
            virtualFragmentNavDto.setInters(inters);
            return this;
        }

        public VirtualFragmentNavDtoBuilder acronym(String acronym) {
            virtualFragmentNavDto.setAcronym(acronym);
            return this;
        }

        public VirtualFragmentNavDtoBuilder member(boolean member) {
            virtualFragmentNavDto.setMember(member);
            return this;
        }

        public VirtualFragmentNavDtoBuilder canAddInter(boolean canAddInter) {
            virtualFragmentNavDto.setCanAddInter(canAddInter);
            return this;
        }

        public VirtualFragmentNavDto build() {
            return virtualFragmentNavDto;
        }
    }
}
