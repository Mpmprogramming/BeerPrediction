/**
 * 
 */
package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

import models.Review.Aspect;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Michi, Marc
 * 
 */
public class Corpus {
	
	private ArrayList<Review> reviews = new ArrayList<Review>();
	StanfordCoreNLP pipeline;
	Properties props;
	
	
	public Corpus(Properties props) {
		this.props = props;
		pipeline = new StanfordCoreNLP(props);
	}
	
	public Corpus getTopReviews(Aspect asp, String whichAspect, int minThreshold) {
		//TODO: delete string as attribute, implement threshold as properties
		
		
		if (whichAspect.toLowerCase() == "overall") {	
		
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() <= minThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "palate") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getPalate() <= minThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "taste") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getTaste() <= minThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "aroma") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAroma() <= minThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if 	(whichAspect.toLowerCase() == "appearance") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAppearance() <= minThreshold) 
				{
					reviews.remove(i);
				}
		}
			
			return new Corpus(this.props);
		}
		
		return new Corpus(this.props);
	}
	
	public Corpus getTopReviews(Aspect asp, String whichAspect) {
		//TODO: delete string as attribute
		
		
	
		
		if (whichAspect.toLowerCase() == "overall") {	
		
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() <= 16) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "palate") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getPalate() <= 3) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "taste") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getTaste() <= 7) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "aroma") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAroma() <= 7) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if 	(whichAspect.toLowerCase() == "appearance") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAppearance() <= 3) 
				{
					reviews.remove(i);
				}
		}
			
			return new Corpus(this.props);
		}
		
		return new Corpus(this.props);
	}
	

	public Corpus getLowReviews(Aspect asp, String whichAspect, int maxThreshold) {
		//TODO: delete string as attribute, implement threshold as properties
		
		
	
		
		if (whichAspect.toLowerCase() == "overall") {	
		
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() >= maxThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "palate") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getPalate() >= maxThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "taste") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getTaste() >= maxThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "aroma") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAroma() >= maxThreshold) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if 	(whichAspect.toLowerCase() == "appearance") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAppearance() >= maxThreshold) 
				{
					reviews.remove(i);
				}
		}
			
			return new Corpus(this.props);
		}
		
		return new Corpus(this.props);
	}
	
	public Corpus getLowReviews(Aspect asp, String whichAspect) {
		//TODO: delete string as attribute
		
		
	
		
		if (whichAspect.toLowerCase() == "overall") {	
		
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getOverall() >= 16) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "palate") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getPalate() >= 3) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "taste") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getTaste() >= 7) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if (whichAspect.toLowerCase() == "aroma") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAroma() >= 7) 
				{
					reviews.remove(i);
				}
		}
			return new Corpus(this.props);
		}
		
		if 	(whichAspect.toLowerCase() == "appearance") {	
			
			for (int i=0; i> reviews.size(); i++) {
			
				if (reviews.get(i).getAppearance() >= 3) 
				{
					reviews.remove(i);
				}
		}
			
			return new Corpus(this.props);
		}
		
		return new Corpus(this.props);
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

					//TODO:Filter non-english reviews (Marc)
					//if getLanguage != en, continue
					//TODO:Filter reviews with missing values 
					this.reviews.add(review);
					if (reviews.size() >= topX ) break;
					
					if (reviews.size() % 100 == 0 ) System.out.println("Reviews parsed so far: " + this.reviews.size());
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

	
	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}


	public Properties getProps() {
		return props;
	}

}
