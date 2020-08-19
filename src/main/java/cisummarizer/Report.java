package cisummarizer;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * You can ask a report things. Things like:
 * <li>"what header should I show if I print you?"
 * <li>"what is the pithy summary of your contents?"
 * <li>"what are the pertinent descriptions of the problems you'd like to talk about?"
 *
 * @author jpratt
 */
abstract class Report {

  private String reportType;

  private int numProblems;
  private boolean errorsEncountered;
  private List<String> decoratedErrorDetails;
  private List<String> decoratedProblemDetails;

  public Report(Parser parser, String reportType) {

    this.reportType = reportType;

    errorsEncountered = !parser.errors().isEmpty();
    decoratedErrorDetails = decoratedDescriptions(parser.errors());

    List<String> problemsAsText =
        parser.problems().stream().map(problemAsString()).distinct().collect(toList());
    problemsAsText.sort(String::compareTo);
    numProblems = problemsAsText.size();

    decoratedProblemDetails =
        decoratedDescriptions(problemsAsText).stream().distinct().collect(toList());
  }

  /**
   * Return what name you would like to give problems for this report. Are they "problems"?
   * "violations"? "errors"?
   *
   * @return the pluralized name for the concept of "problem" in this report
   */
  abstract String nameForProblems();

  /**
   * Return what text you would like to show in the report for a given problem.
   *
   * <p>Often, this may simply be the getLocation() or getType() of the problem, but you can make
   * this whatever you want.
   *
   * <p>Provides a bit of flexibility with the display of problems in the report.
   *
   * @return the text representation of the problem you want to show on the report
   */
  abstract Function<Problem, String> problemAsString();

  public String header() {
    return String.format("[%s]", reportType);
  }

  /**
   * Return a brief description of the result of the report. We distinguish between 3 basic cases:
   *
   * <ul>
   *   <ol>
   *     an error occurred
   *   </ol>
   *   <ol>
   *     no errors happened and no problems as well
   *   </ol>
   *   <ol>
   *     no errors happened and one or more problems happened
   *   </ol>
   * </ul>
   *
   * @return the summary for this report
   */
  public String summary() {
    if (errorsEncountered) {
      return "something bad happened:";
    } else if (numProblems == 0) {
      return String.format("no %s %s found", reportType, nameForProblems());
    } else {
      return String.format("%s %s found: %d", reportType, nameForProblems(), numProblems);
    }
  }

  /**
   * Return a decorated list of lines showing what errors or problems are present in this report.
   * (If no errors or problems are present, then an empty list is returned.)
   *
   * @return a decorated list of errors or problems
   */
  public List<String> details() {
    if (errorsEncountered) {
      return decoratedErrorDetails;
    } else if (numProblems == 0) {
      return Collections.emptyList();
    } else {
      return decoratedProblemDetails;
    }
  }

  /**
   * Return the header, summary, and details of this report as a single String.
   *
   * @return
   */
  public String allContent() {
    String detailsAsString = String.join("\n", details()).trim();
    return String.join("\n", header(), summary(), detailsAsString).trim();
  }

  /**
   * Helper to turn the text form of an error/problem into something slightly purdier.
   *
   * <p>This is how we decorate a problem/error line in the report:
   *
   * <pre>
   * |-- blahblahblah
   * </pre>
   */
  private UnaryOperator<String> formattedLine = line -> String.format("|-- %s", line);

  private List<String> decoratedDescriptions(List<String> thingsToDecorate) {
    List<String> decoratedThings = thingsToDecorate.stream().map(formattedLine).collect(toList());

    // add a purdy little pipe symbol so that the problem descriptions look like
    //   | <== this is added
    //   |-- A
    //   |-- B
    decoratedThings.add(0, "|");
    return decoratedThings;
  }
}
