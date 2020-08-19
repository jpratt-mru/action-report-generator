package cisummarizer;

import java.nio.file.Path;

public class SimpleJunitFailureParser extends SimpleJunitParser {

  public SimpleJunitFailureParser(Path pathToResults) {
    super(pathToResults, "failure");
  }
}
