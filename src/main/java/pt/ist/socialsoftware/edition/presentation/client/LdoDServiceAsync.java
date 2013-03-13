package pt.ist.socialsoftware.edition.presentation.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LdoDServiceAsync {

	void initDatabase(AsyncCallback<Void> callback);

	void plainTransByInter(AsyncCallback<String> callback);

}
