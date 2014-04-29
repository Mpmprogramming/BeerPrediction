/**
 * 
 */
package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import models.Review.Aspect;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;

//import com.cybozu.labs.langdetect.Detector;
//import com.cybozu.labs.langdetect.DetectorFactory;
//import com.cybozu.labs.langdetect.LangDetectException;
//import com.cybozu.labs.langdetect.Language;

/**
 * @author Michi, Marc
 * 
 */
public class Corpus {
	
	private ArrayList<Review> reviews = new ArrayList<Review>();
	private boolean analyzed = false;

	StanfordCoreNLP pipeline;
	Properties props;
	
	
	public Corpus(Properties props) {
		this.props = props;
		pipeline = new StanfordCoreNLP(props);
		
//		try {
//			initLangDetection("profiles");
//		} catch (LangDetectException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public Corpus(Properties props, StanfordCoreNLP pipeline) {
		this.props = props;
		this.pipeline = pipeline;
	}
	
	/**
	 * Does the NLP processing for all reviews in the corpus
	 */
	public void analyze() {
		System.out.println("Analyzing corpus; " + this.reviews.size() + " reviews found ...");
		if (!analyzed) {
			for (Review rev : reviews) {
				rev.analyze(pipeline, props);
			}
			analyzed = true;
		}
	}
	
	public String getTokenConcatenation(Aspect aspect) {
		String result = "";
		for (Review rev : reviews) {
			result += rev.getAnalyzedTokens(aspect);
		}
		return result;
	}
	
	/**
	 * Reads a corpus from a text file
	 * 
	 * @param path the file path
	 * @param topX Maximum number of reviews to keep
	 * @return Number of complete review instances parsed
	 */
	public int loadFromFile(String path, int topX) {
		Review review = null;
		String line = null;
		BufferedReader br = null;
		try {
			System.out.println("Attempt loading file: " + path);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

			while ((line = br.readLine()) != null) {

				if (line.startsWith("beer/name")) {
					String name = line.split(":", 2)[1].trim();
					review = new Review();
					review.setName(name);
				}

				if (line.startsWith("beer/beerId")) {
					String beerId = line.split(":", 2)[1].trim();
					review.setBeerID(beerId);
				}

				if (line.startsWith("beer/brewerId")) {
					String brewerId = line.split(":", 2)[1].trim();
					review.setBrewerID(brewerId);
				}

				// Will be ignored for now:Check if needed
				// Handle missing values
				// if (line.startsWith("beer/ABV")) {
				// String abv = line.split(":", 2)[1].trim();
				// review.setABV(Double.parseDouble(abv));
				// }

				if (line.startsWith("beer/style")) {
					String style = line.split(":", 2)[1].trim();
					review.setStyle(style);
				}

				if (line.startsWith("review/appearance")) {
					String appearance = line.split(":", 2)[1].split("/", 2)[0]
							.trim();
					review.setAppearance(Integer.parseInt(appearance));
				}

				if (line.startsWith("review/aroma")) {
					String aroma = line.split(":", 2)[1].split("/", 2)[0]
							.trim();
					review.setAroma(Integer.parseInt(aroma));
				}

				if (line.startsWith("review/palate")) {
					String palate = line.split(":", 2)[1].split("/", 2)[0]
							.trim();
					review.setPalate(Integer.parseInt(palate));
				}

				if (line.startsWith("review/taste")) {
					String taste = line.split(":", 2)[1].split("/", 2)[0]
							.trim();
					review.setTaste(Integer.parseInt(taste));
				}

				if (line.startsWith("review/overall")) {
					String overall = line.split(":", 2)[1].split("/", 2)[0]
							.trim();
					review.setOverall(Integer.parseInt(overall));
				}

				if (line.startsWith("review/time")) {
					String time = line.split(":", 2)[1].trim();
					review.setTime(Integer.parseInt(time));
				}

				if (line.startsWith("review/profileName")) {
					String profileName = line.split(":", 2)[1].trim();
					review.setProfileName(profileName);
				}

				if (line.startsWith("review/text")) {
					String text = line.split(":", 2)[1].trim();
					review.setText(text);
					
					//TODO: Done!? Filter reviews with missing values
					
				if (review.getText().length() > 1)	{
					
					//TODO:Add inside lang filter when working
					this.reviews.add(review);
					
					
					//TODO: Done!? Filter non-english reviews (Marc)
					
//					try {
//						Detector detector = DetectorFactory.create();
//					       detector.append(review.getText());
//					       if (detectLanguage(review.getText()).equals("en")) {
////					    	   System.out.println("text is "+review.getText());
////					    	   System.out.println("lang is "+detector.detect());
//						
//						 
//						this.reviews.add(review);
//						}
//					       else {
//					    	   //this is very seldom, it happens once after parsing the 55300 review 
//					    	   System.out.println("help! other language");
//					       }
//					} catch (LangDetectException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
					
					if (reviews.size() >= topX ) break;
					
					if (reviews.size() % 1000 == 0 ) System.out.println("Reviews parsed so far: " + this.reviews.size());
				}
			} 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error parsing line: " + line);
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
				System.out.println("Finished reading file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this.reviews.size();
	}

	/**
	 * Write the whole corpus in CSV format to disc
	 * @param file The file path
	 */
	public void writeToCSV(String file) {
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			
			out.write("name,beerId,brewerId,ABV,style,appearance,aroma,palate,taste,overall,time,profileName,text");
			out.write("\r\n");
			
			for (Review rev : this.reviews) {
				out.write(rev.toCSV());
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
	
//	public void initLangDetection(String profileDirectory) throws LangDetectException {
//        DetectorFactory.loadProfile(profileDirectory);
//    }
//    public String detectLanguage(String text) throws LangDetectException {
//        Detector detector = DetectorFactory.create();
//        detector.append(text);
//        return detector.detect();
//    }
    
//    public ArrayList detectLanguages(String text) throws LangDetectException {
//        Detector detector = DetectorFactory.create();
//        detector.append(text);
//        return detector.getProbabilities();
//    }

		//TODO: delete string as attribute, implement threshold as properties

	
	public Corpus getTopReviews(Aspect asp) {
		
		 ArrayList<Review> topReviews = new ArrayList<Review>();
		 Corpus result = new Corpus(this.props, this.pipeline);
		
		if (asp.equals(Aspect.APPEARANCE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getAppearance() > 3) 
				{
					topReviews.add(reviews.get(i));
				}
		}
			result.setReviews(topReviews);
			return result;
		}
		
		if (asp.equals(Aspect.AROMA)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getAroma() >= 7) 
				{
					topReviews.add(reviews.get(i));
				}
		}
			result.setReviews(topReviews);
			return result;
		}
			
		if (asp.equals(Aspect.PALATE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getPalate() > 3) 
				{
					topReviews.add(reviews.get(i));
				}
		}
			result.setReviews(topReviews);
			return result;
		}
		
		if (asp.equals(Aspect.TASTE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getTaste() >= 7) 
				{
					topReviews.add(reviews.get(i));
				}
		}
			result.setReviews(topReviews);
			return result;
		}
		
		if (asp.equals(Aspect.OVERALL)) {	
		 
		 
			for (int i=0; i< reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() >= 15) 
				{
					topReviews.add(reviews.get(i));
					
				}
				
		}
			result.setReviews(topReviews);
			return result;
		}
		
		return null;
	}
	

	
public Corpus getLowReviews(Aspect asp) {
	
		ArrayList<Review> lowReviews = new ArrayList<Review>();
		Corpus result = new Corpus(this.props, this.pipeline);
		
		if (asp.equals(Aspect.APPEARANCE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getAppearance() < 3) 
				{
					lowReviews.add(reviews.get(i));
				}
		}
			result.setReviews(lowReviews);
			return result;
		}
		
		if (asp.equals(Aspect.AROMA)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getAroma() < 5) 
				{
					lowReviews.add(reviews.get(i));
				}
		}
			result.setReviews(lowReviews);
			return result;
		}
			
		if (asp.equals(Aspect.PALATE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getPalate() < 3) 
				{
					lowReviews.add(reviews.get(i));
				}
		}
			result.setReviews(lowReviews);
			return result;
		}
		
		if (asp.equals(Aspect.TASTE)) {	
			 
			for (int i=0; i< reviews.size(); i++) {
				if (reviews.get(i).getTaste() < 5) 
				{
					lowReviews.add(reviews.get(i));
				}
		}
			result.setReviews(lowReviews);
			return result;
		}
		
		if (asp.equals(Aspect.OVERALL)) {	
		 
		 
			for (int i=0; i< reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() <= 8) 
				{
					lowReviews.add(reviews.get(i));
					
				}
				
		}
			result.setReviews(lowReviews);
			return result;
		}
		return null;
	}

	

	
//	public StanfordCoreNLP getPipeline() {
//		return pipeline;
//	}


	public Properties getProps() {
		return props;
	}
	
	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	//TODO:Wtf?
//	public void setWordsForTaste(ArrayList<Review> reviewsArray) {
//		this.reviews = reviewsArray;
//	}


}
