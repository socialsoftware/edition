package pt.ist.socialsoftware.edition.mallet;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.regex.Pattern;

import pt.ist.socialsoftware.edition.domain.Edition;
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
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.LabelSequence;

public class TopicModeler {

	private Pipe pipe;
	private String results = "";

	public String generate(Edition edition) throws IOException {
		pipe = buildPipe();

		String path = PropertiesManager.getProperties().getProperty(
				"mallet.dir");

		InstanceList instances = readDirectory(new File(path
				+ edition.getExternalId()));

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		// Note that the first parameter is passed as the sum over topics, while
		// the second is the parameter for a single dimension of the Dirichlet
		// prior.
		int numTopics = 100;
		ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and
		// combine
		// statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only,
		// for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(50);
		model.estimate();

		// Show the words and topics in the first instance

		// The data alphabet maps word IDs to strings
		Alphabet dataAlphabet = instances.getDataAlphabet();

		FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance
				.getData();
		LabelSequence topics = model.getData().get(0).topicSequence;

		// out
		System.out
				.println(model.getData().get(0).instance.getName().toString());

		Formatter out = new Formatter(new StringBuilder(), Locale.US);
		for (int position = 0; position < tokens.getLength(); position++) {
			out.format("%s-%d ", dataAlphabet.lookupObject(tokens
					.getIndexAtPosition(position)), topics
					.getIndexAtPosition(position));
		}
		System.out.println(out);

		// Estimate the topic distribution of the first instance,
		// given the current Gibbs state.
		double[] topicDistribution = model.getTopicProbabilities(200);

		// Get an array of sorted sets of word ID/count pairs
		ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

		// Show top 5 words in topics with proportions for the first document
		for (int topic = 0; topic < numTopics; topic++) {
			Iterator<IDSorter> iterator = topicSortedWords.get(topic)
					.iterator();

			out = new Formatter(new StringBuilder(), Locale.US);
			out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
			int rank = 0;
			while (iterator.hasNext() && rank < 7) {
				IDSorter idCountPair = iterator.next();
				out.format("%s (%.0f) ",
						dataAlphabet.lookupObject(idCountPair.getID()),
						idCountPair.getWeight());
				rank++;
			}
			System.out.println(out);
			results = results + out.toString() + "<br>";
		}

		// Create a new instance with high probability of topic 0
		StringBuilder topicZeroText = new StringBuilder();
		Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

		int rank = 0;
		while (iterator.hasNext() && rank < 5) {
			IDSorter idCountPair = iterator.next();
			topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID())
					+ " ");
			rank++;
		}

		// Create a new instance named "test instance" with empty target and
		// source fields.
		InstanceList testing = new InstanceList(instances.getPipe());
		testing.addThruPipe(new Instance(topicZeroText.toString(), null,
				"test instance", null));

		TopicInferencer inferencer = model.getInferencer();
		double[] testProbabilities = inferencer.getSampledDistribution(
				testing.get(0), 10, 1, 5);
		System.out.println("0\t" + testProbabilities[0]);
		results = results + testProbabilities[0];

		return results;
	}

	public Pipe buildPipe() {
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new Input2CharSequence("UTF-8"));
		pipeList.add(new CharSequence2TokenSequence(Pattern
				.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
		pipeList.add(new TokenSequenceLowercase());
		// // TODO: a stop list file will need to be included here
		// pipeList.add( new TokenSequenceRemoveStopwords(new
		// File("stoplists/en.txt"), "UTF-8", false, false, false) );
		pipeList.add(new TokenSequenceRemoveStopwords(false, false));
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
