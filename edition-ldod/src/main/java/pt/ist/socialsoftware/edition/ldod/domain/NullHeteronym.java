package pt.ist.socialsoftware.edition.ldod.domain;

public class NullHeteronym extends NullHeteronym_Base {

    public static NullHeteronym getNullHeteronym() {
        for (Heteronym heteronym : TextModule.getInstance().getHeteronymsSet()) {
            if (heteronym instanceof NullHeteronym) {
                return (NullHeteronym) heteronym;
            }
        }
        return new NullHeteronym();
    }

    public NullHeteronym() {
        setTextModule(TextModule.getInstance());
    }

    @Override
    public boolean isNullHeteronym() {
        return true;
    }

    @Override
    public String getName() {
        return "não atribuído";
    }

}
