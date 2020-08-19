package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimpleCompilationParserTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/compilation-status");

  private Parser status;

  void getStatusFor(String fileName) {
    Path pathToFile = RESOURCE_DIR.resolve(fileName);
    status = new SimpleCompilationParser(pathToFile);
  }

  @Test
  @DisplayName("a status has a list of errors if the compilation result file isn't there")
  public void should_have_list_of_errors_if_compilation_result_not_there() {
    getStatusFor("fnoo.txt");

    assertThat(status.errors()).isNotEmpty();
  }

  @Test
  @DisplayName("a status has no problems if the compilation result file is empty")
  public void should_be_0_problems_if_compilation_result_empty() throws IOException {
    getStatusFor("no-errors.txt");

    assertThat(status.errors()).isEmpty();
    assertThat(status.problems()).isEmpty();
  }

  @Test
  @DisplayName(
      "a status has 1 problem if the compilation result file shows one file with one error")
  public void should_be_1_problem_if_compilation_result_shows_one_file_one_error() {
    getStatusFor("one-file-one-error.txt");

    assertThat(status.errors()).isEmpty();
    assertThat(status.problems())
        .extracting(Problem::getLocation)
        .containsExactly("src/main/Main.java");
  }

  @Test
  @DisplayName(
      "a status has 1 problem if the compilation result file shows one file with multiple errors")
  public void should_be_1_problem_if_compilation_result_shows_one_file_multiple_errors() {
    getStatusFor("one-file-multiple-errors.txt");

    assertThat(status.errors()).isEmpty();
    assertThat(status.problems())
        .extracting(Problem::getLocation)
        .containsExactly("src/main/DrillUtil.java");
  }

  @Test
  @DisplayName(
      "a status has 2 problems in alphabetic order if the compilation result file shows two files with multiple errors")
  public void
      should_be_2_problems_in_alphabetic_order_if_compilation_result_shows_two_files_multiple_errors() {
    getStatusFor("two-files-multiple-errors.txt");

    assertThat(status.errors()).isEmpty();
    assertThat(status.problems())
        .extracting(Problem::getLocation)
        .containsExactly("src/main/Main.java", "src/test/DynamicTest.java");
  }

  @Test
  @DisplayName(
      "a status has multiple problems in alphabetic order if the compilation result file shows multiple files with multiple errors")
  public void
      should_be_multiple_problems_in_alphabetic_order_if_compilation_result_shows_multiple_files_multiple_errors() {
    getStatusFor("multiple-files-multiple-errors.txt");

    assertThat(status.errors()).isEmpty();
    assertThat(status.problems())
        .extracting(Problem::getLocation)
        .containsExactly(
            "src/main/DrillUtil.java",
            "src/main/LameUtility.java",
            "src/main/Main.java",
            "src/test/DynamicTest.java",
            "src/test/FooTest.java");
  }
}
