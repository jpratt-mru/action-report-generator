package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filter;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.util.IteratorIterable;
import org.xml.sax.SAXException;

public class PmdParser implements SummarizingParser {

  @Override
  public SummaryResult parse(Path path) {

    Document document = null;
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(false);
      DocumentBuilder builder = factory.newDocumentBuilder();
      org.w3c.dom.Document w3cdoc = builder.parse(Files.newInputStream(path));
      document = new DOMBuilder().build(w3cdoc);

    } catch (SAXException | ParserConfigurationException | IOException e) {
      System.err.println(e);
      return new SummaryErrorResult();
    }

    Element rootNode = document.getRootElement();
    if (!rootNode.getName().equals("pmd")) {
      return new SummaryErrorResult();
    }

    return resultFrom(rootNode);
  }

  private SummaryResult resultFrom(Element rootNode) {
    SummaryResult result = new PmdSummaryResult();

    Filter<Element> problemFilter = Filters.element();
    IteratorIterable<Element> results = rootNode.getDescendants(problemFilter);
    for (Element e : results) {
      if (e.getName().equals("violation")) {
        result.addProblem(e.getAttributeValue("rule"));
      }
    }

    return result;
  }
}
