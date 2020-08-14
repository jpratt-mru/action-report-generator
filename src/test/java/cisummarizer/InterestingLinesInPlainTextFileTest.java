package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InterestingLinesInPlainTextFileTest {

  private static final Predicate<String> DOESNT_MATTER = s -> true;

  private static final Path RESOURCE_DIR =
      Paths.get("src/test/resources/interesting-lines-in-plain-text");

  @Test
  @DisplayName("a file that doesn't exist has errors and no interesting lines")
  public void nonexistent_file_has_errors_and_no_line() {

    Path path = RESOURCE_DIR.resolve("file-thats-not-there");
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, DOESNT_MATTER);

    assertThat(lines.errors()).isNotEmpty();
    assertThat(lines.lines()).isEmpty();
  }

  @Test
  @DisplayName("an empty file has no interesting lines")
  public void empty_file_has_no_interesting_lines() {

    Path path = RESOURCE_DIR.resolve("empty-file.txt");
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, DOESNT_MATTER);

    assertThat(lines.errors()).isEmpty();
    assertThat(lines.lines()).isEmpty();
  }

  @Test
  @DisplayName("a file with one line, but not interesting")
  public void file_with_one_line_but_not_interesting() {

    Path path = RESOURCE_DIR.resolve("one-line-long-string.txt");
    Predicate<String> interestingIfShort = s -> s.length() == 1;
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, interestingIfShort);

    assertThat(lines.errors()).isEmpty();
    assertThat(lines.lines()).isEmpty();
  }

  @Test
  @DisplayName("a file with one line, and is interesting")
  public void file_with_one_line_and_is_interesting() {

    Path path = RESOURCE_DIR.resolve("one-line-long-string.txt");
    Predicate<String> interestingIfShort = s -> s.length() > 1;
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, interestingIfShort);

    assertThat(lines.errors()).isEmpty();
    assertThat(lines.lines()).containsExactly("too long to be interesting");
  }

  @Test
  @DisplayName("a file with many lines, and none are interesting")
  public void file_with_many_lines_and_none_are_interesting() {

    Path path = RESOURCE_DIR.resolve("many-lines.txt");
    Predicate<String> interestingIfShort = s -> s.length() > 1000;
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, interestingIfShort);

    assertThat(lines.errors()).isEmpty();
    assertThat(lines.lines()).isEmpty();
  }

  @Test
  @DisplayName("a file with many lines, but only one is interesting")
  public void file_with_many_lines_but_only_one_is_interesting() {

    Path path = RESOURCE_DIR.resolve("many-lines.txt");
    Predicate<String> interestingIfShort = s -> s.contains("woot!");
    InterestingLines lines = new InterestingLinesInPlainTextFile(path, interestingIfShort);

    assertThat(lines.errors()).isEmpty();
    assertThat(lines.lines()).containsExactly("woot!");
  }
}
