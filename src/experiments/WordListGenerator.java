/**
 * 
 */
package experiments;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import models.Corpus;
import models.Review.Aspect;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
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
	public static int topX = 5000;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Setup properties here
		Properties props = new Properties();
		props.put("posToKeep", "");// TODO not implemented yet
		props.put("idfTransform", "true");
		props.put("tfTransform", "true");
		props.put("outputWordCounts", "false");
		props.put("lowerCaseTokens", "false");
		props.put("useStoplist", "true");
		props.put("stopwordsFile", "data/english-stop-words-small.txt");
		props.put("wordsToKeep", 1000);
		props.put("minTermFreq", 3);
		props.put("useLemma", "true");// Affects stanford core NLP
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		Corpus co = new Corpus(props);
		
		System.out.println(props.toString().replaceAll(", ", "\n"));
		
		long startTime = System.currentTimeMillis();

		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);

		// Set up weka word vector
		FastVector attributes;
		Instances dataSet;
		attributes = new FastVector();

		attributes.addElement(new Attribute("id", (FastVector) null));
		attributes.addElement(new Attribute("tokens", (FastVector) null));

		dataSet = new Instances("BeerAspects", attributes, 0);

		Corpus topReviews;
		Corpus lowReviews;

		// Do top and low for all aspects
		for (Aspect aspect : Aspect.values()) {

			// Only for actual aspects
			if (aspect.equals(Aspect.NONE) || aspect.equals(Aspect.OVERALL))
				continue;

			topReviews = co.getTopReviews(aspect);
			topReviews.analyze();
			String tokens = topReviews.getTokenConcatenation(aspect);

			Instance instance = new Instance(2);
			instance.setValue((Attribute) attributes.elementAt(0), aspect.name() + "_TOP");
			instance.setValue((Attribute) attributes.elementAt(1), tokens);

			dataSet.add(instance);

			lowReviews = co.getLowReviews(aspect);
			lowReviews.analyze();
			tokens = lowReviews.getTokenConcatenation(aspect);
			// System.out.println(tokens);

			instance = new Instance(2);
			instance.setValue((Attribute) attributes.elementAt(0), aspect.name() + "_LOW");
			instance.setValue((Attribute) attributes.elementAt(1), tokens);

			dataSet.add(instance);
		}

		// System.out.println(dataSet.toString());
		Instances dataFiltered = transformToWordVector(dataSet, co.getProps());

//		System.out.println(dataFiltered.toString());
		
		writeArffFile(dataFiltered, "data/output/wordvector.arff");

	      long stopTime = System.currentTimeMillis();
	      long elapsedTime = stopTime - startTime;
	      System.out.println("Finished in: " + elapsedTime/1000 +" s");

	}

	public static Instances transformToWordVector(Instances dataSet, Properties props) {

		Instances dataFiltered = null;

		// Set up config
		Boolean idfTransform = Boolean.parseBoolean(props.getProperty("idfTransform"));
		Boolean tfTransform = Boolean.parseBoolean(props.getProperty("tfTransform"));
		Boolean outputWordCounts = Boolean.parseBoolean(props.getProperty("outputWordCounts"));
		Boolean lowerCaseTokens = Boolean.parseBoolean(props.getProperty("lowerCaseTokens"));

		Boolean useStoplist = Boolean.parseBoolean(props.getProperty("useStoplist"));
		String stopwordsFile = props.getProperty("stopwordsFile");

//		Boolean useStemmer = Boolean.parseBoolean(props.getProperty("useStemmer"));

		int wordsToKeep = Integer.parseInt(props.getProperty("wordsToKeep", "1000"));
		int minTermFreq = Integer.parseInt(props.getProperty("minTermFreq", "3"));

		// //Apply StringToWord filter
		// StringToWordVector filter = new
		// StringToWordVector(Integer.MAX_VALUE);
		StringToWordVector filter = new StringToWordVector(wordsToKeep);
		filter.setIDFTransform(idfTransform);
		filter.setTFTransform(tfTransform);// TODO change to prop key
		filter.setOutputWordCounts(outputWordCounts);

		filter.setMinTermFreq(minTermFreq);
		filter.setLowerCaseTokens(lowerCaseTokens);

		//We use lemmatization of coreNLP instead
//		Stemmer stemmer = new SnowballStemmer();
//		if (useStemmer)
//			filter.setStemmer(stemmer);
		
		if (stopwordsFile != null) {
			filter.setStopwords(new File(stopwordsFile));
			filter.setUseStoplist(useStoplist); // default stopword list
		} else if (useStoplist) System.err.println("Not stopword list provided!");
			

		filter.setAttributeIndices("2");
		try {
			filter.setInputFormat(dataSet);
			dataFiltered = Filter.useFilter(dataSet, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataFiltered;
	}

	public static void writeArffFile(Instances data, String fileName) {
		// Write to file
		try {
			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(data);
			arffSaverInstance.setFile(new File(fileName));
			arffSaverInstance.writeBatch();
			System.out.println("Arff file written to: " + fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
