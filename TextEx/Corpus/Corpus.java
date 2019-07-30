package corpus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import indexing.Indexer;

/**
 * Holds Text files.
 * 
 * @author Anthony Thellaeche
 */
public class Corpus
{

  // rn i made it work like a singleton class since there should only be one ever(?) i guess.
  // if you want to use it you need to call Corpus.getInstance().<insertwhateveryouwanttodo>
  // this prevents from making multiple instances of Corpus.
  // also i added a bunch of add/get/remove methods because idk which one's we're realisticly
  // gonna use yet so i just added a bunch of them that i thought we may use.

  private static Corpus instance;
  private ArrayList<Text> textList;
  private Indexer indexer;

  /**
   * Corpus is only allowed to create itself. Initialize ArrayList of Text objects.
   */
  private Corpus()
  {
    textList = new ArrayList<Text>();
    indexer = new Indexer();
  }

  /**
   * If instance of the Corpus doesn't exist make one, otherwise pass the existing.
   * 
   * @return - instance of corpus.
   */
  public static Corpus getInstance()
  {
    if (instance == null)
      instance = new Corpus();
    return instance;
  }

  /**
   * Add a Text object to list of Texts.
   * 
   * @param text
   *          - The text object to add.
   */
  public void add(Text text)
  {
    try
    {
      indexer.splitTextAndIndex(text);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    textList.add(text);
  }

  /**
   * Add a Text object to list of Texts through a file path.
   * 
   * @param fileLocation
   *          - The path of the file.
   * @throws BadLocationException 
   * @throws IOException 
   * @throws FileNotFoundException 
   * @throws UnsupportedEncodingException 
   * @throws Exception 
   */
  public void add(String fileLocation) throws UnsupportedEncodingException, FileNotFoundException, IOException, BadLocationException
  {
    Text text = new Text(fileLocation);
    // TODO handle the IOException better in catch
    try
    {
      indexer.splitTextAndIndex(text);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    textList.add(text);
  }

  /**
   * Return a text given an index.
   * 
   * @param index
   *          - Index of Text to return.
   * @return - Text at index.
   */
  public Text get(int index)
  {
    return textList.get(index);
  }

  /**
   * Obtains a Text given the file path of the Text object.
   * 
   * @param filepath
   *          - The filepath of the Text you are looking for.
   * @return - Text with the given file path.
   */
  public Text getByFilePath(String filepath)
  {
    for (Text text : textList)
      if (text.getFilePath().equals(filepath))
        return text;
    return null;
  }

  /**
   * Obtains a Text given the full name of the Text object.
   * 
   * @param fullName
   *          - The full name of the Text you'd like to look for.
   * @return - Text with the given full name.
   */
  public Text getByFullName(String fullName)
  {
    for (Text text : textList)
      if (text.getFullName().equals(fullName))
        return text;
    return null;
  }

  /**
   * Obtains a Text given the short name of the Text object.
   * 
   * @param shortName
   *          - The short name of the Text you'd like to look for.
   * @return - Text with the given short name.
   */
  public Text getByShortName(String shortName)
  {
    for (Text text : textList)
      if (text.getShortName().equals(shortName))
        return text;
    return null;
  }

  /**
   * Return the number of texts.
   * 
   * @return - The number of Texts.
   */
  public int getNumberOfTexts()
  {
    return textList.size();
  }

  /**
   * The Text object to remove from list of Texts.
   * 
   * @param text
   *          - The text to remove.
   */
  public void remove(Text text)
  {
    textList.remove(text);
  }

  /**
   * Clear all texts from the list of Texts.
   */
  public void removeAll()
  {
    textList.clear();
  }

  /**
   * Count all the word types across texts.
   * 
   * @return total word types in all texts
   */
  public int getTotalWordTypes()
  {
    int returnTotal = 0;
    for (Text text : textList)
    {
      returnTotal += text.getWordTypes();
    }

    return returnTotal;
  }

  /**
   * Count all the word tokens across texts.
   * 
   * @return total word tokens in all texts
   */
  public int getTotalWordTokens()
  {
    int returnTotal = 0;
    for (Text text : textList)
    {
      returnTotal += text.getWordTokens();
    }

    return returnTotal;
  }

  /**
   * Obtain the indexer.
   * 
   * @return - the indexer of the Corpus
   */
  public Indexer getIndexer()
  {
    return indexer;
  }
  
  @Override
  public String toString()
  {
    String returnString = textList.get(0).getShortName();
    for(int i =1; i < textList.size(); i++)
    {
      returnString += ", " + textList.get(i).getShortName();
    }
    
    return returnString;
  }

}
