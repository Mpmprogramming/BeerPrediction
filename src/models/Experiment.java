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
	int topX = 2000;//TODO Replace with train set files
	HashMap<Aspect, SentiWordList> wordlists = new HashMap<Aspect, SentiWordList>();
	String id;
	
	StanfordCoreNLP pipeline;
	
	public Experiment(Properties props, StanfordCoreNLP pipeline) {
		this.props = props;
		this.pipeline = pipeline;
		this.id = "data/experiments/" + System.getProperty("user.name") + "-" + new Date().toString().replace(" ", "-");
	}

	public void run() {
		
		//Setup output folders
		new File(id+"/wordlists/").mkdirs();
		
		Corpus co = new Corpus(props);

		System.out.println("Running experiment with following settings: "+props.toString());

		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);

		WordListGenerator wlGenerator = new WordListGenerator(id+"/wordlists/");

		String[] wlFiles = wlGenerator.generateWordlists(co).split(";");
		this.loadWordlists(wlFiles);
		
		
		Corpus testSet = new Corpus(props);
		testSet.loadFromFile("data/ratebeer.txt", topX);
		for(Review r: testSet.getReviews()) {
			System.out.println(this.getOverallSentiScore(r));
		}
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
