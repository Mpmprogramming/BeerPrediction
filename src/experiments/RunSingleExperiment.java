/**
 * 
 */
package experiments;

import java.io.IOException;
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
		
		//TODO:Adjust for final experiments
		props.put("maxLoad", "10000");
		
		// Affects stanford core
//		props.put("posToKeep", "NN ADJ");
		props.put("posToKeep", "NN JJ");// TODO not implemented yet (Marc) See Review.analyze()
		props.put("useLemma", "true");// TODO:Check if lemma actually working
		props.put("includePOS", "false");// TODO: Will mess up word vector
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		
		//Wordlist creation parameters
		props.put("idfTransform", "true");
		props.put("tfTransform", "true");
		props.put("outputWordCounts", "true");
		props.put("lowerCaseTokens", "true");
		props.put("useStoplist", "true");
		props.put("stopwordsFile", "data/english-stop-words-small.txt");
		props.put("wordsToKeep", "5000");
		props.put("minTermFreq", "5");
		
		props.put("minTopRatingscore", "1.0");//actual aspect ration / MAX(Aspect)
		props.put("maxLowRatingscore", "0.3");
		
		
		//Evaluation parameters
		props.put("minTopClassScore", "0.71");//[0-1]actual aspect ration / MAX(Aspect); correlates with: fp++ fn--
		props.put("minSentimentTopScore", "2.1");//Correlates with: fn++ fp--
		
		Experiment experiment = new Experiment(props, new StanfordCoreNLP(props));
		try {
			experiment.run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
