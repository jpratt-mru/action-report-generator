package cisummarizer;

public class PmdReport extends Report {

  public PmdReport(Status status) {
    super(status, "pmd", "violations", Problem::getType);
  }
}
