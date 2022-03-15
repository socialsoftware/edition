package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.ldod.search.options.SearchOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FragInterDto {

    private String xmlId;
    private String urlId;
    private String title;
    private String date;
    private String heteronym;
    private int number;
    private List<UsedDto> usedList;
    private List<CategoryDto> categoryList;
    private String acronym;
    private String reference;
    private int startPage;
    private int endPage;
    private String volume;
    private String editionTitle;
    private List<UserDto> userDtoList;
    private String externalId;
    private String shortName;
    private String simpleName;
    private EditionType type;
    private String fragment_title;
    private String fragment_externalId;
    private String fragment_xmlId;
    private String editor;
    private int search_size;
    private SourceType sourceType;
    private boolean handNoteSetEmptyBoolean;
    private boolean typeNoteSetEmptyBoolean;
    private Boolean hasLdoDLabel;
    private String name;
    private String ldoDDatePrint;
    private String ldoDDatePrintExpert;
    private List<SearchOption> search;
    private List<AnnexNoteDto> annexNoteDtoList;
    private String notes;
    private String usesReference;
    private String usesSecondReference;
    private ArrayList<CategoryUserDto> categoryUserDtoList;
    private String virtualExternalId;
    private List<CategoryDto> allCategories;


    public FragInterDto(FragInter fragInter) {
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        if (fragInter.getLdoDDate() != null) {
            this.setDate(fragInter.getLdoDDate().print());
        }

        if (fragInter.getHeteronym() != null) {
            if (fragInter.getHeteronym().getName() != null) {
                this.setHeteronym(fragInter.getHeteronym().getName());
            }

        }



        this.setNumber(fragInter.getNumber());
        this.setAcronym(fragInter.getEdition().getAcronym());
        this.setReference(fragInter.getEdition().getReference());

        this.setUsedList(fragInter.getListUsed().stream()
                .map(UsedDto::new)
                .collect(Collectors.toList()));

        if (fragInter instanceof ExpertEditionInter) {
            this.setStartPage(((ExpertEditionInter) fragInter).getStartPage());
            this.setEndPage(((ExpertEditionInter) fragInter).getEndPage());
            this.setAnnexNoteDtoList(((ExpertEditionInter) fragInter).getSortedAnnexNote().stream()
                    .map(AnnexNoteDto::new)
                    .collect(Collectors.toList()));
            this.setNotes(((ExpertEditionInter) fragInter).getNotes());

            if (((ExpertEditionInter) fragInter).getVolume() != null) {
                this.setVolume(((ExpertEditionInter) fragInter).getVolume());
            }
        }

        this.setExternalId(fragInter.getExternalId());
        this.setShortName(fragInter.getShortName());
        this.setSimpleName(fragInter.getClass().getSimpleName());
        this.setType(fragInter.getSourceType());
        if (fragInter instanceof SourceInter) {
            if (fragInter.getClass().getSimpleName().equals("SourceInter")) {
                this.setSourceType(((SourceInter) fragInter).getSource().getType());
            }

        }
        this.setEditionTitle(fragInter.getEdition().getTitle());
        this.setUsesReference(fragInter.getLastUsed().getEdition().getReference());
        this.setUsesSecondReference(fragInter.getLastUsed().getReference());

        if (fragInter instanceof VirtualEditionInter) {
            this.setCategoryList(((VirtualEditionInter) fragInter).getAssignedCategories().stream()
                    .map(category -> new CategoryDto(category, (VirtualEdition) fragInter.getEdition(), "deep"))
                    .collect(Collectors.toList()));


            ArrayList<CategoryUserDto> arr = new ArrayList<CategoryUserDto>();

            for (Category cat : ((VirtualEditionInter) fragInter).getAssignedCategories()) {
                for (UserDto user : ((VirtualEditionInter) fragInter).getContributorSet(cat).stream().map(UserDto::new).collect(Collectors.toList())) {
                    arr.add(new CategoryUserDto(cat, user));
                }
            }
            this.setCategoryUserDtoList(arr);
            this.setVirtualExternalId(fragInter.getEdition().getExternalId());
        }
        this.setAllCategories(fragInter.getAllDepthCategories().stream().map(CategoryDto::new).collect(Collectors.toList()));
    }

    public FragInterDto(FragInter fragInter, VirtualEdition edition) {
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        if (fragInter.getLdoDDate() != null) {
            this.setDate(fragInter.getLdoDDate().print());
        }

        if (fragInter.getHeteronym() != null) {
            if (fragInter.getHeteronym().getName() != null) {
                this.setHeteronym(fragInter.getHeteronym().getName());
            }

        }

        this.setNumber(fragInter.getNumber());
        this.setUsedList(fragInter.getListUsed().stream()
                .map(UsedDto::new)
                .collect(Collectors.toList()));


        this.setCategoryList(((VirtualEditionInter) fragInter).getAssignedCategories().stream()
                .map(category -> new CategoryDto(category, edition, true))
                .collect(Collectors.toList()));

    }

    public FragInterDto(FragInter fragInter, LdoDUser user) {
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        this.setReference(fragInter.getEdition().getReference());
        this.setAcronym(fragInter.getEdition().getAcronym());


        this.setCategoryList(((VirtualEditionInter) fragInter).getAssignedCategories(user).stream()
                .map(category -> new CategoryDto(category, (VirtualEdition) fragInter.getEdition(), "shallow"))
                .collect(Collectors.toList()));

        this.setUsedList(fragInter.getListUsed().stream()
                .map(UsedDto::new)
                .collect(Collectors.toList()));
    }

    public FragInterDto(FragInter fragInter, Category category) {
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        this.setAcronym(fragInter.getEdition().getAcronym());
        this.setEditionTitle(((VirtualEditionInter) fragInter).getEdition().getTitle());
        this.setUsedList(fragInter.getListUsed().stream()
                .map(UsedDto::new)
                .collect(Collectors.toList()));

        this.setUserDtoList(((VirtualEditionInter) fragInter).getContributorSet(category).stream()
                .map(UserDto::new)
                .collect(Collectors.toList()));
        this.setExternalId(fragInter.getExternalId());
    }

    public FragInterDto(FragInter fragInter, Fragment frag) {
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        this.setExternalId(fragInter.getExternalId());
        this.setShortName(fragInter.getShortName());
        this.setSimpleName(fragInter.getClass().getSimpleName());

        // TODO: Shouldn't be setSourceType instead of setType ??
        this.setType(fragInter.getSourceType());
        if (fragInter instanceof ExpertEditionInter) {
            this.setEditor(((ExpertEdition) fragInter.getEdition()).getEditor());
        }

        this.setFragment_title(frag.getTitle());
        this.setFragment_externalId(frag.getExternalId());

        // TODO: fragment xmlId duplicated ??
        this.setFragment_xmlId(frag.getXmlId());
        this.setUsedList(fragInter.getListUsed().stream()
                .map(UsedDto::new)
                .collect(Collectors.toList()));

    }

    public FragInterDto(Fragment frag, FragInter fragInter, List<SearchOption> search) {
        this.setFragment_title(frag.getTitle());
        this.setFragment_externalId(frag.getExternalId());
        this.setSearch_size(search.size());
        this.setSearch(search);
        this.setXmlId(fragInter.getFragment().getXmlId());
        this.setUrlId(fragInter.getUrlId());
        if (fragInter.getHeteronym() != null) {
            this.setName(fragInter.getHeteronym().getName());
        }

        if (fragInter.getTitle() != null) {
            this.setTitle(fragInter.getTitle());
        }
        this.setExternalId(fragInter.getExternalId());
        this.setShortName(fragInter.getShortName());
        this.setSimpleName(fragInter.getClass().getSimpleName());
        this.setType(fragInter.getSourceType());
        if (fragInter instanceof ExpertEditionInter) {
            this.setEditor(((ExpertEdition) ((ExpertEditionInter) fragInter).getEdition()).getEditor());
            if (((ExpertEditionInter) fragInter).getLdoDDate() != null) {
                this.setLdoDDatePrintExpert(((ExpertEditionInter) fragInter).getLdoDDate().print());
            }

        }
        if (fragInter instanceof SourceInter) {
            if (fragInter.getClass().getSimpleName().equals("SourceInter")) {
                this.setSourceType(((SourceInter) fragInter).getSource().getType());
                if (((SourceInter) fragInter).getSource() instanceof ManuscriptSource) {
                    ManuscriptSource source = (ManuscriptSource) ((SourceInter) fragInter).getSource();
                    this.setHandNoteSetEmptyBoolean(source.getHandNoteSet().isEmpty());
                    this.setTypeNoteSetEmptyBoolean(source.getTypeNoteSet().isEmpty());
                    this.setHasLdoDLabel(source.getHasLdoDLabel());
                    if (source.getLdoDDate() != null) {
                        this.setLdoDDatePrint(source.getLdoDDate().print());
                    }
                }

            }
        }

    }

    public FragInterDto(FragInter fragInter, String type) {
        if (type == "shallow") {
            this.setXmlId(fragInter.getFragment().getXmlId());
            this.setUrlId(fragInter.getUrlId());
            if (fragInter.getTitle() != null) {
                this.setTitle(fragInter.getTitle());
            }
            this.setExternalId(fragInter.getExternalId());
        }

    }

    public String getXmlId() {
        return xmlId;
    }

    public void setXmlId(String xmlId) {
        this.xmlId = xmlId;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getHeteronym() {
        return heteronym;
    }

    public void setHeteronym(String heteronym) {
        this.heteronym = heteronym;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<UsedDto> getUsedList() {
        return usedList;
    }

    public void setUsedList(List<UsedDto> usedList) {
        this.usedList = usedList;
    }

    public List<CategoryDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDto> categoryList) {
        this.categoryList = categoryList;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getEditionTitle() {
        return editionTitle;
    }

    public void setEditionTitle(String editionTitle) {
        this.editionTitle = editionTitle;
    }

    public List<UserDto> getUserDtoList() {
        return userDtoList;
    }

    public void setUserDtoList(List<UserDto> userDtoList) {
        this.userDtoList = userDtoList;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public EditionType getType() {
        return type;
    }

    public void setType(EditionType type) {
        this.type = type;
    }

    public String getFragment_title() {
        return fragment_title;
    }

    public void setFragment_title(String fragment_title) {
        this.fragment_title = fragment_title;
    }

    public String getFragment_externalId() {
        return fragment_externalId;
    }

    public void setFragment_externalId(String fragment_externalId) {
        this.fragment_externalId = fragment_externalId;
    }

    public String getFragment_xmlId() {
        return fragment_xmlId;
    }

    public void setFragment_xmlId(String fragment_xmlId) {
        this.fragment_xmlId = fragment_xmlId;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public int getSearch_size() {
        return search_size;
    }

    public void setSearch_size(int search_size) {
        this.search_size = search_size;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public boolean isHandNoteSetEmptyBoolean() {
        return handNoteSetEmptyBoolean;
    }

    public void setHandNoteSetEmptyBoolean(boolean handNoteSetEmptyBoolean) {
        this.handNoteSetEmptyBoolean = handNoteSetEmptyBoolean;
    }

    public boolean isTypeNoteSetEmptyBoolean() {
        return typeNoteSetEmptyBoolean;
    }

    public void setTypeNoteSetEmptyBoolean(boolean typeNoteSetEmptyBoolean) {
        this.typeNoteSetEmptyBoolean = typeNoteSetEmptyBoolean;
    }

    public Boolean getHasLdoDLabel() {
        return hasLdoDLabel;
    }

    public void setHasLdoDLabel(Boolean hasLdoDLabel) {
        this.hasLdoDLabel = hasLdoDLabel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLdoDDatePrint() {
        return ldoDDatePrint;
    }

    public void setLdoDDatePrint(String ldoDDatePrint) {
        this.ldoDDatePrint = ldoDDatePrint;
    }

    public String getLdoDDatePrintExpert() {
        return ldoDDatePrintExpert;
    }

    public void setLdoDDatePrintExpert(String ldoDDatePrintExpert) {
        this.ldoDDatePrintExpert = ldoDDatePrintExpert;
    }

    public List<SearchOption> getSearch() {
        return search;
    }

    public void setSearch(List<SearchOption> search) {
        this.search = search;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public List<AnnexNoteDto> getAnnexNoteDtoList() {
        return annexNoteDtoList;
    }

    public void setAnnexNoteDtoList(List<AnnexNoteDto> annexNoteDtoList) {
        this.annexNoteDtoList = annexNoteDtoList;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUsesReference() {
        return usesReference;
    }

    public void setUsesReference(String usesReference) {
        this.usesReference = usesReference;
    }

    public String getUsesSecondReference() {
        return usesSecondReference;
    }

    public void setUsesSecondReference(String usesSecondReference) {
        this.usesSecondReference = usesSecondReference;
    }

    public ArrayList<CategoryUserDto> getCategoryUserDtoList() {
        return categoryUserDtoList;
    }

    public void setCategoryUserDtoList(ArrayList<CategoryUserDto> categoryUserDtoList) {
        this.categoryUserDtoList = categoryUserDtoList;
    }

    public String getVirtualExternalId() {
        return virtualExternalId;
    }

    public void setVirtualExternalId(String virtualExternalId) {
        this.virtualExternalId = virtualExternalId;
    }

    public List<CategoryDto> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<CategoryDto> allCategories) {
        this.allCategories = allCategories;
    }

}
