/**
 * 
 */
package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import models.Review.Aspect;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Michi, Marc
 * 
 */
public class Review {

	public enum Aspect {
		OVERALL, AROMA, PALATE, TASTE, APPEARANCE, NONE;
	}

	private String name;
	private String beerID;
	private String brewerID;
	private double ABV; // ABV= Alcohol by volume
	private String style;
	private int appearance;
	private int aroma;
	private int palate;
	private int taste;
	private int overall;
	private int time;
	private String profileName;
	private String text;

	private boolean isAnalyzed = false;

	// Arrays as collections for different aspects of the review
	private static ArrayList<String> wordsForAppearance = new ArrayList<String>();
	private static ArrayList<String> wordsForAroma = new ArrayList<String>();
	private static ArrayList<String> wordsForPalate = new ArrayList<String>();
	private static ArrayList<String> wordsForTaste = new ArrayList<String>();
	private static ArrayList<String> wordsForOverall = new ArrayList<String>();

	static {
		// could be disambiguous: wordsForAppearance.add("appear");
		wordsForAppearance.add("appearance");
		wordsForAppearance.add("color");
		wordsForAppearance.add("colour");
		wordsForAppearance.add("look");
		wordsForAppearance.add("head");

		// wordsForAppearance.add("pour");
		// wordsForAppearance.add("brown");
		// wordsForAppearance.add("red");

		wordsForAroma.add("aroma");

		wordsForPalate.add("palate");

		wordsForTaste.add("taste");
		wordsForTaste.add("flavor");
		wordsForTaste.add("flavour");
		// wordsForTaste.add("sweet");
		// wordsForTaste.add("bitter");

		wordsForOverall.add("overall");
		wordsForOverall.add("general");
	}

	private ArrayList<String> sentences = new ArrayList<String>();
	// NLP analyzed fields
	private String tokens = new String("");
	private HashMap<Aspect, String> analyzedTokens = new HashMap<Aspect, String>();

	public String getAnalyzedTokens(Aspect aspect) {
		return analyzedTokens.get(aspect);
	}

	public int analyze(StanfordCoreNLP pipeline, Properties props) {

		if (isAnalyzed)
			return 0;

		Boolean useLemma = Boolean.parseBoolean(props.getProperty("useLemma"));
		Boolean includePOS = Boolean.parseBoolean(props.getProperty("includePOS"));

		// Initialize to avoid null strings
		for (Aspect a : Aspect.values()) {
			analyzedTokens.put(a, "");
		}

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(this.text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		
		for (CoreMap sentence : sentences) {
			String sentenceText = sentence.get(TextAnnotation.class);
			Aspect sentenceTopic = Review.findAspect(sentenceText);
			this.sentences.add(sentenceText);
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String analyzedToken;
				String text = token.get(TextAnnotation.class);
				analyzedToken = text;
				// this is the text of the token
				if (useLemma)
					analyzedToken = token.get(LemmaAnnotation.class);//TODO:Check here if really difference from text
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				if (includePOS) analyzedToken += pos;

				// System.out.println("Adding: "+analyzedTokens.get(sentenceTopic)+" "+lemma);
				this.tokens += analyzedToken+" ";
				this.analyzedTokens.put(sentenceTopic, analyzedTokens.get(sentenceTopic) + " " + analyzedToken);
				// System.out.println(sentenceTopic.name()
				// +" "+analyzedTokens.get(sentenceTopic));

			}

		}

		isAnalyzed = true;

		return 1;
	}

	public ArrayList<String> getSentences() {
		return sentences;
	}

	//TODO:Maybe handle ambiguous sentences?
	public static Aspect findAspect(String sentence) {

		for (int i = 0; i < getWordsForAppearance().size(); i++) {

			if (sentence.toLowerCase().contains(getWordsForAppearance().get(i))) {

				return Aspect.APPEARANCE;
			}
		}

		for (int i = 0; i < wordsForAroma.size(); i++) {
			if (sentence.toLowerCase().contains(wordsForAroma.get(i))) {

				return Aspect.AROMA;
			}
		}

		for (int i = 0; i < wordsForPalate.size(); i++) {
			if (sentence.toLowerCase().contains(wordsForPalate.get(i))) {

				return Aspect.PALATE;
			}
		}

		for (int i = 0; i < wordsForTaste.size(); i++) {
			if (sentence.toLowerCase().contains(wordsForTaste.get(i))) {

				return Aspect.TASTE;
			}
		}

		for (int i = 0; i < wordsForOverall.size(); i++) {
			if (sentence.toLowerCase().contains(wordsForOverall.get(i))) {

				return Aspect.OVERALL;
			}
		}

		return Aspect.NONE;
	}

	/**
	 * Returns the analyzed tokens as a string concatenation
	 * @return
	 */
	public String getTokens() {
		return tokens;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBeerID() {
		return beerID;
	}

	public void setBeerID(String beerID) {
		this.beerID = beerID;
	}

	public String getBrewerID() {
		return brewerID;
	}

	public void setBrewerID(String brewerID) {
		this.brewerID = brewerID;
	}

	public double getABV() {
		return ABV;
	}

	public void setABV(double aBV) {
		ABV = aBV;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public int getAppearance() {
		return appearance;
	}

	public void setAppearance(int appearance) {
		this.appearance = appearance;
	}

	public int getAroma() {
		return aroma;
	}

	public void setAroma(int aroma) {
		this.aroma = aroma;
	}

	public int getPalate() {
		return palate;
	}

	public void setPalate(int palate) {
		this.palate = palate;
	}

	public int getTaste() {
		return taste;
	}

	public void setTaste(int taste) {
		this.taste = taste;
	}

	public int getOverall() {
		return overall;
	}

	public void setOverall(int overall) {
		this.overall = overall;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Review [name=" + name + ", beerID=" + beerID + ", brewerID=" + brewerID + ", ABV=" + ABV + ", style="
				+ style + ", appearance=" + appearance + ", aroma=" + aroma + ", palate=" + palate + ", taste=" + taste
				+ ", overall=" + overall + ", time=" + time + ", profileName=" + profileName + ", text=" + text + "]";
	}

	public String toCSV() {
		return name + "," + beerID + "," + brewerID + "," + ABV + "," + style + "," + appearance + "," + aroma + ","
				+ palate + "," + taste + "," + overall + "," + time + "," + profileName + ",\""
				+ text.replaceAll("\"", "").replaceAll("[\\t\\n\\r]", " ").trim() + "\"";
	}

	public static ArrayList<String> getWordsForAppearance() {
		return wordsForAppearance;
	}

	public void setWordsForAppearance(ArrayList<String> wordsForApperance) {
		Review.wordsForAppearance = wordsForApperance;
	}

	public ArrayList<String> getWordsForAroma() {
		return wordsForAroma;
	}

	public void setWordsForAroma(ArrayList<String> wordsForAroma) {
		Review.wordsForAroma = wordsForAroma;
	}

	public ArrayList<String> getWordsForPalate() {
		return wordsForPalate;
	}

	public void setWordsForPalate(ArrayList<String> wordsForPalate) {
		Review.wordsForPalate = wordsForPalate;
	}

	public ArrayList<String> getWordsForTaste() {
		return wordsForTaste;
	}

	public void setWordsForTaste(ArrayList<String> wordsForTaste) {
		Review.wordsForTaste = wordsForTaste;
	}

	public ArrayList<String> getWordsForOverall() {
		return wordsForOverall;
	}

	public void setWordsForOverall(ArrayList<String> wordsForOverall) {
		Review.wordsForOverall = wordsForOverall;
	}

}
