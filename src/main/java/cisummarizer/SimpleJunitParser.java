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

public class SimpleJunitParser {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/junit-xml-examples");

  private List<Problem> problems = new ArrayList<>();
  private List<String> errors = new ArrayList<>();
  private Document root;

  public static void main(String[] args) {
    SimpleJunitParser parser =
        new SimpleJunitParser(RESOURCE_DIR.resolve("TEST-junit-jupiter.xml"));
    System.out.println(parser.errors());
    System.out.println(parser.problems());
  }

  public SimpleJunitParser(Path pathToJunitResults) {
    root = xmlDocumentCreatedFrom(pathToJunitResults);

    if (root == null) {
      return;
    }

    if (!isJunit(root)) {
      errors.add("not a junit file");
      return;
    }

    parseProblems();
  }

  public List<Problem> problems() {
    return new ArrayList<>(problems);
  }

  public List<String> errors() {
    return new ArrayList<>(errors);
  }

  private void parseProblems() {

    List<String> locationsWithTestFailures = testFailureLocationsFrom(root);
    List<Problem> failureProblems =
        locationsWithTestFailures
            .stream()
            .map(this::failureProblemsIn)
            .collect(ArrayList::new, List::addAll, List::addAll);
    List<String> locationsWithTestErrors = testErrorLocationsFrom(root);
    List<Problem> errorProblems =
        locationsWithTestErrors
            .stream()
            .map(this::errorProblemsIn)
            .collect(ArrayList::new, List::addAll, List::addAll);

    problems =
        Stream.concat(failureProblems.stream(), errorProblems.stream())
            .collect(Collectors.toList());
  }

  private List<String> testFailureLocationsFrom(Document doc) {

    List<String> locations = new ArrayList<>();
    XPath xpath = XPathFactory.newInstance().newXPath();

    try {
      XPathNodes fileNameNodes =
          xpath.evaluateExpression(
              "/testsuite/testcase[failure]/@classname", doc, XPathNodes.class);
      StreamSupport.stream(fileNameNodes.spliterator(), false)
          .map(Node::getNodeValue)
          .forEach(locations::add);
    } catch (XPathExpressionException e) {
      errors.add(e.toString());
    }
    return locations;
  }

  private List<String> testErrorLocationsFrom(Document doc) {

    List<String> locations = new ArrayList<>();
    XPath xpath = XPathFactory.newInstance().newXPath();

    try {
      XPathNodes fileNameNodes =
          xpath.evaluateExpression("/testsuite/testcase[error]/@classname", doc, XPathNodes.class);
      StreamSupport.stream(fileNameNodes.spliterator(), false)
          .map(Node::getNodeValue)
          .forEach(locations::add);
    } catch (XPathExpressionException e) {
      errors.add(e.toString());
    }
    return locations;
  }

  private List<Problem> failureProblemsIn(String absoluteLocation) {

    List<Problem> problemTypes = new ArrayList<>();
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression =
        String.format("/testsuite/testcase[@classname='%s'][failure]/system-out", absoluteLocation);

    try {
      XPathNodes problemNodes = xpath.evaluateExpression(expression, root, XPathNodes.class);
      System.out.println("failure size: " + problemNodes.size());
      StreamSupport.stream(problemNodes.spliterator(), false)
          .map(Node::getTextContent)
          .map(
              testName ->
                  new Problem("test failure", relativeLocationFrom(absoluteLocation, testName)))
          .forEach(problemTypes::add);
    } catch (XPathExpressionException e) {
      errors.add(e.toString());
    }
    return problemTypes;
  }

  private List<Problem> errorProblemsIn(String absoluteLocation) {
    System.out.println("absolute: " + absoluteLocation);
    List<Problem> problemTypes = new ArrayList<>();
    XPath xpath = XPathFactory.newInstance().newXPath();
    String expression =
        String.format("/testsuite/testcase[@classname='%s'][error]/system-out", absoluteLocation);

    try {
      XPathNodes problemNodes = xpath.evaluateExpression(expression, root, XPathNodes.class);

      StreamSupport.stream(problemNodes.spliterator(), false)
          .map(Node::getTextContent)
          .map(
              testName ->
                  new Problem("test error", relativeLocationFrom(absoluteLocation, testName)))
          .forEach(problemTypes::add);
    } catch (XPathExpressionException e) {
      errors.add(e.toString());
    }
    return problemTypes;
  }

  private boolean isJunit(Document doc) {

    XPath xpath = XPathFactory.newInstance().newXPath();
    try {
      xpath.evaluateExpression("/testsuite", doc, XPathNodes.class);
      return true;
    } catch (XPathExpressionException e) {
      return false;
    }
  }

  private String relativeLocationFrom(String absoluteLocation, String testName) {
    System.out.println("testName: " + testName);
    String slashConverted = absoluteLocation.replace("\\", "/");
    int indexOfLastSlash = slashConverted.lastIndexOf("/");
    String testClassName = slashConverted.substring(indexOfLastSlash + 1);
    return String.format("[%s][%s]", testClassName, formattedOut(testName));
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

  private static String formattedOut(String s) {
    int indexOfDisplayName = s.indexOf("display-name:");
    return s.substring(indexOfDisplayName + 14).trim();
  }
}
