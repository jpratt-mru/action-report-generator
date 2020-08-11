package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PmdParserGoodFileTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources");
  private SummarizingParser parserUnderTest;

  @BeforeEach
  public void setUp() {
    parserUnderTest = new PmdParser();
  }

  @Test
  @DisplayName("it should return a checkstyle summary with no problems detected if xml says so")
  public void it_should_return_checkstyle_summary_with_no_problems_detected_when_xml_says_so()
      throws IOException {

    Path validNoErrorsFile = RESOURCE_DIR.resolve("valid-no-error-pmd.xml");

    SummaryResult result = parserUnderTest.parse(validNoErrorsFile);

    assertThat(result).isInstanceOf(PmdSummaryResult.class);
    assertThat(result.hasProblems()).isFalse();
    assertThat(result.problems()).isEmpty();
  }

  @Test
  @DisplayName(
      "it should return a checkstyle summary with problems detected if xml has one problem")
  public void it_should_return_checkstyle_summary_with_problems_detected_when_xml_has_one_problems()
      throws IOException {

    Path validNoErrorsFile = RESOURCE_DIR.resolve("valid-one-error-pmd.xml");

    SummaryResult result = parserUnderTest.parse(validNoErrorsFile);

    assertThat(result).isInstanceOf(PmdSummaryResult.class);
    assertThat(result.hasProblems()).isTrue();
    assertThat(result.problems()).containsExactly("UnusedLocalVariable");
  }

  @Test
  @DisplayName(
      "it should return a checkstyle summary with problems detected if xml has multiple problems in different files")
  public void
      it_should_return_checkstyle_summary_with_problems_detected_when_xml_has_multiple_problems_in_different_files()
          throws IOException {

    Path validNoErrorsFile = RESOURCE_DIR.resolve("valid-multiple-errors-diff-files-pmd.xml");

    SummaryResult result = parserUnderTest.parse(validNoErrorsFile);

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
