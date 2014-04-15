/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Properties;

import models.Corpus;
import models.Review;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Michi
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

}
