/**
 * 
 */
package test;

import static org.junit.Assert.*;
import models.Corpus;

import org.junit.Test;

/**
 * @author Michi
 *
 */
public class ReadFileTest {

	@Test
	public void test() {
		Corpus co = new Corpus();
		assertEquals(co.loadFromFile("data/first1000lines.txt"), 71);
		co.writeToCSV("data/output/first1000lines.csv");
	}

}
