package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class SimpleCheckstyleParserTest {

  private static final Path RESOURCE_DIR =
      Paths.get("src/test/resources/checkstyle-problem-parser");

  private SimpleXmlParser parser;

  protected void makeParserFor(String fileName) {
    Path path = RESOURCE_DIR.resolve(fileName);
    parser = new SimpleCheckstyleParser(path);
  }

  public static Stream<String> filesWithInvalidData() {
    return Stream.of("blorp", "empty.xml", "malformed.xml", "valid-xml-but-not-checkstyle.xml");
  }

  @ParameterizedTest
  @MethodSource(value = "filesWithInvalidData")
  @DisplayName("it should have no problems but have errors when given path to bad file")
  public void it_should_have_no_problems_but_have_errors_when_given_a_path_to_bad_file(
      String fileName) {

    makeParserFor(fileName);

    assertThat(parser.problems()).isEmpty();
    assertThat(parser.errors()).isNotEmpty();
  }

  @Test
  @DisplayName(
      "it should have no problems and no errors when given path to a pmd result with no errors")
  public void
      it_should_have_no_problems_and_no_errors_when_given_a_path_to_pmd_result_with_no_errors() {

    makeParserFor("valid-no-error-checkstyle.xml");

    assertThat(parser.problems()).isEmpty();
    assertThat(parser.errors()).isEmpty();
  }

  @Test
  @DisplayName(
      "it should have one problem and no errors when given a path to a pmd result with one error")
  public void
      it_should_have_one_problem_and_no_errors_when_given_a_path_to_pmd_result_with_one_error() {

    makeParserFor("valid-one-error-checkstyle.xml");

    assertThat(parser.problems())
        .extracting(Problem::getType)
        .containsOnly("com.puppycrawl.tools.checkstyle.checks.naming.LocalVariableNameCheck");
    assertThat(parser.errors()).isEmpty();
  }

  @Test
  @DisplayName(
      "it should return all unique problems in alphabetic order and no errors when given a path to pmd result with multiple repeated errors in different files")
  public void
      it_should_return_multiple_unique_ordered_no_errors_when_given_path_to_pmd_result_with_multiple_errors() {

    makeParserFor("valid-multiple-errors-diff-files-checkstyle.xml");

    assertThat(parser.problems())
        .extracting(Problem::getType)
        .containsOnly(
            "com.puppycrawl.tools.checkstyle.checks.naming.LocalVariableNameCheck",
            "com.puppycrawl.tools.checkstyle.checks.naming.LocalVariableNameCheck",
            "com.puppycrawl.tools.checkstyle.checks.blocks.NeedBracesCheck");
    assertThat(parser.errors()).isEmpty();
  }
}
