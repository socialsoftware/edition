package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.fenixframework.Atomic;

public class LastTwitterID extends LastTwitterID_Base {

    public static final String FP_CITATIONS = "fp";
    public static final String LIVRO_CITATIONS = "livro";
    public static final String BERNARDO_CITATIONS = "bernardo";
    public static final String VICENTE_CITATIONS = "vicente";

    public LastTwitterID() {
        super();
    }

    public long getLastTwitterID(String fileName) {
        if (fileName.contains(FP_CITATIONS)) {
            return getPessoaLastTwitterID();
        } else if (fileName.contains(LIVRO_CITATIONS)) {
            return getBookLastTwitterID();
        } else if (fileName.contains(BERNARDO_CITATIONS)) {
            return getBernardoLastTwitterID();
        } else if (fileName.contains(VICENTE_CITATIONS)) {
            return getVicenteLastTwitterID();
        }
        return 0; // is it the best policy by default to return 0 instead?
    }

    public String getLastParsedFile(String fileName) {
        if (fileName.contains(FP_CITATIONS)) {
            return getLastPessoaParsedFile();
        } else if (fileName.contains(LIVRO_CITATIONS)) {
            return getLastBookParsedFile();
        } else if (fileName.contains(BERNARDO_CITATIONS)) {
            return getLastBernardoParsedFile();
        } else if (fileName.contains(VICENTE_CITATIONS)) {
            return getLastVicenteParsedFile();
        }
        return null;

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateLastTwitterID(String fileName, long newID) {
        if (fileName.contains(FP_CITATIONS)) {
            setPessoaLastTwitterID(newID);
        } else if (fileName.contains(LIVRO_CITATIONS)) {
            setBookLastTwitterID(newID);
        } else if (fileName.contains(BERNARDO_CITATIONS)) {
            setBernardoLastTwitterID(newID);
        } else if (fileName.contains(VICENTE_CITATIONS)) {
            setVicenteLastTwitterID(newID);
        }
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public void updateLastParsedFile(String fileName) {
        if (fileName.contains(FP_CITATIONS)) {
            setLastPessoaParsedFile(fileName);
        } else if (fileName.contains(LIVRO_CITATIONS)) {
            setLastBookParsedFile(fileName);
        } else if (fileName.contains(BERNARDO_CITATIONS)) {
            setLastBernardoParsedFile(fileName);
        } else if (fileName.contains(VICENTE_CITATIONS)) {
            setLastVicenteParsedFile(fileName);
        }
    }

    public void resetTwitterIDS() {
        setLastPessoaParsedFile(null);
        setLastBookParsedFile(null);
        setLastBernardoParsedFile(null);
        setLastVicenteParsedFile(null);

        setPessoaLastTwitterID(0);
        setBookLastTwitterID(0);
        setBernardoLastTwitterID(0);
        setVicenteLastTwitterID(0);
    }

    public void remove() {
        setVirtualModule(null);
        deleteDomainObject();
    }

}
