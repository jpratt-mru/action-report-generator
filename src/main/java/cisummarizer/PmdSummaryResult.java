package cisummarizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PmdSummaryResult implements SummaryResult {

  HashSet<String> problems = new HashSet<>();

  @Override
  public boolean hasProblems() {
    return !problems.isEmpty();
  }

  @Override
  public void addProblem(String attributeValue) {
    problems.add(attributeValue);
  }

  @Override
  public List<String> problems() {
    List<String> results = new ArrayList<>();
    problems.forEach(e -> results.add(e));
    results.sort(String::compareTo);
    return results;
  }
}
