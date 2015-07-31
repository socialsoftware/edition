package pt.ist.socialsoftware.edition.recommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SortBy {

	public static <T, P extends Comparable> List<Entry<T, P>> sortByValue(Map<T, P> map) {
		List<Entry<T, P>> list = new ArrayList<Entry<T, P>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<T, P>>() {
			@Override
			public int compare(Map.Entry<T, P> o1, Map.Entry<T, P> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		return list;
	}

}
