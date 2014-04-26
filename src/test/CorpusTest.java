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
import org.junit.Test;

/**
 * @author Michi, Marc
 *
 */
public class CorpusTest {
	
	Properties props;
	private Corpus co;
	Review rev;
	
	@Before
	public void setUp() throws Exception
    { 
		rev = new Review();
		props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
		this.co = new Corpus(props);
    }

	@Test
	public void testReadFile() {
		assertEquals(co.loadFromFile("data/ratebeer.txt", 100), 100);
		co.writeToCSV("data/output/first100.csv");
		assertTrue(new File("data/output/first100.csv").exists());
	}
	
	@Test
	public void testGetSentences() {
		rev.setText("On tap at the Springfield, PA location. Poured a deep and cloudy orange (almost a copper) color with a small sized off white head. Aromas or oranges and all around citric. Tastes of oranges, light caramel and a very light grapefruit finish. I too would not believe the 80+ IBUs - I found this one to have a very light bitterness with a medium sweetness to it. Light lacing left on the glass.");
		rev.analyze(co.getPipeline(), co.getProps());
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
		rev.analyze(co.getPipeline(), co.getProps());
		System.out.println("The following sentences are analyzed:" + " "+ rev.getSentences());

		
		for (int i=0; i<rev.getSentences().size();i++) {
		System.out.println(rev.findAspect(rev.getSentences().get(i))+ " : " +rev.getSentences().get(i));
		}
		assertTrue((rev.findAspect((rev.getSentences().get(0))).equals(Aspect.NONE)));
		assertTrue((rev.findAspect((rev.getSentences().get(1))).equals(Aspect.APPEARANCE)));
		assertTrue((rev.findAspect((rev.getSentences().get(2))).equals(Aspect.AROMA)));
		
		assertTrue((rev.findAspect((rev.getSentences().get(3))).equals(Aspect.TASTE)));
		assertTrue((rev.findAspect((rev.getSentences().get(4))).equals(Aspect.TASTE)));
		assertTrue((rev.findAspect((rev.getSentences().get(5))).equals(Aspect.NONE)));

	}
	@Test
	public void testGetLowOverallReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		System.out.println(co.getReviews().size() +" reviews were in the sample!");
		co.getLowReviews(Aspect.OVERALL);
		System.out.println("overall: " +co.getReviews().size() + " are low");
		for (int i=9; i<=20; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(22));
}
	
	@Test
	public void testGetLowAppearanceReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getLowReviews(Aspect.APPEARANCE);
		System.out.println("appearance: " +co.getReviews().size() + " are low");
		assertFalse(co.getReviews().contains(3));
		assertFalse(co.getReviews().contains(4));
		assertFalse(co.getReviews().contains(5));
		assertFalse(co.getReviews().contains(6));
}
	@Test
	public void testGetLowAromaReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getLowReviews(Aspect.AROMA);
		System.out.println("aroma: " +co.getReviews().size() + " are low");
		for (int i=5; i<=10; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(13));
}
	
	@Test
	public void testLowTopPalateReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getLowReviews(Aspect.PALATE);
		System.out.println("palate: " +co.getReviews().size() + " are low");
		assertFalse(co.getReviews().contains(3));
		assertFalse(co.getReviews().contains(4));
		assertFalse(co.getReviews().contains(5));
		assertFalse(co.getReviews().contains(6));
}
	@Test
	public void testGetLowTasteReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getLowReviews(Aspect.TASTE);
		System.out.println("taste: " +co.getReviews().size() + " are low");
		for (int i=5; i<=10; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(13));
}
	
	@Test
	public void testGetTopOverallReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		System.out.println(co.getReviews().size() +" reviews were in the sample!");
		co.getTopReviews(Aspect.OVERALL);
		System.out.println("overall: " +co.getReviews().size() + " are top");
		for (int i=0; i<15; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(22));
}
	
	@Test
	public void testGetTopAppearanceReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getTopReviews(Aspect.APPEARANCE);
		System.out.println("appearance: " +co.getReviews().size() + " are top");
		assertFalse(co.getReviews().contains(2));
		assertFalse(co.getReviews().contains(1));
		assertFalse(co.getReviews().contains(5));
}
	@Test
	public void testGetTopAromaReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getTopReviews(Aspect.AROMA);
		System.out.println("aroma: " +co.getReviews().size() + " are top");
		for (int i=0; i<7; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(13));
}
	
	@Test
	public void testGetTopPalateReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getTopReviews(Aspect.PALATE);
		System.out.println("palate: " +co.getReviews().size() + " are top");
		assertFalse(co.getReviews().contains(2));
		assertFalse(co.getReviews().contains(1));
		assertFalse(co.getReviews().contains(5));
}
	@Test
	public void testGetTopTasteReviews() {

		co.loadFromFile("data/ratebeer.txt", 100);
		co.getTopReviews(Aspect.TASTE);
		System.out.println("taste: " +co.getReviews().size() + " are top");
		for (int i=0; i<7; i++) {
			assertFalse(co.getReviews().contains(i));
		}
		assertFalse(co.getReviews().contains(13));
}
}