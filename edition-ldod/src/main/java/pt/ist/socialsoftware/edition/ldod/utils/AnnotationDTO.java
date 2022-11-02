package pt.ist.socialsoftware.edition.ldod.utils;

import org.apache.commons.lang.StringEscapeUtils;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnotationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private List<RangeJson> ranges;
    private String quote;
    private String text;
    private List<String> tags;
    private String uri;
    private String user;
    private PermissionDTO permissions;

    public AnnotationDTO() {
    }

    public AnnotationDTO(Annotation annotation) {
        if (annotation instanceof AwareAnnotation) {
            // String text = "<a href=\"https://www.w3schools.com/html/\">Visit our HTML
            // tutorial!</a>";

            // String text = "&lta href=\"https://www.w3schools.com/html/\"&gtVisit our HTML
            // tutorial!&lt/a&gt";

            // String text = "<html><body><p>" + "<a
            // href=\"https://www.w3schools.com/html/\">Visit our HTML tutorial</a>"
            // + "</p></body></html>";

            // String text = "&lthtml&gt&ltbody&gt&ltp&gt"
            // + "&lta href=&quothttps://www.w3schools.com/html/&quot&gtVisit our HTML
            // tutorial&lt/a&gt"
            // + "&lt/p&gt&lt/body&gt&lt/html&gt";

            setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
        } else if (annotation instanceof HumanAnnotation) {
            setText(StringEscapeUtils.unescapeHtml(annotation.getText()));
        }

        setId(annotation.getExternalId());
        setQuote(StringEscapeUtils.unescapeHtml(annotation.getQuote()));
        setUri(annotation.getVirtualEditionInter().getExternalId());
        setRanges(annotation.getRangeSet().stream().map(RangeJson::new).collect(Collectors.toList()));
        setUser(annotation.getUser().getUsername());

        // code that supports treatment for Human Annotation
        if (annotation instanceof HumanAnnotation) {
            this.tags = new ArrayList<>();
            for (Tag tag : ((HumanAnnotation) annotation).getTagSet()) {
                this.tags.add(tag.getCategory().getNameInEditionContext(
                        ((HumanAnnotation) annotation).getVirtualEditionInter().getVirtualEdition()));
            }

            setPermissions(
                    new PermissionDTO(((HumanAnnotation) annotation).getVirtualEditionInter().getVirtualEdition(),
                            ((HumanAnnotation) annotation).getUser()));
        }
    }

    public List<RangeJson> getRanges() {
        return this.ranges;
    }

    public void setRanges(List<RangeJson> range) {
        this.ranges = range;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public PermissionDTO getPermissions() {
        return this.permissions;
    }

    public void setPermissions(PermissionDTO permissions) {
        this.permissions = permissions;
    }

}
