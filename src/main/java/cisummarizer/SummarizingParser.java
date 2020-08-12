package cisummarizer;

import java.nio.file.Path;

public interface SummarizingParser {

  SummaryResult parse(Path path);
}
