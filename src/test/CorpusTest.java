/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import models.Corpus;
import models.Review;
import models.Review.Aspect;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Michi, Marc
 *
 */
public class CorpusTest {
	
	static Properties props;
	private static Corpus co;
	static Review rev;
	static StanfordCoreNLP pipeline;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
    { 
		props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
		co = new Corpus(props);
		pipeline = new StanfordCoreNLP(props);
		assertEquals(co.loadFromFile("data/ratebeer.txt", 100), 100);
    }
	
	@Before
	public void setUp() throws Exception
    { 
		rev = new Review();
    }

	@Test
	public void testReadFile() {
		co.writeToCSV("data/output/first100.csv");
		assertTrue(new File("data/output/first100.csv").exists());
	}
	
	@Test
	public void testGetSentences() {
		rev.setText("On tap at the Springfield, PA location. Poured a deep and cloudy orange (almost a copper) color with a small sized off white head. Aromas or oranges and all around citric. Tastes of oranges, light caramel and a very light grapefruit finish. I too would not believe the 80+ IBUs - I found this one to have a very light bitterness with a medium sweetness to it. Light lacing left on the glass.");
		rev.analyze(pipeline, co.getProps());
		assertEquals(6, rev.getSentences().size());
	}
//	@Test
//	public void testFindAspectsSimpleVersion() {
//
//		System.out.println("findaspect");
//		System.out.println(Review.findAspect("Appearing"));
		
	@Test
	public void testFindAspects() {
		rev.setText("On tap at the Springfield, PA location. Poured a deep and cloudy orange (almost a copper) color with a small sized off white head. Aromas or oranges and all around citric. Tastes of oranges, light caramel and a very light grapefruit finish. I too would not believe the 80+ IBUs - I found this one to have a very light bitterness with a medium sweetness to it. Light lacing left on the glass.");
		rev.analyze(pipeline, co.getProps());
		System.out.println("The following sentences are analyzed:" + " "+ rev.getSentences());

		
		for (int i=0; i<rev.getSentences().size();i++) {
		System.out.println(Review.findAspect(rev.getSentences().get(i))+ " : " +rev.getSentences().get(i));
		}
		assertTrue((Review.findAspect((rev.getSentences().get(0))).equals(Aspect.NONE)));
		assertTrue((Review.findAspect((rev.getSentences().get(1))).equals(Aspect.APPEARANCE)));
		assertTrue((Review.findAspect((rev.getSentences().get(2))).equals(Aspect.AROMA)));
		
		assertTrue((Review.findAspect((rev.getSentences().get(3))).equals(Aspect.TASTE)));
		assertTrue((Review.findAspect((rev.getSentences().get(4))).equals(Aspect.NONE)));
		assertTrue((Review.findAspect((rev.getSentences().get(5))).equals(Aspect.NONE)));

	}
	@Test
	public void testGetLowOverallReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		System.out.println(co.getReviews().size() +" reviews were in the sample!");
		Corpus lowOverall = co.getLowReviews(Aspect.OVERALL);
		System.out.println("overall: " +lowOverall.getReviews().size() + " are low");
		for (int i=0; i<lowOverall.getReviews().size(); i++) {
//			assertTrue(co.getReviews().get(i).getOverall() >=1);
			assertTrue(lowOverall.getReviews().get(i).getOverall() <=8);
			}
}
	
	@Test
	public void testGetLowAppearanceReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus lowApp = co.getLowReviews(Aspect.APPEARANCE);
		System.out.println("appearance: " +lowApp.getReviews().size() + " are high");
		for (int i=0; i<lowApp.getReviews().size(); i++) {
			assertTrue(lowApp.getReviews().get(i).getAppearance() <3);
//			assertTrue(co.getReviews().get(i).getAppearance() >=1);
			}
}
	@Test
	public void testGetLowAromaReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus lowAroma = co.getLowReviews(Aspect.AROMA);
		System.out.println("aroma: " + lowAroma.getReviews().size() + " are low");
		for (int i=0; i<lowAroma.getReviews().size(); i++) {
			assertTrue(lowAroma.getReviews().get(i).getAroma() <= 4);
			}
}
	
	@Test
	public void testGetLowPalateReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus lowPalate = co.getLowReviews(Aspect.PALATE);
		System.out.println("palate: " +lowPalate.getReviews().size() + " are low");
		for (int i=0; i<lowPalate.getReviews().size(); i++) {
			assertTrue(lowPalate.getReviews().get(i).getPalate() < 3);
//			assertTrue(lowPalate.getReviews().get(i).getPalate() >=1);
			}
}
	@Test
	public void testGetLowTasteReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus lowTaste = co.getLowReviews(Aspect.TASTE);
		System.out.println("taste: " +lowTaste.getReviews().size() + " are low");
		for (int i=0; i<lowTaste.getReviews().size(); i++) {
			assertTrue(lowTaste.getReviews().get(i).getTaste() <=4);
//			assertTrue(co.getReviews().get(i).getTaste() >=1);
			}
}
	
	@Test
	public void testGetTopOverallReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		System.out.println(co.getReviews().size() +" reviews were in the sample!");
		Corpus topOverall = co.getTopReviews(Aspect.OVERALL);
		System.out.println("overall: " +topOverall.getReviews().size() + " are top");
		for (int i=0; i<topOverall.getReviews().size(); i++) {
			assertTrue(topOverall.getReviews().get(i).getOverall() >=15);
//			assertTrue(co.getReviews().get(i).getOverall() <=20);
			}
}
	
	@Test
	public void testGetTopAppearanceReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus topApp = co.getTopReviews(Aspect.APPEARANCE);
		System.out.println("appearance: " +topApp.getReviews().size() + " are top");
		for (int i=0; i<topApp.getReviews().size(); i++) {
			assertTrue(topApp.getReviews().get(i).getAppearance() >3);
//			assertTrue(co.getReviews().get(i).getAppearance() <=5);
			}
}
	@Test
	public void testGetTopAromaReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus topAroma = co.getTopReviews(Aspect.AROMA);
		System.out.println("aroma: " +topAroma.getReviews().size() + " are top");
		for (int i=0; i<topAroma.getReviews().size(); i++) {
			assertTrue(topAroma.getReviews().get(i).getAroma() >=7);
//			assertTrue(co.getReviews().get(i).getAroma() <=10);
			}
}
	
	@Test
	public void testGetTopPalateReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
		Corpus topPalate = co.getTopReviews(Aspect.PALATE);
		System.out.println("palate: " +topPalate.getReviews().size() + " are top");
		for (int i=0; i<topPalate.getReviews().size(); i++) {
			assertTrue(topPalate.getReviews().get(i).getPalate() >3);
//			assertTrue(co.getReviews().get(i).getPalate() <=5);
			}
}
	@Test
	public void testGetTopTasteReviews() {

//		co.loadFromFile("data/ratebeer.txt", 100);
	    Corpus topTaste = co.getTopReviews(Aspect.TASTE);
		System.out.println("taste: " +topTaste.getReviews().size() + " are top");
		for (int i=0; i<topTaste.getReviews().size(); i++) {
			assertTrue(topTaste.getReviews().get(i).getTaste() >=7);
//			assertTrue(co.getReviews().get(i).getTaste() <=10);
			}
	}
//	@Test
//	public void testDetectLanguageInGeneral() {
//		try {
//			co.initLangDetection("profiles");
//			Detector detector = DetectorFactory.create();
//		       detector.append("Willkommen in der wunderbaren NLP Welt!");
//		} catch (LangDetectException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//}
}