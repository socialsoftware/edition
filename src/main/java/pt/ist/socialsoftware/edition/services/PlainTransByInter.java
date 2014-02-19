/**
 * 
 */
package pt.ist.socialsoftware.edition.services;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

/**
 * Given an interpretation and a fragment provides a plain transcription for the
 * fragment according to the interpretation
 * 
 * @author ars
 * 
 */
public class PlainTransByInter extends LdoDService {

	private String fragInterExternalID = null;

	public void setFragInterExternalID(String fragInterExternalID) {
		this.fragInterExternalID = fragInterExternalID;
	}

	String transcription = null;

	public String getTranscription() {
		return transcription;
	}

	private FragInter fragInter = null;

	@Override
	void execution() throws LdoDException {
		this.fragInter = (FragInter) FenixFramework
				.getDomainObject(this.fragInterExternalID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(this.fragInter);

		this.fragInter.getFragment().getTextPortion().accept(writer);
		transcription = writer.getTranscription();
	}
}
