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

	public String getLastParsedFile(String fileName) {
		if (fileName.contains("fp")) {
			return getLastPessoaParsedFile();
		} else if (fileName.contains("livro")) {
			return getLastBookParsedFile();
		} else if (fileName.contains("bernardo")) {
			return getLastBernardoParsedFile();
		} else if (fileName.contains("vicente")) {
			return getLastVicenteParsedFile();
		}
		return null;

	}

	public void updateLastTwitterID(String fileName, long newID) {

		if (fileName.contains("fp")) {
			setLastPessoaParsedFile(fileName);
			setPessoaLastTwitterID(newID);
		} else if (fileName.contains("livro")) {
			setLastBookParsedFile(fileName);
			setBookLastTwitterID(newID);
		} else if (fileName.contains("bernardo")) {
			setLastBernardoParsedFile(fileName);
			setBernardoLastTwitterID(newID);
		} else if (fileName.contains("vicente")) {
			setLastVicenteParsedFile(fileName);
			setVicenteLastTwitterID(newID);
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
		setLdoD(null);
		deleteDomainObject();
	}

}
