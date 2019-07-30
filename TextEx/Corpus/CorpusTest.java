package corpus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.junit.Test;

/**
 * Test the methods of the Corpus class.
 * 
 * @author Tyree Mitchell
 *
 */
public class CorpusTest
{

  Corpus testCorpus;

  /**
   * Test methods of the Corpus class as well as some Text features.
   * 
   * @throws FileNotFoundException
   *           - If File isn't found pass exception up
   * @throws IOException
   *           - If file becomes unavailable while processing
   * @throws BadLocationException 
   */
  @Test(expected = IndexOutOfBoundsException.class)
  public void corpusTest() throws FileNotFoundException, IOException, BadLocationException
  {

    // Test if singleton pattern properly instantiates Corpus
    testCorpus = Corpus.getInstance();
    
    assertNotNull("Test whether the corpus instantiates", testCorpus);

    Text testText = null;
    testCorpus.add("resources/Dagonet.txt");
    
    assertTrue("Test if texts are loaded", testCorpus.getNumberOfTexts() > 0);
    testText = testCorpus.get(0);

    assertEquals("Test short name", testText, testCorpus.getByShortName("Dagonet"));
    assertNotNull("Check full name with correct title",
        testCorpus.getByFullName("Dagonet Ditties"));

    // Test word counts
    assertTrue("Test total word types", testCorpus.getTotalWordTypes() > 0);
    assertTrue("Test total word tokens", testCorpus.getTotalWordTokens() > 0);

    assertNotNull("Test and make sure the indexer exists", testCorpus.getIndexer());
    // Test obtaining by filePath
    assertEquals("Test file path getter", testText,
        testCorpus.getByFilePath("resources/Dagonet.txt"));

    testCorpus.add(new Text("resources/Dagonet.txt"));
    testCorpus.removeAll();
    testText = new Text("resources/Dagonet.txt");
    testCorpus.add(testText);
    testCorpus.add("resources/emma.txt");
    assertEquals("Test tostring method", "Dagonet, Emma", testCorpus.toString());
    testCorpus.remove(testText);
    testCorpus.removeAll();
    testCorpus.get(0);
  }
}
