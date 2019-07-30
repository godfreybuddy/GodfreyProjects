package corpus;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.junit.Test;

/**
 * Test case for testing the Text class.
 * 
 * @author Tyree Mitchell
 *
 */
public class TextTest
{

  /**
   * Test methods of the Text class.
   * @throws IOException 
   * @throws BadLocationException 
   */
  @Test
  public void test() throws IOException, BadLocationException
  {
    Corpus corpus = Corpus.getInstance();
    Text testText = null;
    if(corpus.getByShortName("Dagonet") == null)
    {
      corpus.add("resources/Dagonet.txt");
      testText = corpus.getByShortName("Dagonet");
      assertTrue("Is line count incremented", testText.getLineCount() > 0);
      assertTrue("Are words incremented", testText.getWordTokens() > 0);
      testText.setFullName("Test Full Name");
    }
  }
}
