package cisummarizer;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PmdReport implements Report {

  private Status pmdStatus;
  private int numProblems;
  private boolean errorsEncountered;
  private List<Problem> orderedProblems;

  public PmdReport(Status pmdStatus) {
    this.pmdStatus = pmdStatus;
    numProblems = pmdStatus.problems().size();
    errorsEncountered = !pmdStatus.errors().isEmpty();

    orderedProblems = new ArrayList<>(pmdStatus.problems());
    orderedProblems.sort(comparing(Problem::getType));
  }

  @Override
  public String header() {
    return "[pmd]";
  }

  @Override
  public String summary() {
    if (errorsEncountered) {
      return "something bad happened:";
    } else if (numProblems == 0) {
      return "no pmd violations found";
    } else {
      return String.format("%d pmd %s found:", numProblems, pluralized(numProblems));
    }
  }

  @Override
  public List<String> details() {
    if (errorsEncountered) {
      Function<String, String> decorator = errorMsg -> "|-- " + errorMsg;
      return decoratedDescriptions(pmdStatus.errors(), decorator);
    } else if (numProblems == 0) {
      return List.of();
    } else {
      Function<Problem, String> decorator = problem -> "|-- " + problem.getType();
      return decoratedDescriptions(orderedProblems, decorator);
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
  public String allContent() {
    String detailsAsString = String.join("\n", details());
    return String.join("\n", header(), summary(), detailsAsString);
  }

  private String pluralized(int numProblems) {
    return numProblems == 1 ? "violation" : "violations";
  }
}
