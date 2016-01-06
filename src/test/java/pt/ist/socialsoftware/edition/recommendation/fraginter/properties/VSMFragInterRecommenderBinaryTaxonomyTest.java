package pt.ist.socialsoftware.edition.recommendation.fraginter.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.mallet.TopicModeler;
import pt.ist.socialsoftware.edition.recommendation.properties.BinaryTaxonomyProperty;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

@Ignore
public class VSMFragInterRecommenderBinaryTaxonomyTest extends VSMFragInterRecommenderTest {
	private VirtualEdition edition;

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
		return new BinaryTaxonomyProperty(edition.getTaxonomy());
	}

	@Override
	protected Property getPropertyWithWeight() {
		return new BinaryTaxonomyProperty(2.0, edition.getTaxonomy());
	}

	@Override
	protected Property getPropertyWithZeroWeight() {
		return new BinaryTaxonomyProperty(0., edition.getTaxonomy());
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
			(new TopicModeler()).generate(user, edition, 4, 3, 4, 100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCluster() {
		FragInter inter = getFragment1();
		Collection<FragInter> inters = edition.getIntersSet();

		Map<Integer, Collection<Property>> propertiesMap = new HashMap<Integer, Collection<Property>>();
		propertiesMap.put(0, new ArrayList<Property>());
		propertiesMap.get(0).add(getProperty());

		// propertiesMap.put(1, new ArrayList<Property>());
		// propertiesMap.get(1).add(new BinaryTaxonomyProperty(t2));
		//
		// propertiesMap.put(2, new ArrayList<Property>());
		// propertiesMap.get(2).add(new BinaryTaxonomyProperty(t3));
		// propertiesMap.get(2).add(new BinaryTaxonomyProperty(t4));

		Collection<FragInter> clusteredEdition = recommender.getClusteredEdition(inter, inters, propertiesMap);
		Assert.assertEquals(inters.size(), clusteredEdition.size() - 1);
	}
}
