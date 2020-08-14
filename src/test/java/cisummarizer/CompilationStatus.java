package cisummarizer;

import static java.util.stream.Collectors.toList;
import static java.util.Comparator.comparing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class CompilationStatus implements Status {

  private List<String> errors;
  private List<Problem> problems;

  public CompilationStatus(Path pathToFile) {
    errors = new ArrayList<>();
    List<String> linesToParse = linesInFile(pathToFile);
    problems = problemsIn(linesToParse);
  }

  //
  // Extracts all lines in the file in the given path;
  // if things blow up, returns an empty list and tosses
  // the exception message into this.errors.
  //
  // I *suspect* this might eventually get pulled out
  // into a helper class, but maybe not.
  //
  private List<String> linesInFile(Path pathToFile) {

    try {
      return Files.lines(pathToFile).collect(toList());
    } catch (IOException e) {
      errors.add(e.getMessage());
      return List.of();
    }
  }

  //
  // We care about *unique* problems in a compilation run - if the
  // same file has multiple errors, we still only want to see
  // a single problem show up with that particular file name.
  //
  // Problems should be in alphabetic order.
  //
  private List<Problem> problemsIn(List<String> linesToParse) {
    List<Problem> problems =
        linesToParse
            .stream()
            .filter(this::isProblem)
            .map(this::buildProblem)
            .distinct()
            .collect(toList());

    problems.sort(comparing(Problem::getLocation));
    return problems;
  }

  private Problem buildProblem(String text) {
    String fileNotCompiling = extractedFileFrom(text);
    return new Problem("compilation", fileNotCompiling);
  }

  //
  // Errors in compilation look like this in Windows:
  //    src\main\Main.java:4: error: cannot find symbol
  //
  // We'll extract the path to the file causing the error
  // by looking for the colon before the line number (4 in the
  // example) and then "correcting" the slashes from the
  // Windows backslash to the Linux forward slash.
  //
  // I thought this might be a good idea, since we'll be running
  // the cisummarizer on a Linux GitHub box most of the time...
  // but not all the time.
  //
  private String extractedFileFrom(String text) {
    int indexOfLineNumber = text.indexOf(":");
    String filePath = text.substring(0, indexOfLineNumber);
    String slashCorrectedFilePath = filePath.replace("\\", "/");
    return slashCorrectedFilePath;
  }

  //
  // Compilations from the command line always(?) provide a message
  // like this:
  //   src\main\Main.java:4: error: cannot find symbol
  //
  // The characters ": error:" appear in these lines, so we'll use
  // that to our advantage.
  //
  private boolean isProblem(String s) {
    return s.contains(": error:");
  }

  @Override
  public List<Problem> problems() {
    return new ArrayList<>(problems); // defensive copy
  }
}
