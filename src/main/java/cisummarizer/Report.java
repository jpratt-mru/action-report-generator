package cisummarizer;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * You can ask a report things. Things like:
 * <li>"what header should I show if I print you?"
 * <li>"what is the pithy summary of your contents?"
 * <li>"what are the pertinent descriptions of the problems you'd like to talk about?"
 *
 * @author jpratt
 */
abstract class Report {

  private Parser parser;
  private int numProblems;
  private boolean errorsEncountered;
  private List<Problem> orderedProblems;
  private String reportType;

  public Report(Parser parser, String reportType) {
    this.parser = parser;
    this.reportType = reportType;
    numProblems = parser.problems().size();
    errorsEncountered = !parser.errors().isEmpty();

    orderedProblems = new ArrayList<>(parser.problems());
    orderedProblems.sort(comparing(toDisplay()));
  }

  abstract String nameForProblems();

  abstract Function<Problem, String> toDisplay();

  public String header() {
    return String.format("[%s]", reportType);
  }

  public String summary() {
    if (errorsEncountered) {
      return "something bad happened:";
    } else if (numProblems == 0) {
      return String.format("no %s %s found", reportType, nameForProblems());
    } else {
      return String.format("%s %s found: %d", reportType, nameForProblems(), numProblems);
    }
  }

  public List<String> details() {
    if (errorsEncountered) {
      Function<String, String> decorator = errorMsg -> "|-- " + errorMsg;
      return decoratedDescriptions(parser.errors(), decorator);
    } else if (numProblems == 0) {
      return List.of();
    } else {
      Function<Problem, String> decorator = problem -> "|-- " + toDisplay().apply(problem);
      return decoratedDescriptions(orderedProblems, decorator);
    }
  }

  public String allContent() {
    String detailsAsString = String.join("\n", details()).trim();
    return String.join("\n", header(), summary(), detailsAsString).trim();
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
}
