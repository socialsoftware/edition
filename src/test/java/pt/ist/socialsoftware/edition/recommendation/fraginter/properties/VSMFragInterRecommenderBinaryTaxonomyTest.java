package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.recommendation.properties.BinaryTaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragInterRecommenderBinaryTaxonomyTest extends VSMFragInterRecommenderTest {
	private Edition edition;
	private Taxonomy taxonomy;
	private Taxonomy t2;
	private Taxonomy t3;
	private Taxonomy t4;

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
		return new BinaryTaxonomyProperty(taxonomy);
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new BinaryTaxonomyProperty(2.0, taxonomy);
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new BinaryTaxonomyProperty(0., taxonomy);
	}

	@Override
	protected void prepare() { 
		String username = "afs123456";
		String password = "123456";
		String firstName = "a";
		String lastName = "fs";
		String email = "afs@afs.afs";
		LdoDUser user = new LdoDUser(ldod, username, password, firstName, lastName, email);
		String acronym = "afsaklsjdasjdlasdlj";
		String title = "afslaksjdlkajsjasl";
		LocalDate date = new LocalDate();
		boolean pub = false;
		Edition usedEdition = LdoD.getInstance().getEdition("TSC");
		edition = LdoD.getInstance().createVirtualEdition(user, acronym, title, date, pub, usedEdition);
		try {
			taxonomy = (new TopicModeler()).generate(edition, "test", 4, 3, 4, 100);
			t2 = (new TopicModeler()).generate(edition, "test2", 2, 2, 6, 100);
			t3 = (new TopicModeler()).generate(edition, "test3", 6, 4, 4, 100);
			t4 = (new TopicModeler()).generate(edition, "test4", 8, 6, 4, 100);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setUp() {
		super.setUp();
	}

	@Test
	public void testCluster() {
		FragInter inter = getFragment1();
		Collection<FragInter> inters = edition.getIntersSet();

		Map<Integer, Collection<Property>> propertiesMap = new HashMap<Integer, Collection<Property>>();
		propertiesMap.put(0, new ArrayList<Property>());
		propertiesMap.get(0).add(getProperty());

		propertiesMap.put(1, new ArrayList<Property>());
		propertiesMap.get(1).add(new BinaryTaxonomyProperty(t2));

		propertiesMap.put(2, new ArrayList<Property>());
		propertiesMap.get(2).add(new BinaryTaxonomyProperty(t3));
		propertiesMap.get(2).add(new BinaryTaxonomyProperty(t4));

		Collection<FragInter> clusteredEdition = recommender.getClusteredEdition(inter, inters, propertiesMap);
		Assert.assertEquals(inters.size(), clusteredEdition.size() - 1);
	}
}
