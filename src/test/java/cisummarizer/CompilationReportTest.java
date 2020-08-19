package cisummarizer;

import static cisummarizer.ReportHeaderAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompilationReportTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/compilation-status");

  private Report report;

  void getReportFor(String fileName) {
    Path pathToFile = RESOURCE_DIR.resolve(fileName);
    report = new CompilationReport(new SimpleCompilationParser(pathToFile));
  }

  @Test
  @DisplayName("when there's an error with the junit result, the report should show that")
  public void compilation_status_report_when_error_with_junit_result() {
    getReportFor("foo.txt");

    assertThat(report).hasHeader("[compilation]");
    assertThat(report.summary()).isEqualTo("something bad happened:");
    assertThat(report.details())
        .containsExactly(
            "|",
            "|-- java.nio.file.NoSuchFileException: src\\test\\resources\\compilation-status\\foo.txt");
  }

  @Test
  @DisplayName("compilation status report for no compilation problems")
  public void compilation_status_report_for_no_compilation_problems() {
    getReportFor("no-errors.txt");

    assertThat(report).hasHeader("[compilation]");
    assertThat(report.summary()).isEqualTo("no compilation problems found");
    assertThat(report.details()).isEmpty();
  }

  @Test
  @DisplayName("compilation status report for one file with one problem")
  public void compilation_status_report_for_one_file_with_one_problem() {
    getReportFor("one-file-one-error.txt");

    assertThat(report).hasHeader("[compilation]");
    assertThat(report.summary()).isEqualTo("compilation problems found: 1");
    assertThat(report.details()).containsExactly("|", "|-- src/main/Main.java");
  }

  @Test
  @DisplayName("compilation status report for one file with multiple problems")
  public void compilation_status_report_for_one_file_with_multiple_problems() {
    getReportFor("one-file-multiple-errors.txt");

    assertThat(report).hasHeader("[compilation]");
    assertThat(report.summary()).isEqualTo("compilation problems found: 1");
    assertThat(report.details()).containsExactly("|", "|-- src/main/DrillUtil.java");
  }

  @Test
  @DisplayName("compilation status report for multiple files with multiple problems")
  public void compilation_status_report_for_multiple_files_with_multiple_problems() {
    getReportFor("multiple-files-multiple-errors.txt");

    assertThat(report).hasHeader("[compilation]");
    assertThat(report.summary()).isEqualTo("compilation problems found: 5");
    assertThat(report.details())
        .containsExactly(
            "|",
            "|-- src/main/DrillUtil.java",
            "|-- src/main/LameUtility.java",
            "|-- src/main/Main.java",
            "|-- src/test/DynamicTest.java",
            "|-- src/test/FooTest.java");
  }
}
