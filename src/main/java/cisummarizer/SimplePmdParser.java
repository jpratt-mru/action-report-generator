package cisummarizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class SimplePmdParser {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/pmd-problem-collector");

  private List<Problem> problems = new ArrayList<>();
  private List<String> errors = new ArrayList<>();

  public static void main(String[] args) {
    SimplePmdParser parser =
        new SimplePmdParser("pmd", RESOURCE_DIR.resolve("valid-one-error-pmd.xml"));
    System.out.println(parser.errors());
    System.out.println(parser.problems());
  }

  public SimplePmdParser(String parserType, Path pathToPmdResults) {
    Document root = xmlDocumentCreatedFrom(pathToPmdResults);

    if (root == null) {
      return;
    }

    if (!isExpected(root)) {
      errors.add(String.format("not a %s file", parserType));
      return;
    }

    problems = parsedProblems(root);
  }

  public List<Problem> problems() {
    return new ArrayList<>(problems);
  }

  public List<String> errors() {
    return new ArrayList<>(errors);
  }

  private List<Problem> parsedProblems(Document root) {

    return nodesOfInterest(root, locationExpression())
        .map(Node::getTextContent)
        .flatMap(location -> problemsIn(location, root))
        .collect(Collectors.toList());
  }

  private Stream<Problem> problemsIn(String location, Document root) {

    return nodesOfInterest(root, locationToProblemExpression(location))
        .map(Node::getTextContent)
        .map(problemType -> buildProblem(problemType, location));
  }

  private String locationExpression() {
    return "/pmd/file/@name";
  }

  private String locationToProblemExpression(String location) {
    return String.format("/pmd/file[@name='%s']/violation/@rule", location);
  }

  Stream<Node> nodesOfInterest(Document root, String expression) {
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

  private String formattedType(String problemType) {
    return problemType;
  }

  private boolean isExpected(Document doc) {

    XPath xpath = XPathFactory.newInstance().newXPath();
    try {
      return xpath.evaluateExpression("/pmd", doc, XPathNodes.class).size() == 1;
    } catch (XPathExpressionException e) {
      return false;
    }
  }

  private String formattedLocation(String absoluteLocation) {
    String slashConverted = absoluteLocation.replace("\\", "/");
    int indexOfLastSlash = slashConverted.lastIndexOf("/");
    return slashConverted.substring(indexOfLastSlash + 1);
  }

  private Document xmlDocumentCreatedFrom(Path path) {
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document xmlDocument = builder.parse(path.toUri().toString());
      return xmlDocument;
    } catch (ParserConfigurationException | SAXException | IOException e) {
      errors.add(e.toString());
      return null;
    }
  }
}
