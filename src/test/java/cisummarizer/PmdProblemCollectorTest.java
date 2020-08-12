package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PmdProblemCollectorTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/pmd-problem-collector");

  @Test
  @DisplayName(
      "it should have no problems and no errors when given path to a pmd result with no errors")
  public void
      it_should_have_no_problems_and_no_errors_when_given_a_path_to_pmd_result_with_no_errors() {

    Path noErrorFile = RESOURCE_DIR.resolve("valid-no-error-pmd.xml");
    ProblemCollector collector = new PmdProblemCollector(noErrorFile);

    assertThat(collector.problems()).isEmpty();
    assertThat(collector.hasProblems()).isFalse();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName(
      "it should have one problem and no errors when given a path to a pmd result with one error")
  public void
      it_should_have_one_problem_and_no_errors_when_given_a_path_to_pmd_result_with_one_error() {

    Path oneErrorFile = RESOURCE_DIR.resolve("valid-one-error-pmd.xml");
    ProblemCollector collector = new PmdProblemCollector(oneErrorFile);

    assertThat(collector.problems()).containsExactly("UnusedLocalVariable");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName(
      "it should return all unique problems in alphabetic order and no errors when given a path to pmd result with multiple repeated errors in different files")
  public void
      it_should_return_multiple_unique_ordered_no_errors_when_given_path_to_pmd_result_with_multiple_errors() {

    Path validFileMultipleRepeatedErrors =
        RESOURCE_DIR.resolve("valid-multiple-errors-diff-files-pmd.xml");
    ProblemCollector collector = new PmdProblemCollector(validFileMultipleRepeatedErrors);

    assertThat(collector.problems())
        .containsExactly(
            "ControlStatementBraces",
            "LocalVariableNamingConventions",
            "UnconditionalIfStatement",
            "UnusedLocalVariable");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }
}
