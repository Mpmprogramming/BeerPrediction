/**
 * 
 */
package models;

import java.io.File;
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
	int topX = 5000;//TODO Replace with train set files
	HashMap<Aspect, SentiWordList> wordlists = new HashMap<Aspect, SentiWordList>();
	String id;
	
	StanfordCoreNLP pipeline;
	
	public Experiment(Properties props, StanfordCoreNLP pipeline) {
		this.props = props;
		this.pipeline = pipeline;
		this.id = "data/experiments/" + System.getProperty("user.name") + "-" + new Date().toString().replace(" ", "-").replace(":", "-");
	}

	public void run() {
		
		double minTopClassScore = Double.parseDouble(props.getProperty("minTopClassScore"));
		double minSentimentTopScore = Double.parseDouble(props.getProperty("minSentimentTopScore"));
		
		//Setup output folders
		new File(id+"/wordlists/").mkdirs();
		
		Corpus co = new Corpus(props);
		//Evaluation scores
		double totalInstances = 0.0;
		int tp = 0;
		int tn = 0;
		int fp = 0;
		int fn = 0;
		int butCounter = 0;

		System.out.println("Running experiment with following settings: "+props.toString());

		int size = co.loadFromFile("data/training.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);

		WordListGenerator wlGenerator = new WordListGenerator(id+"/wordlists/");

		String[] wlFiles = wlGenerator.generateWordlists(co).split(";");
		this.loadWordlists(wlFiles);
		
		
		Corpus testSet = new Corpus(props);
		testSet.loadFromFile("data/test.txt", topX);
		
		for(Review r: testSet.getReviews()) {
			double overallSentiScore = this.getOverallSentiScore(r); //TODO:Maybe in relation to word count in review?
			boolean predictTopOverall = overallSentiScore >= minSentimentTopScore;
			boolean isTopOverall = r.isTop(Aspect.OVERALL, minTopClassScore);
			totalInstances++;
			if (predictTopOverall && isTopOverall) tp++;
			if (!predictTopOverall && !isTopOverall) tn++;
			if (!predictTopOverall && isTopOverall) {
				fn++;
				System.out.println(overallSentiScore +" => predictingtop? "+predictTopOverall+" - isTop? " +isTopOverall);
				System.out.println(r.toString());
			}
			if (predictTopOverall && !isTopOverall) {
				fp++;
				System.out.println(overallSentiScore +" => predictingtop? "+predictTopOverall+" - isTop? " +isTopOverall);
				System.out.println(r.toString());
				if (r.getText().contains("but")) butCounter++;
			}
		}
		
		System.out.println("True positives: "+tp);
		System.out.println("True negatives: "+tn);
		System.out.println("False negatives: "+fn);
		System.out.println("False positives: "+fp);
		System.out.println("Accuracy: "+(tp+tn)/totalInstances);
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
		review.analyze(pipeline, props);
		String[] tokens = review.getTokens().split(" ");
		for(String token: tokens) {
			 score += wordlists.get(Aspect.APPEARANCE).getScore(token);
			 score += wordlists.get(Aspect.TASTE).getScore(token);
			 score += wordlists.get(Aspect.AROMA).getScore(token);
			 score += wordlists.get(Aspect.PALATE).getScore(token);
		}
		return score;
	}

}
