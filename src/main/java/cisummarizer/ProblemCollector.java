package cisummarizer;

import java.util.List;

public interface ProblemCollector {
  List<String> problems();

  boolean hasProblems();

  boolean hasError();
}
