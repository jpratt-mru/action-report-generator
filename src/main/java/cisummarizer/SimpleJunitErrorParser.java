package cisummarizer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleJunitErrorParser extends SimpleJunitParser {

  public SimpleJunitErrorParser(Path pathToResults) {
    super(pathToResults, "error");
  }
}
