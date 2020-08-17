package cisummarizer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJunitFailureParser extends SimpleJunitParser {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/junit-xml-examples");

  public static void main(String[] args) {
    SimpleJunitParser parser =
        new SimpleJunitFailureParser(RESOURCE_DIR.resolve("TEST-junit-jupiter.xml"));
    System.out.println(parser.errors());
    System.out.println(parser.problems());
  }

  public SimpleJunitFailureParser(Path pathToResults) {
    super(pathToResults, "failure");
  }
}
