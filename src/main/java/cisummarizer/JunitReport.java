package cisummarizer;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class JunitReport implements Report {

  private Status junitStatus;
  private int numProblems;
  private int numFailures;
  private int numErrors;
  private boolean errorsEncountered;
  private List<Problem> orderedTestErrors;
  private List<Problem> orderedTestFailures;

  public JunitReport(Status junitStatus) {
    this.junitStatus = junitStatus;
    numProblems = junitStatus.problems().size();
    errorsEncountered = !junitStatus.errors().isEmpty();

    Comparator<Problem> comparator =
        Comparator.comparing(Problem::getLocation).thenComparing(Problem::getType);

    orderedTestErrors =
        junitStatus
            .problems()
            .stream()
            .filter(p -> p.getType().contains("error"))
            .collect(toList());

    orderedTestFailures =
        junitStatus
            .problems()
            .stream()
            .filter(p -> p.getType().contains("failure"))
            .collect(toList());

    orderedTestErrors.sort(comparator);
    orderedTestFailures.sort(comparator);

    numErrors = orderedTestErrors.size();
    numFailures = orderedTestFailures.size();
  }

  @Override
  public String header() {
    return "[junit]";
  }

  @Override
  public String summary() {
    if (errorsEncountered) {
      return "something bad happened:";
    } else if (numProblems == 0) {
      return String.join("\n", "no test errors found", "no test failures found");
    } else {
      return String.format(
          "test errors found: %d%ntest failures found: %d", numErrors, numFailures);
    }
  }

  @Override
  public List<String> details() {
    if (errorsEncountered) {
      Function<String, String> decorator = errorMsg -> "|-- " + errorMsg;
      return decoratedDescriptions(junitStatus.errors(), decorator);
    } else if (numProblems == 0) {
      return List.of();
    } else {
      return errorsAndFailures();
    }
  }

  private List<String> errorsAndFailures() {
    Function<Problem, String> decorator =
        problem -> String.format("|-- [%s] %s", problem.getLocation(), problem.getType());
    List<String> decoratedTestErrors = decoratedDescriptions(orderedTestErrors, decorator);
    List<String> decoratedTestFailures = decoratedDescriptions(orderedTestFailures, decorator);
    decoratedTestErrors.addAll(decoratedTestFailures);

    return decoratedTestErrors;
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
  public String allContent() {
    String detailsAsString = String.join("\n", details());
    return String.join("\n", header(), summary(), detailsAsString).trim();
  }

  private String pluralized(int numProblems) {
    return numProblems == 1 ? "error/failure" : "errors/failures";
  }
}
