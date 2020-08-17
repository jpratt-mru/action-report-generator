package cisummarizer;

import java.nio.file.Path;

public class SimpleCheckstyleParser extends SimpleXmlParser {

  public SimpleCheckstyleParser(Path pathToPmdResults) {
    super("checkstyle", pathToPmdResults);
  }

  @Override
  protected String rootExpression() {
    return "/checkstyle";
  }

  @Override
  protected String locationExpression() {
    return "/checkstyle/file/@name";
  }

  @Override
  protected String locationToProblemExpression(String location) {
    return String.format("/checkstyle/file[@name='%s']/error/@source", location);
  }
}
