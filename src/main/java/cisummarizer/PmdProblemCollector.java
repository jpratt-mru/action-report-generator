package cisummarizer;

import java.nio.file.Path;

public class PmdProblemCollector extends XmlProblemCollector {

  public PmdProblemCollector(Path path) {
    super(path, "pmd", "violation", "rule");
  }
}
