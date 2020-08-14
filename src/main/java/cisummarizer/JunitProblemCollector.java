package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class JunitProblemCollector implements ProblemCollector {

  private Path path;
  private List<String> problems;
  private boolean hasError;
  private List<String> junitResultFileContents;

  public JunitProblemCollector(Path path) {
    this.path = path;
    readAllFileLines();
    confirmResultsAreValid();

    problems = problemsFromDocument();
  }

  @Override
  public List<String> problems() {
    return problems;
  }

  @Override
  public boolean hasProblems() {
    return !problems.isEmpty();
  }

  @Override
  public boolean hasError() {
    return hasError;
  }

  private void readAllFileLines() {
    try {
      junitResultFileContents =
          Files.readAllLines(path).stream().filter(s -> !s.isBlank()).collect(Collectors.toList());

    } catch (IOException e) {
      System.err.println("things went south in the JunitProblemSummarizer for path " + path);
      System.err.println(e);
      hasError = true;
    }
  }

  private void confirmResultsAreValid() {
    hasError = hasError || !hasValidTree() || !hasValidSummary();
  }

  // *very* rough check
  private boolean hasValidTree() {
    return junitResultFileContents.contains("+-- JUnit Jupiter [OK]")
        && junitResultFileContents.contains("'-- JUnit Vintage [OK]");
  }

  private boolean hasValidSummary() {
    int lastLineIndex = junitResultFileContents.size() - 1;
    int expectedSummaryHeaderIndex = lastLineIndex - 12;

    String actualSummaryHeader = junitResultFileContents.get(expectedSummaryHeaderIndex);
    String actualLastLine = junitResultFileContents.get(lastLineIndex);

    return actualSummaryHeader.contains("Test run finished")
        && actualLastLine.contains("tests failed");
  }

  private List<String> problemsFromDocument() {
    return hasError
        ? List.of()
        : junitResultFileContents
            .stream()
            .filter(this::isProblem)
            .map(this::cleanedProblem)
            .collect(Collectors.toList());
  }

  private String cleanedProblem(String s) {
    return s;
  }

  private boolean isProblem(String s) {

    return !s.contains("JUnit Jupiter")
        && !s.contains("JUnit Vintage")
        && (s.trim().contains("[X]") || s.trim().endsWith("[OK]"));
  }
}
