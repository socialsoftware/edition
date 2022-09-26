package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ManuscriptSource extends ManuscriptSource_Base {

    public enum Form {
        LEAF
    }

    ;

    public enum Material {
        PAPER
    }

    ;

    public enum Medium {
        PEN("pen"), PENCIL("pencil"), BLUE_INK("blue-ink"), BLACK_INK("black-ink"), VIOLET_INK("violet-ink"), RED_INK(
                "red-ink"), GREEN_INK("green-ink");

        private final String desc;

        Medium(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }

    ;

    public ManuscriptSource() {
        super();
        setType(SourceType.MANUSCRIPT);
        setHasLdoDLabel(false);
    }

    @Override
    public void remove() {
        getDimensionsSet().forEach(Dimensions::remove);

        for (HandNote handNote : getHandNoteSet()) {
            handNote.remove();
        }

        for (TypeNote typeNote : getTypeNoteSet()) {
            typeNote.remove();
        }

        super.remove();
    }


    public List<Dimensions> getSortedDimensions() {
        return getDimensionsSet().stream().sorted(Comparator.comparingInt(Dimensions_Base::getPosition))
                .collect(Collectors.toList());
    }


}
