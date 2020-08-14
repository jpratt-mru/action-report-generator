package cisummarizer;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class InterestingLinesInPlainTextFile implements InterestingLines {

  private List<String> allLines;
  private List<String> interestingLines;
  private List<String> errors;
  private Predicate<String> thisIsInteresting;

  public InterestingLinesInPlainTextFile(Path path, Predicate<String> thisIsInteresting) {

    this.thisIsInteresting = thisIsInteresting;
    errors = new ArrayList<>();
    allLines = allLinesInFile(path);
    interestingLines = interestingLinesInFile();
  }

  private List<String> interestingLinesInFile() {

    return allLines.stream().filter(thisIsInteresting).collect(toList());
  }

  @Override
  public List<String> errors() {
    return errors;
  }

  @Override
  public List<String> lines() {
    return interestingLines;
  }

  private List<String> allLinesInFile(Path pathToFile) {

    try {
      return Files.lines(pathToFile).collect(toList());
    } catch (IOException e) {
      errors.add(e.toString());
      return Collections.emptyList();
    }
  }
}
