package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.text.feature.generators.TextPortionVisitor;

public class RefText extends RefText_Base {

    public enum RefType {
        GRAPHIC("graphic"), WITNESS("witness"), FRAGMENT("fragment");

        private final String desc;

        RefType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public RefText(TextPortion parent, RefType type, String target) {
        parent.addChildText(this);
        setType(type);
        setTarget(target);
        setSurface(null);
        setScholarInter(null);
        setRefFrag(null);
    }

    @Override
    public Fragment getRefFrag() {
        Fragment fragment = super.getRefFrag();
        if (fragment == null) {
            fragment = TextModule.getInstance().getFragmentByXmlId(getTarget());
            atomicWriteRefFrag(fragment);
        }
        return fragment;
    }

    @Atomic(mode = TxMode.WRITE)
    private void atomicWriteRefFrag(Fragment fragment) {
        setRefFrag(fragment);
    }

    @Override
    public void remove() {
        setSurface(null);
        setScholarInter(null);
        // it is necessary to avoid lazy reload of refFrag
        setTarget("");
        setRefFrag(null);

        super.remove();
    }

    @Override
    public void accept(TextPortionVisitor visitor) {
        visitor.visit(this);
    }

}
