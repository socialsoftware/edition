package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;

public class VSMFragmentRecommender extends VSMRecommender<Fragment> {

	@Override
	protected Collection<Double> acceptProperty(Fragment t1, Property property) {
		return t1.accept(property);
	}

	@Override
	public Collection<Fragment> getDefaultSet() {
		return LdoD.getInstance().getFragmentsSet();
	}

	@Override
	protected void loadProperty(Fragment t1, Fragment t2, Property property) {
		property.setFragmentsGroup(t1, t2);
	}


	public void bootstrap(LdoD ldod) {
		getMostSimilarItems(ldod.getFragment("Fr001"), getDefaultProperties());
	}

	public Fragment getMostSimilarItemWithRestictions(Fragment fragment, List<String> asList, List<Property> properties) {
		List<Fragment> newList = new ArrayList<>(getDefaultSet());
		Fragment result = null;
		newList.remove(fragment);
		double max = Double.NEGATIVE_INFINITY;
		double similiraty;
		for(Fragment otherItem : newList) {
			if(!asList.contains(otherItem.getExternalId())) {
				similiraty = calculateSimilarity(fragment, otherItem, properties);
				if(similiraty > max) {
					result = otherItem;
					max = similiraty;
				}
			}
		}
		return result;
	}

}