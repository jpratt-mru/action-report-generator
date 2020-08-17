package cisummarizer;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class TextFileValidator implements FileValidator {

  private List<String> allLines;
  private List<String> validLines;
  private List<String> errors;
  private Predicate<String> isValid;

  public TextFileValidator(Path path, Predicate<String> isValid) {

    this.isValid = isValid;
    errors = new ArrayList<>();
    allLines = allLinesInFile(path);
    validateAllLines();
    validLines = validLinesInFile();
  }

  public TextFileValidator(Path pathToFile) {
    this(pathToFile, s -> true);
  }

  private void validateAllLines() {
    //    if (!isValid.test(String.join("\n", allLines))) {
    //      errors.add("contents are not valid");
    //    }
  }

  @Override
  public List<String> errors() {
    return new ArrayList<>(errors);
  }

  @Override
  public List<String> content() {
    return new ArrayList<>(validLines);
  }

  private List<String> allLinesInFile(Path pathToFile) {

    try {
      return Files.lines(pathToFile).collect(toList());
    } catch (IOException e) {
      errors.add(e.toString());
      return Collections.emptyList();
    }
  }

  private List<String> validLinesInFile() {
    return allLines.stream().filter(isValid).collect(toList());
  }
}
