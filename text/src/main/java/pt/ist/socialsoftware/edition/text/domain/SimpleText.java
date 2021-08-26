package pt.ist.socialsoftware.edition.text.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.text.config.BeanUtil;
import pt.ist.socialsoftware.edition.text.api.TextEventPublisher;
import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

/**
 * Contains the simple text, no line or pages breaks, spaces, formating, etc
 *
 * @author ars
 */
public class SimpleText extends SimpleText_Base {
    private static final Logger logger = LoggerFactory.getLogger(SimpleText.class);

    public SimpleText(TextPortion parent, String value) {
        parent.addChildText(this);
        setValue(value);
    }

    @Override
    public void remove() {

//        EventInterface.getInstance().publish(new Event(Event.EventType.SIMPLE_TEXT_REMOVE, this.getXmlId()));
        TextEventPublisher eventPublisher = BeanUtil.getBean(TextEventPublisher.class);
        eventPublisher.publishEvent(new Event(Event.EventType.SIMPLE_TEXT_REMOVE, this.getExternalId()));

        super.remove();
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);

    }

    @Override
    public SimpleText getNextSimpleText(ScholarInter inter) {
        return this;
    }

    @Override
    public Boolean getBreakWord() {
        return true;
    }

    @Override
    public String getSeparator(ScholarInter inter) {
        if (getInterps().contains(inter)) {
            String separators = ".,?!:;";
            String separator = null;

            String firstChar = getValue().substring(0, 1);

            if (separators.contains(firstChar) || !super.getBreakWord()) {
                separator = "";
            } else {
                separator = " ";
            }
            return separator;
        } else {
            return super.getSeparator(inter);
        }
    }

    @Override
    public SimpleText getSimpleText(ScholarInter inter, int currentOffset, int offset) {
        int nextCurrentOffset = currentOffset + getSeparator(inter).length() + getValue().length();

        logger.debug("getSimpleText value:{}, separator:{}, currentOffset:{}, offset:{}, nextCurrentOffset:{}",
                getValue(), getSeparator(inter), currentOffset, offset, nextCurrentOffset);

        if (nextCurrentOffset >= offset) {
            return this;
        } else {
            return super.getNextSimpleText(inter).getSimpleText(inter, nextCurrentOffset, offset);
        }
    }

}
