package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.util.IteratorIterable;
import org.xml.sax.SAXException;

public abstract class XmlProblemCollector implements ProblemCollector {

  protected Path path;
  protected List<String> problems;
  protected Document document;
  protected final String validRootName;
  protected final String errorElementName;
  protected final String errorAttributeValue;

  public XmlProblemCollector(
      Path path, String validRootName, String errorElementName, String errorAttributeValue) {
    this.path = path;
    this.validRootName = validRootName;
    this.errorElementName = errorElementName;
    this.errorAttributeValue = errorAttributeValue;
    document = documentFromPath();
    problems = problemsFromDocument();
  }

  @Override
  public List<String> problems() {
    return problems;
  }

  @Override
  public boolean hasProblems() {
    return !problems.isEmpty();
  }

  @Override
  public boolean hasError() {
    return !validRootPresent();
  }

  /**
   * Sometimes the problem in the xml is a bit hard to read. This method lets us address that.
   *
   * @param rawProblem - the problem "as-is" in the xml
   * @return formatted version of the problem
   */
  protected String formattedProblem(String rawProblem) {
    return rawProblem;
  }

  private Document documentFromPath() {
    Document document = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      org.w3c.dom.Document w3cdoc = builder.parse(Files.newInputStream(path));
      document = new DOMBuilder().build(w3cdoc);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      System.err.println("something went south with " + path);
      System.err.println("   here is the exception: " + e);
    }
    return document;
  }

  private List<String> problemsFromDocument() {
    // eliminate dupes
    Set<String> problems = new HashSet<>();

    if (validRootPresent()) {
      IteratorIterable<Element> allElements =
          document.getRootElement().getDescendants(Filters.element());

      allElements.forEach(
          e -> {
            if (isProblem(e)) {
              problems.add(formattedProblem(e.getAttributeValue(errorAttributeValue)));
            }
          });
    }

    // want sorted problems
    List<String> uniqueProblems = new ArrayList<>(problems);
    uniqueProblems.sort(String::compareToIgnoreCase);
    return uniqueProblems;
  }

  private boolean validRootPresent() {
    return document != null && document.getRootElement().getName().equals(validRootName);
  }

  private boolean isProblem(Element e) {
    return e.getName().equals(errorElementName);
  }
}
