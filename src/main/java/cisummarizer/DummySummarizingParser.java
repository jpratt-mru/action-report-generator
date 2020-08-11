package cisummarizer;

import java.nio.file.Path;

public class DummySummarizingParser implements SummarizingParser {

  @Override
  public SummaryResult parse(Path path) {
    return null;
  }
}
