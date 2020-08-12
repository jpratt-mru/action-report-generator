package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PmdProblemCollectorErrorTest {

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/pmd-problem-collector");

  public static Stream<String> filesWithInvalidData() {
    return Stream.of("blorp", "empty.xml", "malformed.xml", "valid-xml-but-not-pmd.xml");
  }

  @Test
  /** Infinitest isn't happy if at least one @Test present. */
  public void forInfinitest() {}

  @ParameterizedTest
  @MethodSource(value = "filesWithInvalidData")
  @DisplayName("it should have no problems but have errors when given path to bad file")
  public void it_should_have_no_problems_but_have_errors_when_given_a_path_to_bad_file(
      String fileName) {
    Path path = RESOURCE_DIR.resolve(fileName);
    ProblemCollector collector = new PmdProblemCollector(path);

    assertThat(collector.problems()).isEmpty();
    assertThat(collector.hasError()).isTrue();
  }
}