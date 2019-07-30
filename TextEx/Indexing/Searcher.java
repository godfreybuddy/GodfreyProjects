package indexing;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Searcher class builds a hit list/search result list, based on any phrase it is given.
 * 
 * @author Tyree Mitchell
 *
 */
public class Searcher
{
  private Indexer indexer;

  // The list of results, set to all results with first search word, then filters out any not
  // containing the rest of the phrase.
  private ArrayList<Posting> initialResults;
  private ArrayList<Posting> possibleResults;

  /**
   * Set indexer to current instance.
   * 
   * @param indexer
   *          - The indexer to search through
   */
  public Searcher(Indexer indexer)
  {
    this.indexer = indexer;
  }

  /**
   * Process the search phrase and generate a hit list/search result list.
   * 
   * @param searchPhrase
   *          - The string to search for.
   */
  public void buildSearchPostings(String searchPhrase)
  {

    String stringToSearch = searchPhrase;
    stringToSearch = searchPhrase.replaceAll("[^A-Za-z0-9\\s\\u0027\\u2019]", " ");
    stringToSearch = stringToSearch.toLowerCase();
    String[] words = stringToSearch.trim().split("\\s+");

    // If the search phrase even exists
    if (indexer.doesTermExist(words[0]))
    {
      // Shallow copy the indexed word so the index doesn't have elements removed
      possibleResults = new ArrayList<Posting>(indexer.getPostingsForWord(words[0]));

      // Initial results that need resetting to be sure
      initialResults = new ArrayList<Posting>(indexer.getPostingsForWord(words[0]));

      for (int i = 1; i < words.length; i++)
      {
        
        for (Posting posting : possibleResults)
        {
          
          for (Posting nextPosting : indexer.getPostingsForWord(words[i]))
          {
            boolean sameText = (posting.getText() == nextPosting.getText());
            boolean correctWordNumber = ((posting.getWordNumber() + i) == (nextPosting
                .getWordNumber()));
            // If the posting is in correct order to be a result
            if ((sameText && correctWordNumber))
            {
              posting.setPotentialResult(true);
              // Set the length of the search phrase for emboldening
              
              posting.setSearchLength(posting.getSearchLength() + 1);
            }
          }
        }
        // Get rid of any results that were invalid.
        clearResults();
      }
    }
    else
    {
      possibleResults = null;
      System.out.println("This search phrase does not exist");
    }
  }

  /**
   * Just clears results of an invalid search type.
   * 
   * @param posting
   */
  private void clearResults()
  {
    Iterator<Posting> iter = getSearchResults().iterator();
    while (iter.hasNext())
    {
      Posting posting = iter.next();
      if (!posting.isPotentialResult())
      {
        iter.remove();
      }
      else
      {
        posting.setPotentialResult(false);
      }
    }
  }

  /**
   * Need to make sure all of our postings go back to a vanilla state.
   */
  public void resetSearch()
  {
    if (possibleResults != null)
    {
      System.out.println("Resetting search");
      for (Posting posting : initialResults)
      {
        posting.setSearchLength(0);
        posting.setPotentialResult(false);
      }
    }
  }

  /**
   * Obtain search results after being processed.
   * 
   * @return - search results
   */
  public ArrayList<Posting> getSearchResults()
  {
    return possibleResults;
  }
}
