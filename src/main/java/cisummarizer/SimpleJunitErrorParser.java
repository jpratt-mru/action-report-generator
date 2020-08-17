package cisummarizer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJunitErrorParser extends SimpleJunitParser {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/junit-xml-examples");

  public static void main(String[] args) {
    SimpleXmlParser parser =
        new SimpleJunitErrorParser(RESOURCE_DIR.resolve("TEST-junit-jupiter.xml"));
    System.out.println(parser.errors());
    System.out.println(parser.problems());
  }

  public SimpleJunitErrorParser(Path pathToResults) {
    super(pathToResults, "error");
  }
}
