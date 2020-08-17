package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SimplePmdParserTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/pmd-problem-collector");

  public static Stream<String> filesWithInvalidData() {
    return Stream.of("blorp", "empty.xml", "malformed.xml", "valid-xml-but-not-pmd.xml");
  }

  @ParameterizedTest
  @MethodSource(value = "filesWithInvalidData")
  @DisplayName("it should have no problems but have errors when given path to bad file")
  public void it_should_have_no_problems_but_have_errors_when_given_a_path_to_bad_file(
      String fileName) {
    Path path = RESOURCE_DIR.resolve(fileName);
    SimplePmdParser collector = new SimplePmdParser("pmd", path);

    assertThat(collector.problems()).isEmpty();
    assertThat(collector.errors()).isNotEmpty();
  }

  @Test
  @DisplayName(
      "it should have no problems and no errors when given path to a pmd result with no errors")
  public void
      it_should_have_no_problems_and_no_errors_when_given_a_path_to_pmd_result_with_no_errors() {

    Path noErrorFile = RESOURCE_DIR.resolve("valid-no-error-pmd.xml");
    SimplePmdParser collector = new SimplePmdParser("pmd", noErrorFile);

    assertThat(collector.problems()).isEmpty();
    assertThat(collector.errors()).isEmpty();
  }

  @Test
  @DisplayName(
      "it should have one problem and no errors when given a path to a pmd result with one error")
  public void
      it_should_have_one_problem_and_no_errors_when_given_a_path_to_pmd_result_with_one_error() {

    Path oneErrorFile = RESOURCE_DIR.resolve("valid-one-error-pmd.xml");
    SimplePmdParser collector = new SimplePmdParser("pmd", oneErrorFile);

    assertThat(collector.problems())
        .extracting(Problem::getType)
        .containsOnly("UnusedLocalVariable");
    assertThat(collector.errors()).isEmpty();
  }

  @Test
  @DisplayName(
      "it should return all unique problems in alphabetic order and no errors when given a path to pmd result with multiple repeated errors in different files")
  public void
      it_should_return_multiple_unique_ordered_no_errors_when_given_path_to_pmd_result_with_multiple_errors() {

    Path validFileMultipleRepeatedErrors =
        RESOURCE_DIR.resolve("valid-multiple-errors-diff-files-pmd.xml");
    SimplePmdParser collector = new SimplePmdParser("pmd", validFileMultipleRepeatedErrors);

    assertThat(collector.problems())
        .extracting(Problem::getType)
        .containsOnly(
            "ControlStatementBraces",
            "LocalVariableNamingConventions",
            "UnconditionalIfStatement",
            "UnusedLocalVariable");
    assertThat(collector.errors()).isEmpty();
  }
}
