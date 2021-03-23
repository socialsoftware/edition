package pt.ist.socialsoftware.edition.ldod.frontend.text;

import pt.ist.fenixframework.Atomic;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.text.api.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.text.api.dto.SurfaceDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.virtual.api.dto.VirtualEditionInterDto;


import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class FeTextRequiresInterface {
    // Uses Frontend User Module
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();

    public String getAuthenticatedUser() {
        return this.feUserProvidesInterface.getAuthenticatedUser();
    }


    // Uses Text Module
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

    public FragmentDto getFragmentByXmlId(String xmlId) {
        return this.textProvidesInterface.getFragmentByXmlId(xmlId);
    }

    public FragmentDto getFragmentByExternalId(String externalId) {
        return this.textProvidesInterface.getFragmentByExternalId(externalId);
    }

    public ScholarInterDto getScholarInterByExternalId(String externalId) {
        return this.textProvidesInterface.getScholarInterbyExternalId(externalId);
    }

    public ScholarInterDto getScholarInterByXmlId(String xmlId) {
        return this.textProvidesInterface.getScholarInter(xmlId);
    }

    public List<ExpertEditionDto> getSortedExpertEditionsDto() {
        return this.textProvidesInterface.getSortedExpertEditionsDto();
    }

    public ExpertEditionDto getExpertEditionByAcronym(String acronym) {
        return this.textProvidesInterface.getExpertEditionDto(acronym);
    }

    public Set<FragmentDto> getFragmentDtoSet() {
        return this.textProvidesInterface.getFragmentDtoSet();
    }

    public Set<FragmentDto> getFragmentDtosWithSourceDtos() {
        return this.textProvidesInterface.getFragmentDtosWithSourceDtos();
    }

    public Set<FragmentDto> getFragmentDtosWithScholarInterDtos() {
        return this.textProvidesInterface.getFragmentDtosWithScholarInterDtos();
    }

    public void getLoaderTEICorpus(InputStream file) {
        this.textProvidesInterface.LoadTEICorpus(file);
    }

    public String getLoadTEIFragmentsAtOnce(InputStream file) {
        return this.textProvidesInterface.LoadFragmentsAtOnce(file);
    }

    public String getLoadTEIFragmentsStepByStep(InputStream file) {
        return this.textProvidesInterface.LoadTEIFragmentsStepByStep(file);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean highlightDiff) {
        return this.textProvidesInterface.getWriteFromPlainHtmlWriter4OneInter(xmlId, highlightDiff);
    }

    public String getWriteFromPlainHtmlWriter4OneInter(String xmlId, boolean displayDiff, boolean displayDel, boolean highlightIns, boolean highlightSubst, boolean showNotes, boolean showFacs, String pbText) {
        return this.textProvidesInterface.getWriteFromPlainHtmlWriter4OneInter(xmlId, displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
    }

    public void removeFragmentByExternalId(String externalId) {
        this.textProvidesInterface.removeFragmentByExternalId(externalId);
    }

    public String getAppTranscription(String xmlId, String externalAppId) {
        return this.textProvidesInterface.getAppTranscriptionFromHtmlWriter4Variations(xmlId, externalAppId);
    }

    public List<String> putAppTextWithVariations(String externalId, List<ScholarInterDto> scholarInters) {
        return this.textProvidesInterface.putAppTextWithVariations(externalId, scholarInters);
    }

    public String exportWithQueryExpertEditionTEI(String query) {
        return this.textProvidesInterface.exportExpertEditionTEI(query);
    }

    public String exportAllExpertEditionTEI() {
        return this.textProvidesInterface.exportAllExpertEditionTEI();
    }

    public String exportRandomExpertEditionTEI() {
        return this.textProvidesInterface.exportRandomExpertEditionTEI();
    }

    public boolean isDomainObjectScholarInter(String interId) {
        return this.textProvidesInterface.isDomainObjectScholarInter(interId);
    }

    public boolean isDomainObjectScholarEdition(String externalId) {
        return this.textProvidesInterface.isDomainObjectScholarEdition(externalId);
    }

    public String getScholarEditionAcronymbyExternal(String externalId) {
        return  this.textProvidesInterface.getScholarEditionAcronymbyExternal(externalId);
    }

    public String getWriteFromHtmlWriter2CompIntersLineByLine(List<ScholarInterDto> scholarInters, boolean lineByLine, boolean showSpaces) {
        return this.textProvidesInterface.getWriteFromHtmlWriter2CompIntersLineByLine(scholarInters, lineByLine, showSpaces);
    }

    public String getTranscriptionFromHtmlWriter2CompInters(List<ScholarInterDto> scholarInters, ScholarInterDto inter, boolean lineByLine, boolean showSpaces) {
        return this.textProvidesInterface.getTranscriptionFromHtmlWriter2CompInters(scholarInters, inter, lineByLine, showSpaces);
    }

    public SurfaceDto getSurfaceFromPbTextId(String pbTextId, String interId) {
        return this.textProvidesInterface.getSurfaceFromPbTextId(pbTextId, interId);
    }

    public SurfaceDto getPrevSurface(String pbTextID, String interId) {
        return this.textProvidesInterface.getPrevSurfaceFromPbTextId(pbTextID, interId);
    }

    public SurfaceDto getNextSurface(String pbTextID, String interId) {
        return this.textProvidesInterface.getNextSurfaceFromPbTextId(pbTextID, interId);
    }

    public String getPrevPbTextExternalId(String pbTextID, String interId) {
        return this.textProvidesInterface.getPrevPbTextExternalId(pbTextID, interId);
    }

    public String getNextPbTextExternalId(String pbTextID, String interId) {
        return this.textProvidesInterface.getNextPbTextExternalId(pbTextID, interId);
    }

    // Uses Virtual Edition Module
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        return this.virtualProvidesInterface.getVirtualEditionByAcronym(acronym);
    }

    public VirtualEditionInterDto getVirtualEditionInterByUrlId(String urlId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByUrlId(urlId);
    }

    public VirtualEditionInterDto getVirtualEditionInterByExternalId(String externalId) {
        return this.virtualProvidesInterface.getVirtualEditionInterByExternalId(externalId);
    }

    public void getLoaderTEICorpusVirtual(InputStream file) {
        this.virtualProvidesInterface.loadTEICorpusVirtual(file);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void getFragmentCorpusGenerator() {
        Set<FragmentDto> fragments = this.getFragmentDtosWithScholarInterDtos();
        this.virtualProvidesInterface.loadTEIFragmentCorpus(fragments);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public boolean initializeTextModule() {
        return this.textProvidesInterface.initializeTextModule();
    }

}