package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Disabled("nope")
public class TextFileValidatorTest {

  private static final Predicate<String> DOESNT_MATTER = s -> true;

  private static final Path RESOURCE_DIR = Paths.get("src/test/resources/file-validator");

  @Test
  @DisplayName("a file that doesn't exist has errors and no valid content")
  public void nonexistent_file_has_errors_and_no_content() {

    Path path = RESOURCE_DIR.resolve("file-thats-not-there");
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isNotEmpty();
    assertThat(validator.content()).isEmpty();
  }

  @Test
  @DisplayName("if empty files are valid, validator has no errors but no content")
  public void if_empty_files_valid_validator_has_no_errors_but_no_content() {

    Path path = RESOURCE_DIR.resolve("empty-file.txt");

    Predicate<String> validIfEmpty = s -> s.isEmpty();
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isEmpty();
    assertThat(validator.content()).isEmpty();
  }

  @Test
  @DisplayName("if empty files are not valid, validator has error and no content")
  public void empty_file_has_no_valid_validator() {

    Path path = RESOURCE_DIR.resolve("empty-file.txt");

    Predicate<String> validIfNotEmpty = s -> !s.isEmpty();
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isNotEmpty();
    assertThat(validator.content()).isEmpty();
  }

  @Test
  @DisplayName("a file with one line, but not valid")
  public void file_with_one_line_but_not_valid() {

    Path path = RESOURCE_DIR.resolve("one-line-long-string.txt");
    Predicate<String> validIfShort = s -> s.length() == 1;
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isEmpty();
    assertThat(validator.content()).isEmpty();
  }

  @Test
  @DisplayName("a file with one line, and is valid")
  public void file_with_one_line_and_is_valid() {

    Path path = RESOURCE_DIR.resolve("one-line-long-string.txt");
    Predicate<String> validIfShort = s -> s.length() > 1;
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isEmpty();
    assertThat(validator.content()).containsExactly("too long to be valid");
  }

  @Test
  @DisplayName("a file with many validator, and none are valid")
  public void file_with_many_validator_and_none_are_valid() {

    Path path = RESOURCE_DIR.resolve("many-validator.txt");
    Predicate<String> validIfShort = s -> s.length() > 1000;
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isEmpty();
    assertThat(validator.content()).isEmpty();
  }

  @Test
  @DisplayName("a file with many validator, but only one is valid")
  public void file_with_many_validator_but_only_one_is_valid() {

    Path path = RESOURCE_DIR.resolve("many-validator.txt");
    Predicate<String> validIfShort = s -> s.contains("woot!");
    FileValidator validator = new TextFileValidator(path, DOESNT_MATTER);

    assertThat(validator.errors()).isEmpty();
    assertThat(validator.content()).containsExactly("woot!");
  }
}
