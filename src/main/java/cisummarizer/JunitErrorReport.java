package cisummarizer;

import java.util.function.Function;

public class JunitErrorReport extends Report {

  public JunitErrorReport(Status status) {
    super(status, "junit error", "errors", Problem::getLocation);
    // TODO Auto-generated constructor stub
  }
}
