package pt.ist.socialsoftware.edition.mallet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.InstanceList;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.utils.TopicDTO;
import pt.ist.socialsoftware.edition.utils.TopicInterPercentageDTO;
import pt.ist.socialsoftware.edition.utils.TopicListDTO;

public class TopicModeler {

	private Pipe pipe;
	private final String corpusPath = PropertiesManager.getProperties().getProperty("corpus.dir");
	private final String corpusFilesPath = PropertiesManager.getProperties().getProperty("corpus.files.dir");

	public TopicListDTO generate(LdoDUser user, VirtualEdition edition, int numTopics, int numWords,
			int thresholdCategories, int numIterations) throws IOException {
		// if a corpus is absent
		File directory = new File(corpusFilesPath);
		if (!directory.exists()) {
			throw new LdoDException("corpus is empty");
		}

		pipe = buildPipe();

		InstanceList instances = readDirectory(edition, new File(corpusFilesPath));

		int numInstances = instances.size();

		// there are no fragments
		if (numInstances == 0) {
			throw new LdoDException("corpus is empty");
		}

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// For real applications, use 1000 to 2000 iterations)
		model.setNumIterations(numIterations);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		return createTopicListDTO(user, edition, model, numTopics, numWords, thresholdCategories, numIterations,
				dataAlphabet, numInstances);

	}

	public Pipe buildPipe() {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceLowercase());
		pipeList.add(new TokenSequenceRemoveStopwords(new File(corpusPath + "stoplist-pt.txt"), "UTF-8", false, false,
				false));
		// pipeList.add(new TokenSequenceRemoveStopwords(false, false));
		pipeList.add(new TokenSequence2FeatureSequence());

		return new SerialPipes(pipeList);
	}

	public InstanceList readDirectory(VirtualEdition edition, File directory) {
		return readDirectories(edition, new File[] { directory });
	}

	public InstanceList readDirectories(VirtualEdition edition, File[] directories) {
		// Construct a file iterator, starting with the
		// specified directories, and recursing through subdirectories.
		// The second argument specifies a FileFilter to use to select
		// files within a directory.
		// The third argument is a Pattern that is applied to the
		// filename to produce a class label. In this case, I've
		// asked it to use the last directory name in the path.

		FileIterator iterator = new FileIterator(directories, new EditionFilter(edition), FileIterator.LAST_DIRECTORY);

		// Construct a new instance list, passing it the pipe
		// we want to use to process instances.
		InstanceList instances = new InstanceList(pipe);

		// Now process each instance provided by the iterator.
		instances.addThruPipe(iterator);

		return instances;
	}

	private TopicListDTO createTopicListDTO(LdoDUser user, VirtualEdition edition, ParallelTopicModel model,
			int numTopics, int numWords, int thresholdCategories, int numIterations, Alphabet dataAlphabet,
			int numInstances) {

		TopicListDTO topics = new TopicListDTO();
		topics.setUsername(user.getUsername());

		Taxonomy taxonomy = edition.getTaxonomy();

		topics.setTaxonomyExternalId(taxonomy.getExternalId());

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		topics.setTopics(new ArrayList<TopicDTO>());

		// create a category for each topic
		// counter do avoid duplicate category names
		for (int position = 0; position < numTopics; position++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(position).iterator();

			TopicDTO topic = new TopicDTO();
			topic.setInters(new ArrayList<TopicInterPercentageDTO>());
			topics.getTopics().add(topic);

			assert topics.getTopics().get(position) == topic;

			// associate the words for each category
			int rank = 0;
			String wordName = "";
			while (iterator.hasNext() && rank < numWords) {
				IDSorter idCountPair = iterator.next();
				String word = (String) dataAlphabet.lookupObject(idCountPair.getID());
				wordName = wordName + "(" + word + ")";

				rank++;
			}

			while (taxonomy.getCategory(wordName) != null) {
				wordName = wordName + "_dup";
			}

			topic.setName(wordName);
		}

		// associate categories with fragment interpretations
		for (int instance = 0; instance < numInstances; instance++) {

			String fileName = model.getData().get(instance).instance.getName().toString();
			String[] subs = fileName.split("[/\\.]");
			String externalId = subs[subs.length - 2];
			VirtualEditionInter fragInter = null;
			for (VirtualEditionInter inter : edition.getVirtualEditionIntersSet()) {
				if (inter.getLastUsed().getExternalId().equals(externalId)) {
					fragInter = inter;
					break;
				}
			}

			// Estimate the topic distribution for each instance,
			// given the current Gibbs state.
			double[] topicDistribution = model.getTopicProbabilities(instance);

			for (int position = 0; position < numTopics; position++) {
				BigDecimal bd = new BigDecimal(topicDistribution[position]);
				bd = bd.setScale(2, RoundingMode.HALF_UP);
				int percentage = (int) (bd.doubleValue() * 100);
				if (percentage >= thresholdCategories) {
					TopicInterPercentageDTO interToTopic = new TopicInterPercentageDTO();
					interToTopic.setExternalId(fragInter.getExternalId());
					interToTopic.setTitle(fragInter.getTitle());
					interToTopic.setPercentage(percentage);

					TopicDTO topic = topics.getTopics().get(position);
					topic.getInters().add(interToTopic);
				}
			}
		}

		List<TopicDTO> sortedList = topics.getTopics().stream().sorted((t1, t2) -> t1.getName().compareTo(t2.getName()))
				.collect(Collectors.toList());
		topics.setTopics(sortedList);

		for (TopicDTO topic : sortedList) {
			List<TopicInterPercentageDTO> sortedFrags = topic.getInters().stream()
					.sorted((i1, i2) -> i2.getPercentage() - i1.getPercentage()).collect(Collectors.toList());
			topic.setInters(sortedFrags);
		}

		return topics;
	}

	/** This class illustrates how to build a simple file filter */
	class EditionFilter implements FileFilter {
		private final Set<String> filenames = new HashSet<String>();

		public EditionFilter(VirtualEdition edition) {
			for (VirtualEditionInter inter : edition.getVirtualEditionIntersSet()) {
				filenames.add(inter.getLastUsed().getExternalId() + ".txt");
			}
		}

		/**
		 * Note that {@ref FileIterator} will only call this filter if the file
		 * is not a directory, so we do not need to test that it is a file.
		 */
		@Override
		public boolean accept(File file) {
			return filenames.contains(file.getName());
		}
	}

}
