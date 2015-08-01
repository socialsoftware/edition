package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.io.IOException;

import org.joda.time.LocalDate;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.recommendation.properties.WeightTaxonomyProperty;

public class VSMFragInterRecommenderWeightTaxonomyTest extends VSMFragInterRecommenderTest {

	private Edition edition;
	private Taxonomy taxonomy;

	@Override
	protected FragInter getFragment1() {
		return edition.getSortedInterps().get(1);
	}

	@Override
	protected FragInter getFragment2() {
		return edition.getSortedInterps().get(0);
	}

	@Override
	protected Property getProperty() {
		return new WeightTaxonomyProperty(taxonomy);
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new WeightTaxonomyProperty(2.0, taxonomy);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new WeightTaxonomyProperty(0., taxonomy);
	}

	@Override
	protected void prepare() { 
		String username = "afs123456";
		String password = "123456";
		String firstName = "a";
		String lastName = "fs";
		String email = "afs@afs.afs";
		LdoDUser user = new LdoDUser(ldod, username, password, firstName, lastName, email);
		String acronym = "afs";
		String title = "afs";
		LocalDate date = new LocalDate();
		boolean pub = false;
		Edition usedEdition = LdoD.getInstance().getEdition("TSC");
		edition = LdoD.getInstance().createVirtualEdition(user, acronym, title, date, pub, usedEdition);
		try {
			taxonomy = (new TopicModeler()).generate(edition, "test", 4, 3, 4, 100);
			Taxonomy t2 = (new TopicModeler()).generate(edition, "test2", 2, 2, 6, 100);
			Taxonomy t3 = (new TopicModeler()).generate(edition, "test3", 6, 4, 4, 100);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setUp() {
		super.setUp();
	}

}
