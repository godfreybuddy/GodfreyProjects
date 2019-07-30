package indexing;

import corpus.Text;

/**
 * A Posting a triple composed of a text identifier, line and word numbers.
 * 
 * @author Tyree Mitchell
 *
 */
public class Posting
{

  private String term; // The word that this location belongs to.
  private String cleanContext; // The context after non-alphanumeric characters are removed. Used
                               // for comparing to search.
  private String rawContext; // The context before, non-alphanumeric characters are removed. Used
                             // for displaying the context in a hit list.
  private Text text; // The text this posting belongs to.
  private int lineNumber; // Which line in the text the word appears at.
  private int wordNumber; // Where the word is in the text.
  private boolean potentialResult;
  private int searchLength;

  /**
   * Create a new posting.
   * 
   * @param term
   *          - The indexed word.
   * @param text
   *          - The text document this word is in.
   * @param lineNumber
   *          - The line number this word is at.
   * @param wordNumber
   *          - The word number of this word.
   * @param cleanContext
   *          - The cleaned context of the word.
   * @param rawContext
   *          - The raw context of the word.
   */
  public Posting(String term, Text text, int lineNumber, int wordNumber, String cleanContext,
      String rawContext)
  {
    this.term = term;
    this.text = text;
    this.lineNumber = lineNumber;
    this.wordNumber = wordNumber;
    this.cleanContext = cleanContext;
    this.rawContext = rawContext;
    this.potentialResult = false;
    this.searchLength = 0;
  }

  /**
   * Get the indexed word.
   * 
   * @return the indexed word.
   */
  public String getTerm()
  {
    return term;
  }

  /**
   * Get the text this indexed word belongs to.
   * 
   * @return the text word belongs to.
   */
  public Text getText()
  {
    return text;
  }

  /**
   * Get the line number this word is at in its respective text.
   * 
   * @return line number.
   */
  public int getLineNumber()
  {
    return lineNumber;
  }

  /**
   * Get the word number this word appears in its respective text.
   * 
   * @return word number.
   */
  public int getWordNumber()
  {
    return wordNumber;
  }

  /**
   * Obtain the line the word appears in before modification.
   * 
   * @return the before modded line.
   */
  public String getCleanContext()
  {
    return cleanContext;
  }

  /**
   * Set the clean context.
   * 
   * @param cleanContext
   *          - The context to set clean context to.
   */
  public void setCleanContext(String cleanContext)
  {
    this.cleanContext = cleanContext;
  }

  /**
   * Obtain the line the word appears in before modification.
   * 
   * @return the raw line.
   */
  public String getRawContext()
  {
    return rawContext;
  }

  /**
   * Set the raw context.
   * 
   * @param rawContext
   *          - The context to set the raw context to.
   */
  public void setRawContext(String rawContext)
  {
    this.rawContext = rawContext;
  }

  /**
   * When searching, is this postings a potential search result?
   * 
   * @return - search result status
   */
  public boolean isPotentialResult()
  {
    return potentialResult;
  }

  /**
   * Allow searcher to set whether the posting is a search result or not.
   * 
   * @param potentialResult
   *          - Status of it being a result.
   */
  public void setPotentialResult(boolean potentialResult)
  {
    this.potentialResult = potentialResult;
  }

  /**
   * ToString method for use in the StartScreen hitlist.
   */
  @Override
  public String toString()
  {
    String boldContext = "";
    String rawFix = rawContext.trim().replaceAll(" +", " ");
    //String cleanFix = rawContext.replaceAll("-", " ");
    String cleanFix = cleanContext.trim().replaceAll(" +", " ");
    String[] splitContext = rawFix.split("\\s+");
    String[] splitClean = cleanFix.split("\\s+");
    /*String[] splitContext = rawFix.trim().split("\\s+");
    String[] splitClean = cleanFix.trim().split("\\s+");*/
    //String[] splitClean = cleanContext.split("\\s+");
    int termIndex = 0;

    /*if(cleanFix.indexOf(term) != -1)
    {
      termIndex = cleanFix.indexOf(term);
    }*/
    for (int i = 0; i < splitClean.length; i++)
    {
      if (splitClean[i].equals(term))
      {
        
        termIndex = i;
        break;
      }
    }
    
    int lastAppearance = (termIndex + searchLength);
    if (lastAppearance > splitContext.length)
    {
      lastAppearance = splitContext.length;
    }
    
    for (int i = 0; i < splitContext.length; i++)
    {
      if(i == termIndex)
      {
        boldContext += ("<html><b>");
      }
      boldContext += splitContext[i];
      if(i == lastAppearance)
      {
        boldContext += "</b>";
      }
      boldContext += " ";
     
    }
    
    //Calculate whitespace for shortname field
    String htmlShortName = htmlFormat(10, true, text.getShortName());
    //int addSpaces = 10 - text.getShortName().length();
    //for(int i = 0; i < addSpaces; i++)
    //{
      //htmlShortName += "&nbsp;";
    //}
    
    //Calculate whitespace for line numbers
    String htmlLineNumber = htmlFormat(5, false, String.valueOf(lineNumber));
    //int addSpaces = 5 - htmlLineNumber.get
    
    boldContext += "";
    return "<html><i>" + htmlShortName + "</i> " + htmlLineNumber + " " + boldContext;
    /*return new String(String.format("<html><i> %-10s </i> %5d  %s", htmlShortName, htmlLineNumber,
        boldContext));*/
  }
  
  private String htmlFormat(int fieldLength, boolean leftJustify, String stringToForm)
  {
    int numberSpaces = fieldLength - stringToForm.length();
    String returnString = stringToForm;
    String spaceFormat = "";
    for(int i = 0; i < numberSpaces; i++)
    {
      spaceFormat += "&nbsp;";
    }
    if(leftJustify)
    {
      returnString += spaceFormat;
    }
    else
    {
      returnString = spaceFormat + returnString;
    }
    
    return returnString;
  }

  /**
   * Set the length of the search phrase.
   * 
   * @param searchLength
   *          - The phrase length
   */
  public void setSearchLength(int searchLength)
  {
    this.searchLength = searchLength;
  }

  /**
   * Get the length of the search phrase.
   * 
   * @return - The phrase length
   */
  public int getSearchLength()
  {
    return searchLength;
  }
}
