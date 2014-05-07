package pt.ist.socialsoftware.edition.mallet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.FragWord;
import pt.ist.socialsoftware.edition.domain.FragWordInCategory;
import pt.ist.socialsoftware.edition.domain.GeneratedCategory;
import pt.ist.socialsoftware.edition.domain.GeneratedTagInFragInter;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.Taxonomy;
import pt.ist.socialsoftware.edition.utils.PropertiesManager;
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

public class TopicModeler {

	private Pipe pipe;
	private final String corpusPath = PropertiesManager.getProperties()
			.getProperty("corpus.dir");
	private final String corpusEditionsPath = PropertiesManager.getProperties()
			.getProperty("corpus.editions.dir");

	public Taxonomy generate(Edition edition, String name, int numTopics,
			int numWords, int thresholdCategories, int numIterations)
			throws IOException {
		// if a corpus is absent
		File directory = new File(corpusEditionsPath + edition.getExternalId());
		if (!directory.exists()) {
			return null;
		}

		pipe = buildPipe();

		InstanceList instances = readDirectory(new File(corpusEditionsPath
				+ edition.getExternalId()));

		int numInstances = instances.size();

		// there are no fragments
		if (numInstances == 0) {
			return null;
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

		return writeTopicModel(edition, name, model, numTopics, numWords,
				thresholdCategories, numIterations, dataAlphabet, numInstances);
	}

	public Pipe buildPipe() {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern
				.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceLowercase());
		pipeList.add(new TokenSequenceRemoveStopwords(new File(corpusPath
				+ "stoplist-pt.txt"), "UTF-8", false, false, false));
		// pipeList.add(new TokenSequenceRemoveStopwords(false, false));
		pipeList.add(new TokenSequence2FeatureSequence());

		return new SerialPipes(pipeList);
	}

	public InstanceList readDirectory(File directory) {
		return readDirectories(new File[] { directory });
	}

	public InstanceList readDirectories(File[] directories) {

		// Construct a file iterator, starting with the
		// specified directories, and recursing through subdirectories.
		// The second argument specifies a FileFilter to use to select
		// files within a directory.
		// The third argument is a Pattern that is applied to the
		// filename to produce a class label. In this case, I've
		// asked it to use the last directory name in the path.
		FileIterator iterator = new FileIterator(directories, new TxtFilter(),
				FileIterator.LAST_DIRECTORY);

		// Construct a new instance list, passing it the pipe
		// we want to use to process instances.
		InstanceList instances = new InstanceList(pipe);

		// Now process each instance provided by the iterator.
		instances.addThruPipe(iterator);

		return instances;
	}

	@Atomic(mode = TxMode.WRITE)
	private Taxonomy writeTopicModel(Edition edition, String name,
			ParallelTopicModel model, int numTopics, int numWords,
			int thresholdCategories, int numIterations, Alphabet dataAlphabet,
			int numInstances) {

		Taxonomy taxonomy = new Taxonomy(LdoD.getInstance(), edition, name,
				numTopics, numWords, thresholdCategories, numIterations);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		Map<String, FragWord> mapOfWords = new HashMap<String, FragWord>();
		Category[] categories = new Category[numTopics];

		// create a category for each topic
		// counter do avoid duplicate category names
		int counter = 1;
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic)
					.iterator();

			GeneratedCategory category = new GeneratedCategory();
			category.init(taxonomy);
			categories[topic] = category;

			// associate the words for each category
			int rank = 0;
			String wordName = "";
			while (iterator.hasNext() && rank < numWords) {
				IDSorter idCountPair = iterator.next();
				String word = (String) dataAlphabet.lookupObject(idCountPair
						.getID());
				wordName = wordName + "(" + word + ")";

				// do not duplicate FragWords
				FragWord fragWord = mapOfWords.get(word);
				if (fragWord == null) {
					fragWord = new FragWord(taxonomy, word);
					mapOfWords.put(word, fragWord);
				}

				new FragWordInCategory(category, fragWord,
						(int) idCountPair.getWeight());

				rank++;
			}

			category.setName(wordName + "[" + counter++ + "]");
		}

		// associate categories with fragment interpretations
		for (int instance = 0; instance < numInstances; instance++) {

			String fileName = model.getData().get(instance).instance.getName()
					.toString();
			String[] subs = fileName.split("[/\\.]");
			String externalId = subs[subs.length - 2];
			FragInter fragInter = null;
			for (FragInter inter : edition.getIntersSet()) {
				if (inter.getLastUsed().getExternalId().equals(externalId)) {
					fragInter = inter;
					break;
				}
			}

			// Estimate the topic distribution fora each instance,
			// given the current Gibbs state.
			double[] topicDistribution = model.getTopicProbabilities(instance);

			for (int topic = 0; topic < numTopics; topic++) {
				BigDecimal bd = new BigDecimal(topicDistribution[topic]);
				bd = bd.setScale(2, RoundingMode.HALF_UP);
				int percentage = (int) (bd.doubleValue() * 100);
				if (percentage >= thresholdCategories) {
					new GeneratedTagInFragInter().init(fragInter,
							categories[topic], percentage);
				}
			}
		}

		return taxonomy;
	}

	/** This class illustrates how to build a simple file filter */
	class TxtFilter implements FileFilter {

		/**
		 * Test whether the string representation of the file ends with the
		 * correct extension. Note that {@ref FileIterator} will only call this
		 * filter if the file is not a directory, so we do not need to test that
		 * it is a file.
		 */
		@Override
		public boolean accept(File file) {
			return file.toString().endsWith(".txt");
		}
	}

}
