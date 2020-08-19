package cisummarizer;

import java.util.function.Function;

/**
 * A Report that display the errors and problems in a SimpleCheckstyleParser.
 *
 * <p>Here's what a CheckstyleReport looks like when there are no errors and no problems:
 *
 * <pre>
 * [checkstyle]
 * no checkstyle violations found
 * </pre>
 *
 * <p>Here's what a CheckstyleReport looks like when with one error and no problems:
 *
 * <pre>
 * [checkstyle]
 * something bad happened:
 * |
 * |-- bad thing
 * </pre>
 *
 * <p>Here's what a CheckstyleReport looks like when problems are there - notice that the location
 * of the problem is not shown (as it's not important for this summary report) and that all the
 * problems found are displayed in alphabetic order:
 *
 * <pre>
 * [checkstyle]
 * checkstyle violations found: 4
 * |
 * |-- violationA
 * |-- violationB
 * |-- violationC
 * |-- violationD
 * </pre>
 *
 * @author jpratt
 */
public class CheckstyleReport extends Report {

  public CheckstyleReport(SimpleCheckstyleParser parser) {
    super(parser, "checkstyle");
  }

  @Override
  String nameForProblems() {
    return "violations";
  }

  @Override
  Function<Problem, String> problemAsString() {
    return Problem::getType;
  }
}
