package cisummarizer;

import java.nio.file.Path;

public interface SummarizingParser {

  public SummaryResult parse(Path path);
}
