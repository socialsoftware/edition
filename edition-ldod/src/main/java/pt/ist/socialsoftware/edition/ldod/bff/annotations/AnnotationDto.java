package pt.ist.socialsoftware.edition.ldod.bff.annotations;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.utils.RangeJson;

import java.util.List;
import java.util.stream.Collectors;

public class AnnotationDto {

    private String externalId;

    private String uri;

    private boolean human;
    private String quote;
    private String text;
    private String sourceLink;
    private String profileURL;
    private String country;
    private String date;
    private String username;
    private List<String> tagList;
    private List<RangeJson> ranges;

    private String contents;
    private boolean canBeRead;
    private boolean canBeUpdated;
    private boolean canBeDeleted;


    public AnnotationDto() {
    }

    public AnnotationDto(Annotation annotation, LdoDUser user) {
        setUri(annotation.getVirtualEditionInter().getExternalId());
        setExternalId(annotation.getExternalId());
        setHuman(annotation.isHumanAnnotation());
        setQuote(annotation.getQuote());
        setUsername(annotation.getUser().getUsername());
        setRanges(annotation.getRangeSet().stream().map(RangeJson::new).collect(Collectors.toList()));
        if (isHuman()) {
            setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
            setTagList(((HumanAnnotation) annotation).getTagSet()
                    .stream()
                    .map(t -> t.getCategory().getNameInEditionContext(annotation.getVirtualEditionInter().getVirtualEdition()))
                    .collect(Collectors.toList()));
            VirtualEdition ve = annotation.getVirtualEditionInter().getVirtualEdition();
            setCanBeRead(ve.getPub() || (user != null && ve.getParticipantSet().contains((user))));
            setCanBeUpdated(((HumanAnnotation) annotation).canUpdate(user));
            setCanBeDeleted(((HumanAnnotation) annotation).canDelete(user));
            setContents(((HumanAnnotation) annotation).getContents());

        } else {
            setDate(((AwareAnnotation) annotation).getDate());
            setSourceLink(((AwareAnnotation) annotation).getSourceLink());
            setProfileURL(((AwareAnnotation) annotation).getProfileURL());
            setCountry(((AwareAnnotation) annotation).getCountry());
        }
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public boolean isCanBeRead() {
        return canBeRead;
    }

    public void setCanBeRead(boolean canBeRead) {
        this.canBeRead = canBeRead;
    }

    public boolean isCanBeUpdated() {
        return canBeUpdated;
    }

    public void setCanBeUpdated(boolean canBeUpdated) {
        this.canBeUpdated = canBeUpdated;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public List<RangeJson> getRanges() {
        return ranges;
    }

    public void setRanges(List<RangeJson> ranges) {
        this.ranges = ranges;
    }

}
