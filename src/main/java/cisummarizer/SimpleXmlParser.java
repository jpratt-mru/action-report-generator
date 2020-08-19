package cisummarizer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathNodes;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class SimpleXmlParser implements Parser {

  private List<Problem> problems;
  private List<String> errors;
  private Document root;

  public SimpleXmlParser(String parserType, Path pathToResults) {

    this.problems = null;
    errors = new ArrayList<>();
    root = xmlDocumentCreatedFrom(pathToResults);

    if (root == null) {
      return;
    }

    if (!validRoot()) {
      errors.add(String.format("not a %s file", parserType));
    }
  }

  @Override
  public List<Problem> problems() {
    if (problems == null) {
      problems = parsedProblems();
    }
    return problems;
  }

  @Override
  public List<String> errors() {
    return errors;
  }

  private boolean validRoot() {

    return nodesOfInterest(rootExpression()).anyMatch(e -> true);
  }

  abstract String rootExpression();

  abstract String locationExpression();

  abstract String locationToProblemExpression(String location);

  protected String formattedType(String problemType) {
    return problemType;
  }

  protected String formattedLocation(String absoluteLocation) {
    String slashConverted = absoluteLocation.replace("\\", "/");
    int indexOfLastSlash = slashConverted.lastIndexOf("/");
    return slashConverted.substring(indexOfLastSlash + 1);
  }

  private List<Problem> parsedProblems() {

    return nodesOfInterest(locationExpression())
        .map(Node::getTextContent)
        .flatMap(this::problemsIn)
        .distinct()
        .collect(Collectors.toList());
  }

  private Stream<Problem> problemsIn(String location) {
    return nodesOfInterest(locationToProblemExpression(location))
        .map(Node::getTextContent)
        .map(problemType -> buildProblem(problemType, location));
  }

  Stream<Node> nodesOfInterest(String expression) {

    XPath xpath = XPathFactory.newInstance().newXPath();

    try {
      return StreamSupport.stream(
          xpath.evaluateExpression(expression, root, XPathNodes.class).spliterator(), false);
    } catch (XPathExpressionException e) {
      errors.add(e.toString());
      return Stream.empty();
    }
  }

  private Problem buildProblem(String type, String location) {

    return new Problem(formattedType(type), formattedLocation(location));
  }

  private Document xmlDocumentCreatedFrom(Path path) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return builder.parse(path.toUri().toString());
    } catch (ParserConfigurationException | SAXException | IOException e) {
      errors.add(e.toString());
      return null;
    }
  }
}
