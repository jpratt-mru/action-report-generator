package cisummarizer;

public class DetailedTestSummary implements Summary {

  private String name;
  private ProblemCollector collector;

  public DetailedTestSummary(ProblemCollector collector, String name) {
    this.collector = collector;
    this.name = name;
  }

  public boolean summaryFailed() {
    return collector.hasError();
  }

  @Override
  public String problemNumber() {
    String result =
        collector.hasError() ? "invalid file" : String.valueOf(collector.problems().size());
    return String.format("\"number of %s problems\": %s", name, result);
  }

  @Override
  public String problemSummary() {
    String header =
        String.format(
            "test results (compilation errors: %d, failing tests: %d)",
            numberCompilation(), numberFailing());
    String details =
        collector.hasError()
            ? "invalid file"
            : collector.hasProblems() ? String.join("\n", collector.problems()) : "[none]";
    return String.join("\n", header, details);
  }

  private int numberFailing() {
    return (int) collector.problems().stream().filter(this::isFailingTest).count();
  }

  private int numberCompilation() {
    return (int) collector.problems().stream().filter(this::isCompilationError).count();
  }

  private boolean isCompilationError(String s) {
    return s.contains("compilation");
  }

  private boolean isFailingTest(String s) {
    return s.trim().endsWith("[X]");
  }
}
