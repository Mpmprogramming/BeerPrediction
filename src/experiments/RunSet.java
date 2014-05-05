/**
 * 
 */
package experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import models.Experiment;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Michi
 *
 */
public class RunSet {
	
	public static Properties generateMasterProperties() {
		// Setup standard properties here
		Properties masterProps = new Properties();

		//TODO:Adjust for final experiments
		masterProps.put("maxLoad", "100000");

		// Affects stanford core
		masterProps.put("posToKeep", "NN ADJ");// TODO not implemented yet (Marc) See Review.analyze()
		masterProps.put("useLemma", "true");// TODO:Check if lemma actually working
		masterProps.put("includePOS", "false");// TODO: Will mess up word vector
		masterProps.put("annotators", "tokenize, ssplit, pos, lemma");


		//Wordlist creation parameters
		masterProps.put("idfTransform", "true");
		masterProps.put("tfTransform", "true");
		masterProps.put("outputWordCounts", "true");
		masterProps.put("lowerCaseTokens", "true");
		masterProps.put("useStoplist", "true");
		masterProps.put("stopwordsFile", "data/english-stop-words-small.txt");
		masterProps.put("wordsToKeep", "5000");
		masterProps.put("minTermFreq", "5");

		masterProps.put("minTopRatingscore", "1.0");//actual aspect ration / MAX(Aspect)
		masterProps.put("maxLowRatingscore", "0.3");


		//Evaluation parameters
		masterProps.put("minTopClassScore", "0.8");//[0-1]actual aspect ration / MAX(Aspect); correlates with: fp++ fn--
		masterProps.put("minSentimentTopScore", "0.0001");//Correlates with: fn++ fp--
		
		
		return masterProps;

	}
	
	public static ArrayList<Properties> generatesProperties() {
		ArrayList<Properties> configs = new ArrayList<Properties>();
		configs.add(generateMasterProperties());
		//TODO fill array list
		return configs;
	}
	
	
	public static void main(String[] args) {
		
		ArrayList<Properties> configs = generatesProperties();
		
		for (int i = 0; i < configs.size(); i++) {
			Experiment experiment = new Experiment(configs.get(i), new StanfordCoreNLP(configs.get(i)));
			try {
				experiment.run();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
