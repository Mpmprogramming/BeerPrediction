/**
 * 
 */
package models;

import java.util.HashMap;
import java.util.Properties;

import models.Review.Aspect;

/**
 * @author Michi
 * 
 */
public class Experiment {

	Properties props;
	int topX = 2000;//TODO Replace with train set files
	HashMap<Aspect, SentiWordList> wordlists = new HashMap<Aspect, SentiWordList>();

	public void run() {
		Corpus co = new Corpus(props);

		System.out.println(props.toString().replaceAll(", ", "\n"));

		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);

		WordListGenerator wlGenerator = new WordListGenerator("data/output/wordlists/");

		String[] wlFiles = wlGenerator.generateWordlists(co).split(";");
		this.loadWordlists(wlFiles);
	}

	private void loadWordlists(String[] wlFiles) {
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

}
