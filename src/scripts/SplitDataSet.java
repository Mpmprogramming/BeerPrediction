/**
 * 
 */
package scripts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Corpus;
import models.Review;

/**
 * @author Marc, Michi
 * 
 */
public class SplitDataSet {

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
		new SplitDataSet().splitDataSet("data/ratebeer.txt", Integer.MAX_VALUE);

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
		int numOfTrainings = 0;
		int numOfTests = 0;
		int amountNonEnglishReviews= 0;

		try {
			System.out.println("Attempt loading file: " + path);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					path), "UTF-8"));
			OutputStream outputStreamTraining = new FileOutputStream(
					"data/training.txt");
			Writer writerTraining = new OutputStreamWriter(outputStreamTraining);

			OutputStream outputStreamTest = new FileOutputStream(
					"data/test.txt");
			Writer writerTest = new OutputStreamWriter(outputStreamTest);

			// Boolean writeTraining =true;
			// int countTrainingAttributes = 0;
			// Boolean writeTest = false;
			// int countTestAttributes = 0;
			int tempCount = 1;

			while ((line = br.readLine()) != null) {

				if (tempCount % 3 != 0) {

					if (line.startsWith("beer/name")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("beer/beerId")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("beer/brewerId")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					// Will be ignored for now:Check if needed
					// Handle missing values
					// if (line.startsWith("beer/ABV")) {
					// String abv = line.split(":", 2)[1].trim();
					// review.setABV(Double.parseDouble(abv));
					// }

					if (line.startsWith("beer/style")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/appearance")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/aroma")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/palate")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/taste")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/overall")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/time")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/profileName")) {
						writerTraining.write(line);
						writerTraining.write("\r\n");

					}

					if (line.startsWith("review/text")) {

						if (!detect(line).equals("en")) {
							System.err.println("Non english review filtered");
							amountNonEnglishReviews++;
							continue;
						}

						writerTraining.write(line);
						// TODO: exclude numbers from textual review
						// TODO: filter language
						writerTraining.write("\r\n");
						writerTraining.write("\r\n");
						// writeTraining=false;
						// writeTest=true

						tempCount++;
						numOfReviews++;
						numOfTrainings++;
						System.out.println("Parsed reviews so far: "
								+ numOfReviews);
					}
				}

				// else {
				// writeTraining=false;
				// writeTest=true;
				// // countTrainingAttributes=0;
				// }

				// if (tempCount % 3==0)
				else {

					if (line.startsWith("beer/name")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("beer/beerId")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("beer/brewerId")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					// Will be ignored for now:Check if needed
					// Handle missing values
					// if (line.startsWith("beer/ABV")) {
					// String abv = line.split(":", 2)[1].trim();
					// review.setABV(Double.parseDouble(abv));
					// }

					if (line.startsWith("beer/style")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/appearance")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/aroma")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/palate")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/taste")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/overall")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/time")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/profileName")) {
						writerTest.write(line);
						writerTest.write("\r\n");

					}

					if (line.startsWith("review/text")) {

						if (!detect(line).equals("en")) {
							System.err.println("Non english review filtered");
							amountNonEnglishReviews++;
							continue;
						}

						writerTest.write(line);
						writerTest.write("\r\n");
						writerTest.write("\r\n");
						// writeTraining=true;
						// writeTest=false;
						tempCount++;
						numOfReviews++;
						numOfTests++;

					}
				}

				// else {
				// writeTraining=true;
				// writeTest=false;
				// // countTestAttributes=0;
				// }

				// TODO: Done!? Filter reviews with missing values

				// if (review.getText().length() > 1) {
				//
				// //TODO:Add inside lang filter when working
				// this.reviews.add(review);
				//

				// TODO: Done!? Filter non-english reviews (Marc)

				// try {
				// Detector detector = DetectorFactory.create();
				// detector.append(review.getText());
				// if (detectLanguage(review.getText()).equals("en")) {
				// // System.out.println("text is "+review.getText());
				// // System.out.println("lang is "+detector.detect());
				//
				//
				// this.reviews.add(review);
				// }
				// else {
				// //this is very seldom, it happens once after parsing the
				// 55300 review
				// System.out.println("help! other language");
				// }
				// } catch (LangDetectException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				if (numOfReviews >= topX)

				{
					break;
				}
				//
				// if (reviews.size() % 1000 == 0 )
				// System.out.println("Reviews parsed so far: " +
				// this.reviews.size());

			}

			writerTraining.close();
			writerTest.close();

			System.out.println("Dataset of " + numOfReviews + " Reviews"
					+ " has been split into " + numOfTrainings + " "
					+ "Trainings- and " + numOfTests + " " + "Testitems!");
			System.out.println("Number of non-english reviews:" +amountNonEnglishReviews);

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
