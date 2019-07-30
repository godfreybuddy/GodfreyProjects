package indexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.junit.Test;

import corpus.Text;

/**
 * Test case for Indexer.
 * 
 * @author Tyree Mitchell
 *
 */
public class IndexerTest
{

  /**
   * Test the methods of the Indexer.
   * 
   * @throws FileNotFoundException
   * @throws IOException
   *           if file becomes unavailable while processing
   * @throws FileNotFoundException
   *           throw an exception if file isn't found
   * @throws BadLocationException 
   */
  @Test
  public void test() throws FileNotFoundException, IOException, BadLocationException
  {
    Text wholeText = new Text("resources/alice.txt");
    Indexer indexer = new Indexer();
    try
    {
      indexer.splitTextAndIndex(wholeText);
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertTrue(indexer.doesTermExist("beginning"));
    assertNotNull("Make sure index is instantiated", indexer.getIndex());

    Searcher searcher = new Searcher(indexer);
    searcher.buildSearchPostings("beginning to get");
    assertEquals("Test term", "beginning", searcher.getSearchResults().get(0).getTerm());
    assertEquals("Test if posting exists",
        "Alice was beginning to get very tired of sitting by her sister on the",
        searcher.getSearchResults().get(0).getRawContext());
    assertTrue("Make sure attributes work", searcher.getSearchResults().get(0).getLineNumber() > 0);

    System.out.println(searcher.getSearchResults().get(0).toString());
  }

}
