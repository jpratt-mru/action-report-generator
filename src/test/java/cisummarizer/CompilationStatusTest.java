package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CompilationStatusTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/compilation-status");

  @Test
  @DisplayName("a status has no issues if the compilation result file is empty")
  public void compilation_result_with_nothing_means_everything_ok() throws IOException {
    Path pathToFile = RESOURCE_DIR.resolve("empty.txt");
    Status status = new CompilationStatus(pathToFile);

    assertThat(status.problems()).isEmpty();
  }
}

interface Status {

  List<Problem> problems();
}

interface Problem {}

class CompilationStatus implements Status {

  public CompilationStatus(Path pathToFile) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public List<Problem> problems() {
    return List.of();
  }
}
