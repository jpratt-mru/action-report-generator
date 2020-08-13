package cisummarizer;

import java.util.List;
import java.util.stream.Collectors;

public class PassFailSummary implements Summary {

  private String name;
  private ProblemCollector collector;

  public PassFailSummary(ProblemCollector collector, String name) {
    this.name = name;
    this.collector = collector;
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
    String header = String.format("%s problem list", name);
    String details =
        collector.hasError() ? "invalid file" : collector.hasProblems() ? problemList() : "[none]";
    return String.join("\n", header, details);
  }

  private String problemList() {

    List<String> withBullets =
        collector.problems().stream().map(p -> "|-- " + p).collect(Collectors.toList());
    return String.join("\n", withBullets);
  }
}
