/**
 * 
 */
package models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import models.Review.Aspect;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stemmers.Stemmer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * @author Michi
 * Generates wordlists and underlying word vector as files on disk
 * 
 */
public class WordListGenerator {

	
	public WordListGenerator(String outputDir) {
		this.outputDir = outputDir;
	}
	
	private String outputDir;

	/**
	 * Generates aspect based word lists from the given corpus
	 * @param co
	 * @return A String containing the path to all wordlists separated by ";"
	 */
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public String generateWordlists(Corpus co) {
		long startTime = System.currentTimeMillis();
		// Set up weka word vector
		FastVector attributes;
		Instances dataSet;
		attributes = new FastVector();

		attributes.addElement(new Attribute("aspect_id", (FastVector) null));
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

			Instance instance = new SparseInstance(2);
			instance.setValue((Attribute) attributes.elementAt(0), aspect.name() + "_TOP");
			instance.setValue((Attribute) attributes.elementAt(1), tokens);

			dataSet.add(instance);

			lowReviews = co.getLowReviews(aspect);
			lowReviews.analyze();
			tokens = lowReviews.getTokenConcatenation(aspect);
			// System.out.println(tokens);

			instance = new SparseInstance(2);
			instance.setValue((Attribute) attributes.elementAt(0), aspect.name() + "_LOW");
			instance.setValue((Attribute) attributes.elementAt(1), tokens);

			dataSet.add(instance);
		}

		// System.out.println(dataSet.toString());
		Instances dataFiltered = transformToWordVector(dataSet, co.getProps());

		// System.out.println(dataFiltered.toString());
		String pathsToLists = writeWordlists(dataFiltered);

		writeArffFile(dataFiltered, this.outputDir+"wordvector.arff");

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Generated wordlists in: " + elapsedTime / 1000 + " s");
		return pathsToLists;
	}

	private Instances transformToWordVector(Instances dataSet, Properties props) {

		Instances dataFiltered = null;

		// Set up config
		Boolean idfTransform = Boolean.parseBoolean(props.getProperty("idfTransform"));
		Boolean tfTransform = Boolean.parseBoolean(props.getProperty("tfTransform"));
		Boolean outputWordCounts = Boolean.parseBoolean(props.getProperty("outputWordCounts"));
		Boolean lowerCaseTokens = Boolean.parseBoolean(props.getProperty("lowerCaseTokens"));

		Boolean useStoplist = Boolean.parseBoolean(props.getProperty("useStoplist"));
		String stopwordsFile = props.getProperty("stopwordsFile");

		// Boolean useStemmer =
		// Boolean.parseBoolean(props.getProperty("useStemmer"));

		int wordsToKeep = Integer.parseInt(props.getProperty("wordsToKeep"));
		int minTermFreq = Integer.parseInt(props.getProperty("minTermFreq"));

		// //Apply StringToWord filter
		// StringToWordVector filter = new
		// StringToWordVector(Integer.MAX_VALUE);
		StringToWordVector filter = new StringToWordVector(wordsToKeep);
		filter.setIDFTransform(idfTransform);
		filter.setTFTransform(tfTransform);
		filter.setOutputWordCounts(outputWordCounts);

		filter.setMinTermFreq(minTermFreq);
		filter.setLowerCaseTokens(lowerCaseTokens);

		// We use lemmatization of coreNLP instead
		// Stemmer stemmer = new SnowballStemmer();
		// if (useStemmer)
		// filter.setStemmer(stemmer);

		if (stopwordsFile != null) {
			filter.setStopwords(new File(stopwordsFile));
			filter.setUseStoplist(useStoplist); // default stopword list
		} else if (useStoplist)
			System.err.println("Not stopword list provided!");

		filter.setAttributeIndices("2");
		try {
			filter.setInputFormat(dataSet);
			dataFiltered = Filter.useFilter(dataSet, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataFiltered;
	}

	public void writeArffFile(Instances data, String fileName) {
		// Write to file
		try {
			ArffSaver arffSaverInstance = new ArffSaver();
			arffSaverInstance.setInstances(data);
			arffSaverInstance.setFile(new File(fileName));
			arffSaverInstance.writeBatch();
			System.out.println("Arff file written to: " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes one word list for each aspect
	 * 
	 * @param data
	 * @return A String containing the path to all wordlists seperated by ";"
	 */
	public String writeWordlists(Instances data) {
		String result = "";
		String[] wordLists = new String[4];
		for (int j = 0; j < wordLists.length; j++) {
			wordLists[j] = "";
		}

		// Loop over all tokens (attributes)
		for (int i = 1; i < data.numAttributes(); i++) {
			double[] scores = data.attributeToDoubleArray(i);
			double[] aspectScores = new double[4];
			String token = data.attribute(i).name();
//			System.out.println("Token: " + token);

			// Calculate aspect score by summing top and low scores
			aspectScores[0] = scores[0] + scores[1]; // Aroma
			aspectScores[1] = scores[2] + scores[3]; // Palate
			aspectScores[2] = scores[4] + scores[5]; // Taste
			aspectScores[3] = scores[6] + scores[7]; // Appearance

			int maxIndex = 0;
			double maxScore = 0;
			for (int j = 0; j < aspectScores.length; j++) {
				if (aspectScores[j] > maxScore) {
					maxScore = aspectScores[j];
					maxIndex = j;
					// TODO Implement min threshold to include
				}
			}
//			System.out.println("Max value and aspect: " + maxIndex + "/" + maxScore);

			Double ratio = scores[maxIndex * 2] / scores[maxIndex * 2 + 1];
//			System.out.println("Score ratio: " + ratio);

			// Word is positive wrt aspect
			if (ratio > 1) // TODO: Configurable threshold
				wordLists[maxIndex] += token + " " + (scores[maxIndex * 2]-scores[maxIndex * 2 + 1]) + "\n";
			// Word is negative wrt aspect
			else if (ratio < 1)
				wordLists[maxIndex] += token + " " + -1 * (scores[maxIndex * 2 + 1]-scores[maxIndex * 2]) + "\n";

//			for (int j = 0; j < scores.length; j++) {
//				System.out.print(scores[j] + ",");
//			}
//			System.out.println();

		}// End loop over attributes

		// Write lists to file
		Writer out = null;
		try {
			for (int j = 0; j < wordLists.length; j++) {

				String path = this.outputDir;
				if (j == 0)
					path += Aspect.AROMA.name();
				if (j == 1)
					path += Aspect.PALATE.name();
				if (j == 2)
					path += Aspect.TASTE.name();
				if (j == 3)
					path += Aspect.APPEARANCE.name();
				path += ".txt";
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
				out.write(wordLists[j]);
				out.close();
				System.out.println("Wordlist written to: " + path + " (" + wordLists[j].split("\n").length + ")");
				result += path+";";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
