package pt.ist.socialsoftware.edition.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.recommendation.VSMVirtualEditionInterRecommender;
import pt.ist.socialsoftware.edition.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.utils.RangeJson;

public class VirtualEditionInter extends VirtualEditionInter_Base {
	private static Logger logger = LoggerFactory.getLogger(VirtualEditionInter.class);

	public VirtualEditionInter(Section section, FragInter inter, int number) {
		setFragment(inter.getFragment());
		setHeteronym(null);
		setLdoDDate(null);
		setSection(section);
		setNumber(number);
		setUses(inter);
	}

	@Override
	public void remove() {
		for (VirtualEditionInter inter : getIsUsedBySet()) {
			inter.setUses(getUses());
		}

		setSection(null);

		setUses(null);

		for (Tag tag : getTagSet()) {
			tag.remove();
		}

		for (Annotation annotation : getAnnotationSet()) {
			annotation.remove();
		}

		super.remove();
	}

	@Override
	public String getShortName() {
		return getVirtualEdition().getAcronym();
	}

	@Override
	public String getTitle() {
		return getUses().getTitle();
	}

	@Override
	public EditionType getSourceType() {
		return EditionType.VIRTUAL;
	}

	public int compareVirtualEditionInter(VirtualEditionInter other) {
		if (this.getVirtualEdition() == other.getVirtualEdition()) {
			int diff = getNumber() - other.getNumber();
			int result = diff > 0 ? 1 : (diff < 0) ? -1 : 0;
			if (result != 0) {
				return result;
			} else {
				String myTitle = getTitle();
				String otherTitle = other.getTitle();
				return myTitle.compareTo(otherTitle);
			}
		} else {
			return this.getVirtualEdition().getTitle().compareTo(other.getVirtualEdition().getTitle());
		}

	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Edição Virtual: " + getVirtualEdition().getTitle() + "(" + getVirtualEdition().getAcronym()
				+ ")" + "<br>";

		result = result + "Edição Base: " + getLastUsed().getShortName() + "<br>";

		result = result + "Título: " + getTitle() + "<br>";

		if (getHeteronym() != null) {
			result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";
		}

		if (getLdoDDate() != null) {
			result = result + "Data: " + getLdoDDate().print() + "<br>";
		}

		return result;
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return getVirtualEdition() == edition;
	}

	@Override
	public FragInter getLastUsed() {
		return getUses().getLastUsed();
	}

	@Override
	public Edition getEdition() {
		return getVirtualEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = getUses().getListUsed();
		listUses.add(0, getUses());
		return listUses;
	}

	@Override
	public String getReference() {
		return Integer.toString(getNumber());
	}

	public Set<LdoDUser> getAnnotationContributorSet() {
		Set<LdoDUser> contributors = new HashSet<LdoDUser>();
		for (Annotation annotation : getAnnotationSet()) {
			contributors.add(annotation.getUser());
		}
		return contributors;
	}

	public VirtualEdition getVirtualEdition() {
		return getSection().getRootSection().getVirtualEdition();
	}

	public FragInter getNextInter() {
		LdoDUser user = LdoDUser.getAuthenticatedUser();
		VSMVirtualEditionInterRecommender recommender = new VSMVirtualEditionInterRecommender();
		Collection<Property> properties = user.getRecommendationWeights(getVirtualEdition()).getProperties();
		return recommender.getMostSimilarItem(this, getVirtualEdition().getVirtualEditionIntersSet(), properties);
	}

	@Atomic(mode = TxMode.WRITE)
	public Annotation createAnnotation(String quote, String text, LdoDUser user, List<RangeJson> rangeList,
			List<String> tagList) {
		logger.debug("createAnnotation start:{}, startOffset:{}, end:{}, endOffset:{}", rangeList.get(0).getStart(),
				rangeList.get(0).getStartOffset(), rangeList.get(0).getEnd(), rangeList.get(0).getEndOffset());

		SimpleText startText = null;
		// startText =
		// getFragment().getTextPortion().getSimpleText(getLastUsed(), 0,
		// rangeList.get(0).getStartOffset());
		SimpleText endText = null;
		// endText = getFragment().getTextPortion().getSimpleText(getLastUsed(),
		// 0, rangeList.get(0).getEndOffset());

		Annotation annotation = new Annotation(this, startText, endText, quote, text, user);

		for (RangeJson rangeJson : rangeList) {
			new Range(annotation, rangeJson.getStart(), rangeJson.getStartOffset(), rangeJson.getEnd(),
					rangeJson.getEndOffset());
		}

		for (String tag : tagList) {
			createTag(annotation.getUser(), tag, annotation);
		}

		return annotation;
	}

	@Atomic(mode = TxMode.WRITE)
	public void associate(LdoDUser user, Set<String> categoryNames) {
		getAssignedCategories(user).stream()
				.filter(c -> !categoryNames.contains(c.getNameInEditionContext(getVirtualEdition())))
				.forEach(c -> dissociate(user, c));

		Set<String> existingCategories = getAssignedCategories(user).stream()
				.map(c -> c.getNameInEditionContext(getVirtualEdition())).collect(Collectors.toSet());

		Set<String> toAssociate = categoryNames.stream().filter(cname -> !existingCategories.contains(cname))
				.collect(Collectors.toSet());

		for (String categoryName : toAssociate) {
			createTag(user, categoryName, null);
		}

	}

	@Atomic(mode = TxMode.WRITE)
	public void dissociate(LdoDUser user, Category category) {
		Set<Tag> tags = getTagSet().stream().filter(t -> (t.getCategory() == category) && (t.getContributor() == user))
				.collect(Collectors.toSet());
		for (Tag tag : tags) {
			tag.remove();
		}

		Set<Annotation> annotations = getAnnotationSet().stream()
				.filter(a -> a.getTagSet().isEmpty() && a.getText() == null).collect(Collectors.toSet());
		for (Annotation annotation : annotations) {
			annotation.remove();
		}
	}

	public List<Category> getAssignedCategories() {
		return getAllDepthTags().stream().map(t -> t.getCategory()).distinct()
				.sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
				.collect(Collectors.toList());
	}

	public List<Category> getNonAssignedCategories(LdoDUser user) {
		List<Category> interCategories = getAssignedCategories(user);

		List<Category> categories = getAllDepthCategories().stream().filter(c -> !interCategories.contains(c))
				.sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
				.collect(Collectors.toList());

		return categories;
	}

	public List<Category> getAssignedCategories(LdoDUser user) {
		List<Category> categories = getAllDepthTags().stream().filter(t -> t.getContributor() == user)
				.map(t -> t.getCategory()).distinct()
				.sorted((c1, c2) -> c1.compareInEditionContext(this.getVirtualEdition(), c2))
				.collect(Collectors.toList());

		return categories;
	}

	public List<Category> getSortedCategories(VirtualEdition virtualEdition) {
		Taxonomy taxonomy = virtualEdition.getTaxonomy();
		return getTagSet().stream().filter(t -> t.getCategory().getTaxonomy() == taxonomy).map(t -> t.getCategory())
				.distinct().sorted().collect(Collectors.toList());
	}

	@Override
	public Set<Category> getAllDepthCategories() {
		Set<Category> categories = null;
		if (getVirtualEdition().checkAccess())
			categories = new HashSet<Category>(getVirtualEdition().getTaxonomy().getCategoriesSet());
		else
			categories = new HashSet<Category>();

		categories.addAll(getUses().getAllDepthCategories());

		return categories;
	}

	@Override
	public Set<Annotation> getAllDepthAnnotations() {
		Set<Annotation> annotations = null;
		if (getVirtualEdition().checkAccess())
			annotations = new HashSet<Annotation>(getAnnotationSet());
		else
			annotations = new HashSet<Annotation>();

		annotations.addAll(getUses().getAllDepthAnnotations());

		return annotations;
	}

	@Override
	public Set<Tag> getAllDepthTags() {
		Set<Tag> tags = null;

		if (getVirtualEdition().checkAccess())
			tags = new HashSet<Tag>(getTagSet());
		else
			tags = new HashSet<Tag>();

		tags.addAll(getUses().getAllDepthTags());

		return tags;
	}

	public Set<LdoDUser> getContributorSet(Category category) {
		return getAllDepthTags().stream().filter(t -> t.getCategory() == category).map(t -> t.getContributor())
				.collect(Collectors.toSet());
	}

	public String getAllDepthCategoriesJSON() {
		ObjectMapper mapper = new ObjectMapper();

		List<CategoryDTO> categories = getAllDepthCategories().stream()
				.sorted((c1, c2) -> c1.compareInEditionContext(getVirtualEdition(), c2))
				.map(c -> new CategoryDTO(getVirtualEdition(), c)).collect(Collectors.toList());

		try {
			return mapper.writeValueAsString(categories);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private void createTag(LdoDUser user, String categoryName, Annotation annotation) {
		if (categoryName.contains(".")) {
			String[] values = categoryName.split("\\.");
			VirtualEdition edition = (VirtualEdition) LdoD.getInstance().getEdition(values[0]);
			if (edition.getTaxonomy().getCategory(values[1]) != null)
				edition.getTaxonomy().createTag(this, values[1], annotation, user);
			else
				throw new LdoDException("Cannot create Category in an inherited Virtual Edition");
		} else {
			getVirtualEdition().getTaxonomy().createTag(this, categoryName, annotation, user);
		}
	}

	public void updateTags(Annotation annotation, List<String> tags) {
		for (Tag tag : annotation.getTagSet()) {
			if (!tags.contains(tag.getCategory().getNameInEditionContext(getVirtualEdition()))) {
				tag.remove();
			}
		}

		for (String tag : tags) {
			if (!existsTag(tag)) {
				createTag(annotation.getUser(), tag, annotation);
			}
		}

	}

	private boolean existsTag(String name) {
		for (Tag tag : getTagSet()) {
			if (tag.getCategory().getNameInEditionContext(getVirtualEdition()).equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Set<VirtualEdition> getUsedIn() {
		Set<VirtualEdition> editions = getIsUsedBySet().stream().flatMap(i -> i.getUsedIn().stream())
				.collect(Collectors.toSet());
		editions.add(getVirtualEdition());

		return editions;

	}

}
