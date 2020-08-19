package cisummarizer;

import java.util.function.Function;

/**
 * A Report that display the errors and problems in a SimpleJunitErrorParser.
 *
 * <p>Here's what a JunitErrorReport looks like when there are no errors and no problems:
 *
 * <pre>
 * [junit]
 * no junit errors found
 * </pre>
 *
 * <p>Here's what a JunitErrorReport looks like when with one error and no problems:
 *
 * <pre>
 * [junit]
 * something bad happened:
 * |
 * |-- bad thing
 * </pre>
 *
 * <p>Here's what a JunitErrorReport looks like when problems are there - notice that the text
 * displayed is the name of the TestClass where the error occurred along with the name of the method
 * (or display name) of the test that error'd out - these are all displayed in alphabetic order:
 *
 * <pre>
 * [junit]
 * junit errors found: 4
 * |
 * |-- [ClassA] some method A
 * |-- [ClassA] some method B
 * |-- [ClassB] some method
 * |-- [ClassC] some method
 * </pre>
 *
 * @author jpratt
 */
public class JunitErrorReport extends Report {

  public JunitErrorReport(SimpleJunitErrorParser parser) {
    super(parser, "junit");
  }

  @Override
  String nameForProblems() {
    return "errors";
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
            "[%s] %s", problem.getLocation(), problem.getType().replace("test error: ", ""));
  }
}
