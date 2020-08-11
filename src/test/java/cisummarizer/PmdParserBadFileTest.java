package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PmdParserBadFileTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources");
  private SummarizingParser parserUnderTest;

  @BeforeEach
  public void setUp() {
    parserUnderTest = new PmdParser();
  }

  @Test
  @DisplayName("it should return a summary error result when given a non-existent file")
  public void it_should_return_a_summary_error_result_when_given_a_nonexistent_file() {
    Path nonexistentFile = RESOURCE_DIR.resolve("blorp");
    SummaryResult result = parserUnderTest.parse(nonexistentFile);

    assertThat(result).isInstanceOf(SummaryErrorResult.class);
  }

  @Test
  @DisplayName("it should return a summary error result when given an empty file")
  public void it_should_return_a_summary_error_result_when_given_an_empty_file() {
    Path emptyFile = RESOURCE_DIR.resolve("empty.xml");
    SummaryResult result = parserUnderTest.parse(emptyFile);

    assertThat(result).isInstanceOf(SummaryErrorResult.class);
  }

  @Test
  @DisplayName("it should return a summary error result when given file is grossly malformed")
  public void it_should_return_a_summary_error_result_when_given_a_grossly_malformed_file() {
    Path malformedFile = RESOURCE_DIR.resolve("malformed.xml");
    SummaryResult result = parserUnderTest.parse(malformedFile);

    assertThat(result).isInstanceOf(SummaryErrorResult.class);
  }

  @Test
  @DisplayName("it should return a summary error result when given file without pmd tags")
  public void it_should_return_a_summary_error_result_when_given_a_file_without_pmd_tags() {
    Path xmlButNotPMD = RESOURCE_DIR.resolve("valid-xml-but-not-pmd.xml");
    SummaryResult result = parserUnderTest.parse(xmlButNotPMD);

    assertThat(result).isInstanceOf(SummaryErrorResult.class);
  }
}
