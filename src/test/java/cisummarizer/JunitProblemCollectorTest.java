package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled("until done working with stuffs")
public class JunitProblemCollectorTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/junit-problem-collector");

  @Test
  @DisplayName(
      "it should have no problems and no errors when given path to a junit result with no errors")
  public void
      it_should_have_no_problems_and_no_errors_when_given_a_path_to_junit_result_with_no_errors() {

    Path noErrorFile = RESOURCE_DIR.resolve("valid-no-error-junit.txt");
    ProblemCollector collector = new JunitProblemCollector(noErrorFile);

    assertThat(collector.problems()).isEmpty();
    assertThat(collector.hasProblems()).isFalse();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName(
      "it should have one problem and no errors when given a path to a junit result with one error")
  public void
      it_should_have_one_problem_and_no_errors_when_given_a_path_to_junit_result_with_one_error() {

    Path oneErrorFile = RESOURCE_DIR.resolve("valid-one-error-junit.txt");
    ProblemCollector collector = new JunitProblemCollector(oneErrorFile);

    assertThat(collector.problems()).containsExactly("not_gonna_pass_ever() [X]");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName(
      "it should have two problems and no errors when given a path to a junit result with two errors in single test class")
  public void
      it_should_have_two_problems_and_no_errors_when_given_a_path_to_junit_result_with_two_errors_single_test_class() {

    Path oneErrorFile = RESOURCE_DIR.resolve("valid-two-errors-one-test-class-junit.txt");
    ProblemCollector collector = new JunitProblemCollector(oneErrorFile);

    assertThat(collector.problems())
        .containsExactly(
            "when you call main, it should print the expected lines [X]",
            "not_gonna_pass_ever() [X]");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName("it should be able to handle nested junit5 tests")
  public void it_should_be_able_to_handle_nested_junit5_tests() {

    Path oneErrorFile = RESOURCE_DIR.resolve("nested-tests.txt");
    ProblemCollector collector = new JunitProblemCollector(oneErrorFile);

    assertThat(collector.problems())
        .containsExactly(
            "an outer level test [X]",
            "nest one failing test [X]",
            "nested nested failing test [X]",
            "nest two failing test [X]");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }

  @Test
  @DisplayName("it should be able to handle dynamic junit5 tests")
  public void it_should_be_able_to_handle_dynamic_junit5_tests() {

    Path oneErrorFile = RESOURCE_DIR.resolve("dynamic-tests.txt");
    ProblemCollector collector = new JunitProblemCollector(oneErrorFile);

    assertThat(collector.problems()).containsExactly("string a [X]", "string c [X]");
    assertThat(collector.hasProblems()).isTrue();
    assertThat(collector.hasError()).isFalse();
  }
}
