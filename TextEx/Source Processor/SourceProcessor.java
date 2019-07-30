package sourceProcessor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.rtf.RTFEditorKit;

import corpus.Text;

/**
 * Source Processor for Text.
 * 
 * @author Anthony Thellaeche
 */
public class SourceProcessor
{
  /**
   * Processes different types of files.
   * 
   * @param text | the text to be processed
   * @return the processed text as a string.
   * @throws UnsupportedEncodingException | if encoding is Incorrect
   * @throws FileNotFoundException | if file is not found
   * @throws IOException | if IO is interrupted
   * @throws BadLocationException | if an rtf is unreadable
   */
  public static String process(Text text)
      throws UnsupportedEncodingException, FileNotFoundException, IOException, BadLocationException
  {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(new File(text.getFilePath())), "UTF8"));

    String fileType = text.getFilePath().substring(text.getFilePath().lastIndexOf('.'));

    if (fileType.equals(".txt")) 
    { 
      reader.close();
      return processTXT(text); 
    }
    else if (fileType.equals(".rtf")) return processRTF(text, reader);
    else if (fileType.contains(".htm")) return processHTML(text, reader);
    else return process(text, reader);
  }

  /**
   * Processor for .txt extensions.
   * 
   * @param text | text to be processed
   * @return string with the processed text
   * @throws IOException | if IO is interupted
   */
  private static String processTXT(Text text) throws IOException
  {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(new File(text.getFilePath())), "UTF8"));
    
    // If START is never encountered while parsing for names then the stream and reader are reset.
    if (!gotoStart(text, reader))
    {
      reader.close();
      reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(new File(text.getFilePath())), "UTF8"));
    }

    // Normal processing
    StringBuilder string = new StringBuilder();
    String nextLine;
    while ((nextLine = reader.readLine()) != null)
    {
      if (nextLine.contains("*** END") || nextLine.contains("***END"))
        break;
      string.append(nextLine + '\n');
      text.incrementLineCount();
    }
    reader.close();

    return string.toString();
  }

  /**
   * Processor for .rtf extensions.
   * 
   * @param text | text to be processed
   * @param reader | file reader
   * @return string with the processed text
   * @throws IOException | if IO is interupted
   * @throws BadLocationException | if rtf parsing is impossible
   */
  private static String processRTF(Text text, BufferedReader reader)
      throws IOException, BadLocationException
  {
    // First part, gets rid of all the rtf nonsense.
    RTFEditorKit rtfParser = new RTFEditorKit();
    Document document = rtfParser.createDefaultDocument(); // output
    rtfParser.read(reader, document, 0);
    String cleanedText = document.getText(0, document.getLength());
    reader.close();

    // Second part, does normal processing.
    InputStream cleanedStream = new ByteArrayInputStream(cleanedText.getBytes());
    BufferedReader newReader = new BufferedReader(new InputStreamReader(cleanedStream));

    // If START is never encountered while parsing for names then the stream and reader are reset.
    if (!gotoStart(text, newReader))
    {
      newReader.close();
      cleanedStream = new ByteArrayInputStream(cleanedText.getBytes());
      newReader = new BufferedReader(new InputStreamReader(cleanedStream));
    }
    
    // Normal processing
    StringBuilder string;
    String nextLine;
    string = new StringBuilder();
    while ((nextLine = newReader.readLine()) != null)
    {
      if (nextLine.contains("*** END") || nextLine.contains("***END")) break;
      string.append(nextLine + '\n');
      text.incrementLineCount();
    }
    newReader.close();

    return string.toString();
  }

  /**
   * Processor for .htm* extensions.
   * 
   * @param text | text to be processed
   * @param reader | file reader
   * @return string with the processed text
   * @throws IOException | if IO is interupted
   */
  private static String processHTML(Text text, BufferedReader reader) throws IOException
  {
    // removes all HTML tags
    HTMLParser parser = new HTMLParser();
    ParserDelegator pd = new ParserDelegator();
    pd.parse(reader, parser, true);
    reader.close();
    
    InputStream cleanedStream = new ByteArrayInputStream(parser.getWholeParsedText().getBytes());
    BufferedReader newReader = new BufferedReader(new InputStreamReader(cleanedStream));

    // parses for names
    if (!gotoStart(text, newReader))
    {
      newReader.close();
      cleanedStream = new ByteArrayInputStream(parser.getWholeParsedText().getBytes());
      newReader = new BufferedReader(new InputStreamReader(cleanedStream));
    }

    // normal processing
    StringBuilder string = new StringBuilder();
    String nextLine = new String();
    while ((nextLine = newReader.readLine()) != null)
    {
      if (nextLine.contains("*** END") || nextLine.contains("***END"))
        break;
      string.append(nextLine + '\n');
      text.incrementLineCount();
    }
    newReader.close();
    return string.toString();
  }

  /**
   * Processor for any other extension.
   * 
   * @param text | text to be processed
   * @param reader | file reader
   * @return string with the processed text
   * @throws IOException | if IO is interupted
   */
  private static String process(Text text, BufferedReader reader) throws IOException
  {
    StringBuilder string;
    String nextLine;

    string = new StringBuilder();
    while ((nextLine = reader.readLine()) != null)
    {
      string.append(nextLine + '\n');
      text.incrementLineCount();
    }
    reader.close();

    return string.toString();
  }

  /**
   * Gets the full name and short name for the text for non-rtf files.
   * 
   * @param text | text to grab names from
   * @param reader | file reader
   * @return if name was successfully grabbed
   * @throws IOException | if IO is interupted
   */
  public static boolean getNames(Text text, BufferedReader reader) throws IOException
  {
    boolean success = false;
    String nextLine;

    while ((nextLine = reader.readLine()) != null)
    {
      if (nextLine.contains("*** START OF TH") || nextLine.contains("***START OF TH"))
      {
        success = true;
        break;
      }
      if (nextLine.contains("Title:"))
        text.setFullName(nextLine.substring(nextLine.indexOf(':') + 2, nextLine.length()).trim());
    }

    if (text.getFullName() == null)
    {
      text.setFullName("Unknown");
      text.setShortName("Unknown");
    }
    else
    {
      text.setShortName(text.getFullName().split("\\s+")[0]);
    }

    return success;
  }
  
  public static boolean gotoStart(Text text, BufferedReader reader) throws IOException
  {
    boolean success = false;
    String nextLine;

    while ((nextLine = reader.readLine()) != null)
    {
      if (nextLine.contains("*** START OF TH") || nextLine.contains("***START OF TH"))
      {
        success = true;
        break;
      }
    }
    return success;
  }
}
