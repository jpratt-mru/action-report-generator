package cisummarizer;

import java.nio.file.Path;

public class SimpleJunitErrorParser extends SimpleJunitParser {

  public SimpleJunitErrorParser(Path pathToResults) {
    super(pathToResults, "error");
  }
}
