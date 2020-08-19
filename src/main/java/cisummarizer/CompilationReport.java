package cisummarizer;

public class CompilationReport extends Report {

  public CompilationReport(Status status) {
    super(status, "compilation", "problems", Problem::getLocation);
  }
}
