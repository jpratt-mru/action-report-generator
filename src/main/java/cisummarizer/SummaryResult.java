package cisummarizer;

import java.util.List;

public interface SummaryResult {

  public boolean hasProblems();

  default void addProblem(String attributeValue) {}

  default List<String> problems() {
    return List.of();
  }
}
