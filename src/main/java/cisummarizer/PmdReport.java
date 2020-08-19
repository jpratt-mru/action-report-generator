package cisummarizer;

import java.util.function.Function;

/**
 * A Report that display the errors and problems in a SimplePmdParser.
 *
 * <p>Here's what a PmdReport looks like when there are no errors and no problems:
 *
 * <pre>
 * [pmd]
 * no pmd violations found
 * </pre>
 *
 * <p>Here's what a PmdReport looks like when with one error and no problems:
 *
 * <pre>
 * [pmd]
 * something bad happened:
 * |
 * |-- bad thing
 * </pre>
 *
 * <p>Here's what a PmdReport looks like when problems are there - notice that the location of the
 * problem is not shown (as it's not important for this summary report) and that all the problems
 * found are displayed in alphabetic order:
 *
 * <pre>
 * [pmd]
 * pmd violations found: 4
 * |
 * |-- violationA
 * |-- violationB
 * |-- violationC
 * |-- violationD
 * </pre>
 *
 * @author jpratt
 */
public class PmdReport extends Report {

  public PmdReport(Parser parser) {
    super(parser, "pmd");
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
