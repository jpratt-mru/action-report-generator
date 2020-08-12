package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PmdParserGoodFileTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources");
  private SummarizingParser parserUnderTest;

  @BeforeEach
  public void setUp() {
    parserUnderTest = new PmdParser();
  }

  @Test
  @DisplayName("it should return a pmd summary with no problems detected if xml says so")
  public void it_should_return_pmd_summary_with_no_problems_detected_when_xml_says_so() {

    Path validNoErrorsFile = RESOURCE_DIR.resolve("valid-no-error-pmd.xml");
    SummaryResult result = parserUnderTest.parse(validNoErrorsFile);

    assertThat(result).isInstanceOf(PmdSummaryResult.class);
    assertThat(result.hasProblems()).isFalse();
    assertThat(result.problems()).isEmpty();
  }

  @Test
  @DisplayName("it should return a pmd summary with problems detected if xml has one problem")
  public void it_should_return_pmd_summary_with_problems_detected_when_xml_has_one_problems() {

    Path oneErrorFile = RESOURCE_DIR.resolve("valid-one-error-pmd.xml");
    SummaryResult result = parserUnderTest.parse(oneErrorFile);

    assertThat(result).isInstanceOf(PmdSummaryResult.class);
    assertThat(result.hasProblems()).isTrue();
    assertThat(result.problems()).containsExactly("UnusedLocalVariable");
  }

  @Test
  @DisplayName(
      "it should return a pmd summary with problems only once and in alphabetical order if xml has multiple problems in different files")
  public void
      it_should_return_pmd_summary_with_problems_only_once_and_in_alphabetic_order_when_xml_has_multiple_problems_in_different_files() {

    Path validFileMultipleRepeatedErrors =
        RESOURCE_DIR.resolve("valid-multiple-errors-diff-files-pmd.xml");
    SummaryResult result = parserUnderTest.parse(validFileMultipleRepeatedErrors);

    assertThat(result).isInstanceOf(PmdSummaryResult.class);
    assertThat(result.hasProblems()).isTrue();
    assertThat(result.problems())
        .containsExactly(
            "ControlStatementBraces",
            "LocalVariableNamingConventions",
            "UnconditionalIfStatement",
            "UnusedLocalVariable");
  }
}
