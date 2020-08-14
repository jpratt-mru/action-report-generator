package cisummarizer;

import java.util.List;
import static java.util.stream.Collectors.toList;

class CompilationReport implements Report {

  Status compilationStatus;

  public CompilationReport(Status compilationStatus) {
    this.compilationStatus = compilationStatus;
  }

  @Override
  public String summary() {
    if (compilationStatus.problems().isEmpty()) {
      return "no compilation problems";
    } else {
      return String.format(
          "%d %s did not compile:", compilationStatus.problems().size(), pluralized());
    }
  }

  private String pluralized() {
    return compilationStatus.problems().size() == 1 ? "file" : "files";
  }

  @Override
  public List<String> problemDescriptions() {
    if (compilationStatus.problems().isEmpty()) {
      return List.of();
    } else {
      return compilationStatus.problems().stream().map(this::decoratedProblem).collect(toList());
    }
  }

  private String decoratedProblem(Problem rawProblem) {
    return "|-- " + rawProblem.getLocation();
  }

  @Override
  public String header() {
    return "[compilation]";
  }
}
