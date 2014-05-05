/**
 * 
 */
package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Review.Aspect;

/**
 * @author Michi
 * 
 */
public class Experiment {

	Properties props;
	HashMap<Aspect, SentiWordList> wordlists = new HashMap<Aspect, SentiWordList>();
	String id;
	Result finalResult;
	StanfordCoreNLP pipeline;
	
	public Experiment(Properties props, StanfordCoreNLP pipeline) {
		this.props = props;
		this.pipeline = pipeline;
		this.id = "data/experiments/" + System.getProperty("user.name") + "-" + new Date().toString().replace(" ", "-").replace(":", "-");
	}

	public void run() throws IOException {
		
		double minTopClassScore = Double.parseDouble(props.getProperty("minTopClassScore"));
		double minSentimentTopScore = Double.parseDouble(props.getProperty("minSentimentTopScore"));
		int maxLoad = Integer.parseInt(props.getProperty("maxLoad"));
		
		//Setup output folders
		new File(id+"/wordlists/").mkdirs();
		
		Writer errorWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.id+"/errors.txt"), "UTF-8"));
		
		Corpus co = new Corpus(props, this.pipeline);
		//Evaluation scores
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		int butCounter = 0;

		System.out.println("Running experiment with following settings: "+props.toString());
		long startTime = System.currentTimeMillis();

		int size = co.loadFromFile("data/training.txt", maxLoad);
		System.out.println("Total amount of reviews loaded: " + size);

		WordListGenerator wlGenerator = new WordListGenerator(id+"/wordlists/");

		//TODO: Possible wl improvements: Filter out: numbers as tokens and aspect key words, make sure lemmatization works
		String[] wlFiles = wlGenerator.generateWordlists(co).split(";");
		this.loadWordlists(wlFiles);
		
		
		Corpus testSet = new Corpus(props, this.pipeline);
		testSet.loadFromFile("data/test.txt", maxLoad);
		testSet.analyze();
		
		for(Review r: testSet.getReviews()) {
			double overallSentiScore = this.getOverallSentiScore(r); 
			boolean predictTopOverall = overallSentiScore >= minSentimentTopScore;
			boolean isTopOverall = r.isTop(Aspect.OVERALL, minTopClassScore);
			if (predictTopOverall && isTopOverall) tp++;
			if (!predictTopOverall && !isTopOverall) tn++;
			if (!predictTopOverall && isTopOverall) {
				fn++;
				errorWriter.write(overallSentiScore +" => predictingtop? "+predictTopOverall+" - isTop? " +isTopOverall+"\n");
				errorWriter.write(r.toString()+"\n");
			}
			if (predictTopOverall && !isTopOverall) {
				fp++;
				errorWriter.write(overallSentiScore +" => predictingtop? "+predictTopOverall+" - isTop? " +isTopOverall+"\n");
				errorWriter.write(r.toString()+"\n");
				if (r.getText().contains("but")) butCounter++;
			}
		}
		
		errorWriter.close();
		this.finalResult = new Result(tp, tn, fn, fp);
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Finished experiment in " + elapsedTime / 1000 + " s");
		System.out.println("Config: "+props);
		
		
		
//		System.out.println("True positives: "+tp);
//		System.out.println("True negatives: "+tn);
//		System.out.println("False negatives: "+fn);
//		System.out.println("False positives: "+fp);
//		System.out.println("Accuracy: "+(tp+tn)/totalInstances);
//		System.out.println("Accuracy: "+result.getAccuracy());
		System.out.println(finalResult);
		System.out.println("BUT-% in FP instances: "+(double)butCounter/fp);
	}

	public void loadWordlists(String[] wlFiles) {
		for (String file : wlFiles) {
			if (file.contains(Aspect.APPEARANCE.name()))
				wordlists.put(Aspect.APPEARANCE, new SentiWordList().loadFromFile(file));
			if (file.contains(Aspect.TASTE.name()))
				wordlists.put(Aspect.TASTE, new SentiWordList().loadFromFile(file));
			if (file.contains(Aspect.AROMA.name()))
				wordlists.put(Aspect.AROMA, new SentiWordList().loadFromFile(file));
			if (file.contains(Aspect.PALATE.name()))
				wordlists.put(Aspect.PALATE, new SentiWordList().loadFromFile(file));
		}
	}
	
	public double getOverallSentiScore(Review review) {
		double score = 0.0;
		int sentWordsCount = 0;
		review.analyze(pipeline, props);
		String[] tokens = review.getTokens().split(" ");
		for(String token: tokens) {
			double tmp = score;
			 score += wordlists.get(Aspect.APPEARANCE).getScore(token);
			 score += wordlists.get(Aspect.TASTE).getScore(token);
			 score += wordlists.get(Aspect.AROMA).getScore(token);
			 score += wordlists.get(Aspect.PALATE).getScore(token);
			 if (tmp != score) sentWordsCount++;
		}
		return score/sentWordsCount;//TODO:Currently dividing by senti words count; Other weightening possible
	}

}
