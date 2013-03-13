package pt.ist.socialsoftware.edition.presentation.client;

import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface LdoDService extends RemoteService {

	public void initDatabase() throws LdoDException;

	public String plainTransByInter() throws LdoDException;
}
