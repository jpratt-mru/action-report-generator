package cisummarizer;

public class CheckstyleReport extends Report {

  public CheckstyleReport(Status status) {
    super(status, "checkstyle", "violations", Problem::getType);
  }
}
