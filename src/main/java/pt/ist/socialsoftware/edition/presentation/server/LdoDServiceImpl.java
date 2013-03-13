package pt.ist.socialsoftware.edition.presentation.server;

import pt.ist.fenixframework.pstm.Transaction;
import pt.ist.socialsoftware.edition.domain.DatabaseBootstrap;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.presentation.client.LdoDService;
import pt.ist.socialsoftware.edition.services.LoadLdoDFromTEIService;
import pt.ist.socialsoftware.edition.services.PlainTransByInter;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LdoDServiceImpl extends RemoteServiceServlet implements
		LdoDService {

	private static final long serialVersionUID = 1L;

	private static boolean databasedInitialized = false;

	@Override
	public void initDatabase() {
		if (!databasedInitialized) {
			DatabaseBootstrap.initDatabase();

			LoadLdoDFromTEIService service = new LoadLdoDFromTEIService();
			service.atomicExecution();
			databasedInitialized = true;
		}

	}

	@Override
	public String plainTransByInter() throws LdoDException {

		String fragInterExternalID = null;

		Transaction.begin();
		LdoD ldoD = LdoD.getInstance();

		for (Fragment frag : ldoD.getFragments()) {
			for (FragInter fragInter : frag.getFragmentInter()) {
				if (fragInter.getName().equals("Teresa Sobral Cunha")) {
					fragInterExternalID = fragInter.getExternalId();
				}
			}
		}
		Transaction.commit();

		PlainTransByInter service = new PlainTransByInter();
		service.setFragInterExternalID(fragInterExternalID);
		service.atomicExecution();
		return service.getTranscription();
	}

}
