/**
 * 
 */
package models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author Michi
 *
 */
public class SentiWordList {
	
	private HashMap<String, Double> wordlist;
	
	public SentiWordList() {
		this.wordlist = new HashMap<String, Double>();
	}
	
	
	public SentiWordList loadFromFile(String path) {
		String line = null;
		BufferedReader br = null;
		try {
			System.out.println("Attempt loading wordlist: " + path);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

			while ((line = br.readLine()) != null) {
				String[] splits = line.split(" ");
				wordlist.put(splits[0].trim(), Double.parseDouble(splits[1]));
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
				System.out.println("Finished loading wordlist.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return this;
	}
	
	public double getScore(String word) {
		if (wordlist.get(word) != null) {
			return wordlist.get(word);
		} else return 0.0;
	}
	
	public int size() {
		return this.wordlist.size();
	}


	@Override
	public String toString() {
		return "SentiWordlist [wordlist=" + wordlist + "]";
	}
	
	

}
