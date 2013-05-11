package pt.ist.socialsoftware.edition.domain;

public class User extends User_Base {

	public User(LdoD ldoD, String username, String password) {
		super();
		setLdoD(ldoD);
		setUsername(username);
		setPassword(password);
	}

}
