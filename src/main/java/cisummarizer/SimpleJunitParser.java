package cisummarizer;

import java.nio.file.Path;

public abstract class SimpleJunitParser extends SimpleXmlParser {

  protected String problemSeverity;

  public SimpleJunitParser(Path pathToResults, String problemSeverity) {
    super("junit", pathToResults);
    this.problemSeverity = problemSeverity;
  }

  @Override
  String rootExpression() {
    return "/testsuite";
  }

  @Override
  String locationExpression() {

    return String.format("/testsuite/testcase[%s]/@classname", problemSeverity);
  }

  @Override
  String locationToProblemExpression(String location) {
    return String.format(
        "/testsuite/testcase[@classname='%s'][%s]/system-out", location, problemSeverity);
  }

  @Override
  public String formattedType(String problemType) {
    int indexOfDisplayName = problemType.indexOf("display-name:");
    String displayName = problemType.substring(indexOfDisplayName + 14).trim();
    return String.format("test %s: %s", problemSeverity, displayName);
  }
}
