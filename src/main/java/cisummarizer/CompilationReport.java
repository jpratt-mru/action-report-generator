package cisummarizer;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;

class CompilationReport implements Report {

  private Status compilationStatus;
  private int numProblems;
  private boolean errorsEncountered;

  public CompilationReport(Status compilationStatus) {

    this.compilationStatus = compilationStatus;
    numProblems = compilationStatus.problems().size();
    errorsEncountered = !compilationStatus.errors().isEmpty();
  }

  @Override
  public String summary() {
    if (errorsEncountered) {
      return "something bad happened:";
    } else if (numProblems == 0) {
      return "no compilation problems";
    } else {
      return String.format("%d %s did not compile:", numProblems, pluralized(numProblems));
    }
  }

  private String pluralized(int numProblems) {
    return numProblems == 1 ? "file" : "files";
  }

  @Override
  public List<String> problemDescriptions() {
    if (errorsEncountered) {
      Function<String, String> decorator = errorMsg -> "|-- " + errorMsg;
      return decoratedDescriptions(compilationStatus.errors(), decorator);
    } else if (numProblems == 0) {
      return List.of();
    } else {
      Function<Problem, String> decorator = problem -> "|-- " + problem.getLocation();
      return decoratedDescriptions(compilationStatus.problems(), decorator);
    }
  }

  private <T> List<String> decoratedDescriptions(
      List<T> thingsToDecorate, Function<T, String> decorator) {
    List<String> decoratedThings = thingsToDecorate.stream().map(decorator).collect(toList());

    // add a purdy little pipe symbol so that the problem descriptions look like
    //   | <== this is added
    //   |-- A
    //   |-- B
    decoratedThings.add(0, "|");
    return decoratedThings;
  }

  @Override
  public String header() {
    return "[compilation]";
  }
}
