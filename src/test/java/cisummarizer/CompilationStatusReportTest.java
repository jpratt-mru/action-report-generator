package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompilationStatusReportTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/compilation-status");

  @Test
  @DisplayName("a compilation result with absolutely nothing in it means everything is ok")
  public void compilation_result_with_nothing_means_everything_ok() {
    Report report = new CompilationReport("");

    assertThat(report.summary()).isEqualTo("no compilation errors detected");
    assertThat(report.details()).isEqualTo("");
  }
}

interface Report {

  String summary();

  String details();
}

class CompilationReport implements Report {

  public CompilationReport(String compilation) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public String summary() {

    return "no compilation errors detected";
  }

  @Override
  public String details() {
    return "";
  }
}
