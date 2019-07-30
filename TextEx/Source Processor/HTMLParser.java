package sourceProcessor;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Parses HTML documents.
 * 
 * @author Anthony
 */
public class HTMLParser extends HTMLEditorKit.ParserCallback
{
  private StringBuilder string;
  private int newLine;

  /**
   * creates a stringBuilder, which will contain all the post-processed text.
   * sets newline counter to 0.
   * when newline counter reaches 80 newlines are added.
   */
  public HTMLParser()
  {
    string = new StringBuilder();
    newLine = 0;
  }

  /**
   * Parses the start tags in the text.
   * adds 2 newlines in the presence of p, br, and h1-6 tags.
   * 
   * @param t | start tags
   * @param a | mutable attributes (ex. src, height)
   * @param pos | current position
   */
  public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
  {
    if (t.equals(HTML.Tag.P) || t.equals(HTML.Tag.BR) || t.equals(HTML.Tag.H1)
        || t.equals(HTML.Tag.H2) || t.equals(HTML.Tag.H3) || t.equals(HTML.Tag.H4)
        || t.equals(HTML.Tag.H5) || t.equals(HTML.Tag.H6))
    {
      string.append("\n\n");
      newLine = 0;
    }
  }

  /**
   * Parses the end tags in the text.
   * 
   * @param t | end tags
   * @param a | mutable attributes (ex. src, height)
   * @param pos | current position
   */
  public void handleEndtag(HTML.Tag t, MutableAttributeSet a, int pos)
  {
    if (t.equals(HTML.Tag.P) || t.equals(HTML.Tag.BR) || t.equals(HTML.Tag.H1)
        || t.equals(HTML.Tag.H2) || t.equals(HTML.Tag.H3) || t.equals(HTML.Tag.H4)
        || t.equals(HTML.Tag.H5) || t.equals(HTML.Tag.H6))
    {
      newLine = 0;
    }
  }

  /**
   * Parses the actual text portion of the text.
   * 
   * @param data | stream of characters
   * @param pos | current position
   */
  public void handleText(char[] data, int pos)
  {
    for (char c : data)
    {
      if(c == '\n') newLine = 0;
      string.append(c);
      handleNewLine(c);
    }
  }

  /**
   * Appends newline if appropriate.
   * 
   * @param c | newLine if space is present to prevent breaking words
   */
  public void handleNewLine(char c)
  {
    if (newLine < 80) newLine++;
    else
    {
      if(c == ' ')
      {
        string.append('\n');
        newLine = 0;
      }
    }
  }

  /**
   * Returns the whole parsed text.
   * 
   * @return the whole parsed text
   */
  public String getWholeParsedText()
  {
    return string.toString();
  }
}
