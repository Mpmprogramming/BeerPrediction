/**
 * 
 */
package experiments;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import models.Experiment;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Michi
 *
 */
public class RunSet {
	
	public static ArrayList<Experiment> finishedExperiments = new ArrayList<Experiment>();
	
	public static Properties generateMasterProperties() {
		// Setup standard properties here
		Properties masterProps = new Properties();

		//TODO:Adjust for final experiments
		masterProps.put("maxLoad", "100000");
		
		// Affects stanford core

		masterProps.put("posToKeep", "NN JJ RB");//TODO: DONE: Fix this to nn jj adv!
		masterProps.put("useLemma", "true");//Try!
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
		masterProps.put("minTermFreq", "10");
		
		//For optimization focus on thresholds
		masterProps.put("minTopRatingscore", "1.0");//actual aspect ration / MAX(Aspect)
		masterProps.put("maxLowRatingscore", "0.4");
		
		
		//Evaluation parameters
		masterProps.put("minTopClassScore", "0.71");//[0-1]actual aspect ration / MAX(Aspect); correlates with: fp++ fn--
		masterProps.put("minSentimentTopScore", "0.3");//Correlates with: fn++ fp--
		
		
		return masterProps;

	}
	
	public static ArrayList<Properties> generatesProperties() {
		ArrayList<Properties> configs = new ArrayList<Properties>();
		
		//Add standard config
		configs.add(generateMasterProperties());
		
//		Properties change1 = generateMasterProperties();
//		change1.put("minSentimentTopScore", "0.001");
//		configs.add(change1);
//		
//		Properties change2 = generateMasterProperties();
//		change2.put("maxLowRatingscore", "0.3");
//		configs.add(change2);
		
		//TODO fill array list with more configs
		return configs;
	}
	
	
	public static void main(String[] args) {
		
		ArrayList<Properties> configs = generatesProperties();
		StanfordCoreNLP pipeline = new StanfordCoreNLP(generateMasterProperties());
		
		for (int i = 0; i < configs.size(); i++) {
			Experiment experiment = new Experiment(configs.get(i), pipeline);
			try {
				experiment.run();
				finishedExperiments.add(experiment);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		writeResultSummary();
		
	}
	
	public static void writeResultSummary() {
		//TODO write csv and console output
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/experiments/summary_"+ new Date().toString().replace(" ", "-").replace(":", "-")+".csv"), "UTF-8"));
			
//			out.write("name,beerId,brewerId,ABV,style,appearance,aroma,palate,taste,overall,time,profileName,text");
//			out.write("\r\n");
			
			for (Experiment exp : finishedExperiments) {
				out.write(exp.toCSV());
				out.write("\r\n");
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
