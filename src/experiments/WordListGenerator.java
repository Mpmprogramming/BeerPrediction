/**
 * 
 */
package experiments;

import java.io.File;
import java.util.Properties;

import models.Corpus;
import models.Review.Aspect;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Michi
 * 
 */
public class WordListGenerator {

	/**
	 * Maximum number of reviews to load
	 */
	public static int topX = 100;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		// Setup properties here
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		Corpus co = new Corpus(props);

		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);


		// Set up weka word vector
		FastVector attributes;
		Instances dataSet;
		attributes = new FastVector();

		attributes.addElement(new Attribute("id", (FastVector) null));
		attributes.addElement(new Attribute("tokens", (FastVector) null));

		dataSet = new Instances("BeerAspects", attributes, 0);

		// Set up config
		Boolean idfTransform = Boolean.parseBoolean(props.getProperty("idfTransform"));
		Boolean tfTransform = Boolean.parseBoolean(props.getProperty("tfTransform"));
		Boolean outputWordCounts = Boolean.parseBoolean(props.getProperty("outputWordCounts"));
		Boolean lowerCaseTokens = Boolean.parseBoolean(props.getProperty("lowerCaseTokens"));

		Boolean useStoplist = Boolean.parseBoolean(props.getProperty("useStoplist"));
		String stopwordsFile = props.getProperty("stopwordsFile");

		Boolean useStemmer = Boolean.parseBoolean(props.getProperty("useStemmer"));

		int wordsToKeep = Integer.parseInt(props.getProperty("wordsToKeep", "1000"));
		
		Corpus topTaste = co.getTopReviews(Aspect.TASTE);
		topTaste.analyze();
		String tokens = topTaste.getTokenConcatenation(Aspect.TASTE);

		Instance instance = new Instance(2);
		instance.setValue((Attribute) attributes.elementAt(0), "TASTE_TOP");
		instance.setValue((Attribute) attributes.elementAt(1), tokens);

		dataSet.add(instance);
		
		Corpus lowTaste = co.getLowReviews(Aspect.TASTE);
		lowTaste.analyze();
		tokens = lowTaste.getTokenConcatenation(Aspect.TASTE);
		System.out.println(tokens);

		instance = new Instance(2);
		instance.setValue((Attribute) attributes.elementAt(0), "TASTE_LOW");
		instance.setValue((Attribute) attributes.elementAt(1), tokens);
		
		dataSet.add(instance);
		// TODO:Add all other aspects
//		System.out.println(dataSet.toString());

		// //Apply StringToWord filter
		// StringToWordVector filter = new
		// StringToWordVector(Integer.MAX_VALUE);
		StringToWordVector filter = new StringToWordVector(1000);
		filter.setIDFTransform(true);
		filter.setTFTransform(false);//TODO change to prop key
		filter.setOutputWordCounts(true);

//		filter.setMinTermFreq(2);
		filter.setLowerCaseTokens(lowerCaseTokens);

		//Stemmer stemmer = new SnowballStemmer();
		if (useStemmer)
//			filter.setStemmer(stemmer);// TODO: stemmer!!!
		if (stopwordsFile != null)
			filter.setStopwords(new File(stopwordsFile));
		filter.setUseStoplist(useStoplist); // default stopword list

		filter.setAttributeIndices("2");
		filter.setInputFormat(dataSet);
		Instances dataFiltered = Filter.useFilter(dataSet, filter);
		String result = dataFiltered.toString();
		
		System.out.println(result);

	}

}
