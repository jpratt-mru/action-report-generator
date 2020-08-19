package cisummarizer;

import java.util.function.Function;

/**
 * A Report that display the failures and problems in a SimpleJunitParser.
 *
 * <p>Here's what a JunitReport looks like when there are no failures and no problems:
 *
 * <pre>
 * [junit]
 * no junit failures found
 * </pre>
 *
 * <p>Here's what a JunitReport looks like when with one failure and no problems:
 *
 * <pre>
 * [junit]
 * something bad happened:
 * |
 * |-- bad thing
 * </pre>
 *
 * <p>Here's what a JunitReport looks like when problems are there - notice that the text displayed
 * is the name of the TestClass where the failure occurred along with the name of the method (or
 * display name) of the test that failure'd out - these are all displayed in alphabetic order:
 *
 * <pre>
 * [junit]
 * junit failures found: 4
 * |
 * |-- [ClassA] some method A
 * |-- [ClassA] some method B
 * |-- [ClassB] some method
 * |-- [ClassC] some method
 * </pre>
 *
 * @author jpratt
 */
public class JunitFailureReport extends Report {

  public JunitFailureReport(Parser status) {
    super(status, "junit");
  }

  @Override
  String nameForProblems() {
    return "failures";
  }

  /**
   * The details displayed for this report is a bit complicated.
   *
   * <p>Need to do some extra work here because the problems returned by the SimpleJunitErrorParser
   * is in the form:
   *
   * <pre>
   * test error: name_of_the_method_that_error'd (or its display name)
   * </pre>
   */
  @Override
  Function<Problem, String> problemAsString() {
    return problem ->
        String.format(
            "[%s] %s", problem.getLocation(), problem.getType().replace("test failure: ", ""));
  }
}
