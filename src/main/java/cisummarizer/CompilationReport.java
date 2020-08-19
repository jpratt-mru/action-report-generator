package cisummarizer;

import java.util.function.Function;

/**
 * A Report that display the errors and problems in a SimpleCompilationParser.
 *
 * <p>Here's what a CompilationReport looks like when there are no errors and no problems:
 *
 * <pre>
 * [compilation]
 * no compilation problems found
 * </pre>
 *
 * <p>Here's what a CompilationReport looks like when with one error and no problems:
 *
 * <pre>
 * [compilation]
 * something bad happened:
 * |
 * |-- bad thing
 * </pre>
 *
 * <p>Here's what a CompilationReport looks like when problems are there - notice that the location
 * of the problem is the only thing shown (as it's the only important thing for this summary report)
 * and that all the locations found are displayed in alphabetic order:
 *
 * <pre>
 * [compilation]
 * compilation problems found: 4
 * |
 * |-- some/java/path/A
 * |-- some/java/path/B
 * |-- some/java/path/C
 * |-- some/java/path/D
 * </pre>
 *
 * @author jpratt
 */
public class CompilationReport extends Report {

  public CompilationReport(SimpleCompilationParser parser) {
    super(parser, "compilation");
  }

  @Override
  String nameForProblems() {
    return "problems";
  }

  @Override
  Function<Problem, String> problemAsString() {
    return Problem::getLocation;
  }
}
