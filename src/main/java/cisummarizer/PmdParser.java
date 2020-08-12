package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.util.IteratorIterable;
import org.xml.sax.SAXException;

public class PmdParser implements SummarizingParser {

  @Override
  public SummaryResult parse(Path path) {
    Document document = documentFrom(path);
    return document == null ? new SummaryErrorResult() : resultParsedFrom(document);
  }

  private Document documentFrom(Path path) {
    Document document = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      org.w3c.dom.Document w3cdoc = builder.parse(Files.newInputStream(path));
      document = new DOMBuilder().build(w3cdoc);
    } catch (SAXException | ParserConfigurationException | IOException e) {
      System.err.println(e);
    }
    return document;
  }

  private SummaryResult resultParsedFrom(Document document) {
    Element rootNode = document.getRootElement();
    return validPmdRoot(rootNode) ? pmdSummaryResultFrom(rootNode) : new SummaryErrorResult();
  }

  private boolean validPmdRoot(Element rootNode) {
    return rootNode.getName().equals("pmd");
  }

  private SummaryResult pmdSummaryResultFrom(Element rootNode) {
    SummaryResult result = new PmdSummaryResult();

    addProblemsToResult(rootNode, result);

    return result;
  }

  private void addProblemsToResult(Element rootNode, SummaryResult result) {
    IteratorIterable<Element> allElements = rootNode.getDescendants(Filters.element());
    allElements.forEach(
        e -> {
          if (isProblem(e)) {
            result.addProblem(e.getAttributeValue("rule"));
          }
        });
  }

  private boolean isProblem(Element e) {
    return e.getName().equals("violation");
  }
}
