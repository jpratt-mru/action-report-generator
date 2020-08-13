package cisummarizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Summarizer {

  List<Summary> summaries = new ArrayList<>();

  public void add(Summary summary) {
    summaries.add(summary);
  }

  public String problemNumbers() {
    List<String> props =
        summaries.stream().map(Summary::problemNumber).collect(Collectors.toList());
    return String.format("{%s}", String.join(",", props));
  }

  public String problemSummary() {
    List<String> sections =
        summaries.stream().map(Summary::problemSummary).collect(Collectors.toList());
    return String.join("\n\n-------------------------\n\n", sections);
  }
}
