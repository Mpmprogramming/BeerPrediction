/**
 * 
 */
package scripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import models.Review;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Michi
 * 
 */
public class SplitDataSetInto3 {

	static Properties props;
	static Review reviews;
	static StanfordCoreNLP pipeline;

	// private static Corpus tempCorp;

	/**
	 * @param args
	 * @throws LangDetectException
	 */

	public static void main(String[] args) throws LangDetectException {

		// tempCorp.getReviews();
		DetectorFactory.loadProfile("profiles");
		new SplitDataSetInto3().splitDataSet("data/ratebeer.txt", Integer.MAX_VALUE);
		// new SplitDataSet().splitDataSet("data/ratebeer.txt", 10000);

	}

	public String detect(String text) {
		try {
			Detector detector = DetectorFactory.create();
			detector.append(text);
			return detector.detect();
		} catch (LangDetectException e) {
			e.printStackTrace();
		}
		return "";
	}

	public int splitDataSet(String path, int topX) {
		// Review review = null;
		String line = null;
		BufferedReader br = null;
		int numOfReviews = 0;
		int amountNonEnglishReviews = 0;
		
		try {
			System.out.println("Attempt loading file: " + path);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

			BufferedWriter[] out = new BufferedWriter[3];
			int[] counter = new int[3];

			out[0] = new BufferedWriter(new FileWriter("data/split1.txt"));
			out[1] = new BufferedWriter(new FileWriter("data/split2.txt"));
			out[2] = new BufferedWriter(new FileWriter("data/split3.txt"));

			while ((line = br.readLine()) != null) {

				if (line.startsWith("review/text")) {

					if (!detect(line).equals("en")) {
						System.err.println("Non english review filtered");
						amountNonEnglishReviews++;
						continue;
					}
					
					out[numOfReviews % 3].write(line);
					out[numOfReviews % 3].newLine();
					counter[numOfReviews % 3]++;
					numOfReviews++;
					System.out.println("Parsed reviews so far: " + numOfReviews);
				}

				else if (line.startsWith("beer") || line.startsWith("review")) {
					out[numOfReviews % 3].write(line);
					out[numOfReviews % 3].newLine();
				}

				if (numOfReviews >= topX) break;

			}

			out[0].close();
			out[1].close();
			out[2].close();

			System.out.println("Dataset of " + numOfReviews + " Reviews" + " has been split into parts: " + counter[0] + " - " + counter[1] 
					+ " - " +counter[1]);
			System.out.println("Number of non-english reviews:" + amountNonEnglishReviews + " percentage of non-english reviews: "
					+ ((double) amountNonEnglishReviews) / numOfReviews);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error parsing line: " + line);
			e.printStackTrace();
		} finally {
			try {
				br.close();
				System.out.println("Finished reading file.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return numOfReviews;
	}

}
