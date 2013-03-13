/**
 * 
 */
package pt.ist.socialsoftware.edition.services;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.visitors.PlainText4InterWriter;

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
		this.fragInter = (FragInter) AbstractDomainObject
				.fromExternalId(this.fragInterExternalID);

		PlainText4InterWriter writer = new PlainText4InterWriter(this.fragInter);
		this.fragInter.getFragment().getVariationPoint().accept(writer);
		transcription = writer.getResult();

	}

}
