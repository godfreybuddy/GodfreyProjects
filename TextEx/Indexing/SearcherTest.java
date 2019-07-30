package indexing;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.junit.Test;

import corpus.Corpus;

/**
 * Tests the searcher class.
 * @author Tyree Mitchell
 *
 */
public class SearcherTest
{

  /**
   * Test to make sure the searcher searches the index.
   * @throws FileNotFoundException - Issue finding file.
   * @throws IOException - Issue reading in file.
   * @throws BadLocationException 
   */
  @Test
  public void test() throws FileNotFoundException, IOException, BadLocationException
  {
    Corpus corpus = Corpus.getInstance();
    Searcher searcher = new Searcher(corpus.getIndexer());
    corpus.add("resources/alice.txt");
    searcher.buildSearchPostings("beginning to get");
    searcher.buildSearchPostings("peeped into the book her");
    assertTrue("Test and make sure it's retrieving results",
        searcher.getSearchResults().size() > 0);
    corpus.removeAll();
    searcher.resetSearch();
  }
}
