/**
 * 
 */
package experiments;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Experiment;

/**
 * @author apfelbaum24
 *
 */
public class RunSingleExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup properties here
		Properties props = new Properties();
		props.put("idfTransform", "true");
		props.put("tfTransform", "true");
		props.put("outputWordCounts", "true");
		props.put("lowerCaseTokens", "true");
		props.put("useStoplist", "true");
		props.put("stopwordsFile", "data/english-stop-words-small.txt");
		props.put("wordsToKeep", "2000");
		props.put("minTermFreq", "3");
		props.put("minTopRatingscore", "0.9");
		props.put("maxLowRatingscore", "0.4");

		// Affects stanford core
		props.put("posToKeep", "NN ADJ");// TODO not implemented yet (Marc) See
											// Review.analyze()
		props.put("useLemma", "true");// TODO:Check if lemma actually working
		props.put("includePOS", "false");// TODO: Will mess up word vector
											// creation
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		Experiment experiment = new Experiment(props, new StanfordCoreNLP(props));
		experiment.run();

	}

}
