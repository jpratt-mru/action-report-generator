package cisummarizer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJunitFailureParser extends SimpleJunitParser {

  public SimpleJunitFailureParser(Path pathToResults) {
    super(pathToResults, "failure");
  }
}
