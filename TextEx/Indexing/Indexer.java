package indexing;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import corpus.Text;

/**
 * Indexes Text objects, and also provides statistics back to the text object.
 * 
 * @author Tyree Mitchell
 */
public class Indexer
{
  // Map of Word to an array of Postings(Each posting is essentially the location of the word)
  private HashMap<String, ArrayList<Posting>> index;

  // WordCount for a Text file
  private int wordTypes = 0;
  private int wordCount = 0;
  
  /**
   * Initializes the Map of words to postings.
   */
  public Indexer()
  {
    index = new HashMap<String, ArrayList<Posting>>();
  }

  /**
   * Goes line by line in the Text and calls Index() to index each line.
   * 
   * @param text
   *          - The object containing the entire clean loaded text.
   * @throws IOException
   *           - Throw an exception in case of an error when reading the Text object.
   */
  public void splitTextAndIndex(Text text) throws IOException
  {
    InputStream inputStream = new ByteArrayInputStream(text.getText().getBytes());
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    int lineNumber = 0;
    wordTypes = 0;
    wordCount = 0;
    String line;
    while ((line = bufferedReader.readLine()) != null)
    {
      indexLine(line, lineNumber, text);
      lineNumber++;
    }
    text.setWordTokens(wordCount);
    text.setWordTypes(wordTypes);
  }

  /**
   * Indexes any string given to it.
   * 
   * @param lineToIndex
   *          - The line of words to index.
   * @param lineNumber
   *          - The line number.
   * @param text
   *          - The object containing the entire clean loaded text.
   */
  public void indexLine(String lineToIndex, int lineNumber, Text text)
  {
    // Clean up the text.
    String cleanText = lineToIndex.toLowerCase();
    cleanText = cleanText.replaceAll("[^A-Za-z0-9\\s\\u0027\\u2019]", " ");

    // Split the words in the line.
    String[] words = cleanText.trim().split("\\s+");

    // Index each word in the words array.
    for (int i = 0; i < words.length; i++)
    {
      // If the word isn't already in the index, create a new Posting tree for the word.
      // Then add location for where the word appears.
      if (!index.containsKey(words[i]))
      {
        index.put(words[i], new ArrayList<Posting>());
        index.get(words[i])
            .add(new Posting(words[i], text, lineNumber, wordCount, cleanText, lineToIndex));
        // New unique word type
        wordTypes++;
      }
      // If word exists in the index, add a new location of where the word appears.
      else
      {
        index.get(words[i])
            .add(new Posting(words[i], text, lineNumber, wordCount, cleanText, lineToIndex));
      }
      wordCount++;
    }
  }

  /**
   * Get the postings for an indexed word.
   * 
   * @param word
   *          - the indexed word to search for
   * @return - The postings that belong to the word param
   */
  public ArrayList<Posting> getPostingsForWord(String word)
  {
    ArrayList<Posting> postings = null;
    if (doesTermExist(word))
    {
      postings = new ArrayList<Posting>(index.get(word));
    }
    return postings;
  }

  /**
   * Checks if a word exists in the index.
   * 
   * @param term
   *          - The term to look for.
   * @return whether the term exists.
   */
  public boolean doesTermExist(String term)
  {
    return index.containsKey(term);
  }
  
  /**
   * Returns the index.
   * @return - The whole index.
   */
  public HashMap<String, ArrayList<Posting>> getIndex()
  {
    return index;
  }
}
