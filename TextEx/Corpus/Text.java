package corpus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.text.BadLocationException;

import sourceProcessor.SourceProcessor;

/**
 * A Text and all it's properties.
 * 
 * @author Anthony Thellaeche
 */
public class Text
{
  private String text;
  private String filePath;
  private String fullName;
  private String shortName;
  private int lineCount;
  private int wordTokens; // Occurrence of words
  private int wordTypes; // Unique words

  /**
   * Gets the text from the specified file path (if not null). Searches for the title before the
   * start of the book.
   * 
   * @param filePath
   *          | file location.
   * @throws BadLocationException 
   * @throws IOException 
   * @throws FileNotFoundException 
   * @throws UnsupportedEncodingException 
   * @throws Exception 
   */
  public Text(String filePath) throws UnsupportedEncodingException, FileNotFoundException, IOException, BadLocationException
  {
    wordTokens = 0;
    wordTypes = 0;
    lineCount = 0;
    this.filePath = filePath;
    getNames();
  }

  private void getNames() throws IOException
  {
    //shortName and fullname are determined here
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(new File(filePath)), "UTF8"));
    SourceProcessor.getNames(this, reader);
    reader.close();
  }
  
  public void processText() throws UnsupportedEncodingException, FileNotFoundException, IOException, BadLocationException
  {
    this.text = SourceProcessor.process(this);
  }
  
  
  /**
   * Return the actual text of the Text object(the reading material).
   * 
   * @return - String of all text inside Text.
   */
  public String getText()
  {
    return text;
  }

  /**
   * Get the file path for this text object.
   * 
   * @return - The file path for this object.
   */
  public String getFilePath()
  {
    return filePath;
  }

  /**
   * Get the full name/title of this text.
   * 
   * @return - The full name/title of this Text.
   */
  public String getFullName()
  {
    return fullName;
  }
  
  /**
   * Set full name.
   * 
   * @param fullName
   *          used to set this text's short name.
   */
  public void setFullName(String fullName)
  {
    this.fullName = fullName;
  }

  /**
   * Get the short name/title of this text.
   * 
   * @return - The short name/title of this Text.
   */
  public String getShortName()
  {
    return shortName;
  }
  
  /**
   * Set short name.
   * 
   * @param shortName
   *          used to set this text's short name.
   */
  public void setShortName(String shortName)
  {
    this.shortName = shortName;
  }

  /**
   * increment line count by 1.
   */
  public void incrementLineCount()
  {
    lineCount++;
  }
  
  /**
   * Get the number of lines in the text.
   * 
   * @return - Get the number of lines in the text.
   */
  public int getLineCount()
  {
    return lineCount;
  }

  /**
   * Get number of word tokens.
   * 
   * @return number of word tokens
   */
  public int getWordTokens()
  {
    return wordTokens;
  }

  /**
   * Set word tokens.
   * 
   * @param wordTokens
   *          used to set this.wordTokens
   */
  public void setWordTokens(int wordTokens)
  {
    this.wordTokens = wordTokens;
  }

  /**
   * Get number of word types.
   * 
   * @return number of word types
   */
  public int getWordTypes()
  {
    return wordTypes;
  }

  /**
   * Set word types.
   * 
   * @param wordTypes
   *          used to set this.wordTypes
   */
  public void setWordTypes(int wordTypes)
  {
    this.wordTypes = wordTypes;
  }
}
