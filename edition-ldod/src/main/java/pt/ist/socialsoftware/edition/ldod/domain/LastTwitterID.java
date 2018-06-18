package pt.ist.socialsoftware.edition.ldod.domain;

public class LastTwitterID extends LastTwitterID_Base {

	public LastTwitterID() {
		super();
	}

	public long getLastTwitterID(String fileName) {
		if (fileName.contains("fp")) {
			return getPessoaLastTwitterID();
		} else if (fileName.contains("livro")) {
			return getBookLastTwitterID();
		} else if (fileName.contains("bernardo")) {
			return getBernardoLastTwitterID();
		} else if (fileName.contains("vicente")) {
			return getVicenteLastTwitterID();
		}
		return 0; // is it the best policy by default to return 0 instead?
	}

	public void updateLastTwitterID(String fileName, long newID) {
		if (fileName.contains("fp")) {
			setPessoaLastTwitterID(newID);
		} else if (fileName.contains("livro")) {
			setBookLastTwitterID(newID);
		} else if (fileName.contains("bernardo")) {
			setBernardoLastTwitterID(newID);
		} else if (fileName.contains("vicente")) {
			setVicenteLastTwitterID(newID);
		}
	}

	public void resetTwitterIDS() {
		setPessoaLastTwitterID(0);
		setBookLastTwitterID(0);
		setBernardoLastTwitterID(0);
		setVicenteLastTwitterID(0);
	}

}
