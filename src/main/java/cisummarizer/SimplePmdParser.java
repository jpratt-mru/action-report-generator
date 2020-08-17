package cisummarizer;

import java.nio.file.Path;

public class SimplePmdParser extends SimpleXmlParser {

  public SimplePmdParser(Path pathToPmdResults) {
    super("pmd", pathToPmdResults);
  }

  @Override
  protected String rootExpression() {
    return "/pmd";
  }

  @Override
  protected String locationExpression() {
    return "/pmd/file/@name";
  }

  @Override
  protected String locationToProblemExpression(String location) {
    return String.format("/pmd/file[@name='%s']/violation/@rule", location);
  }
}
