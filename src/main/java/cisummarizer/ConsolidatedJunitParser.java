package cisummarizer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsolidatedJunitParser implements Status {

  private SimpleJunitParser errorParser;
  private SimpleJunitParser failureParser;

  private List<String> errors;
  private List<Problem> problems;

  public ConsolidatedJunitParser(Path path) {
    errorParser = new SimpleJunitErrorParser(path);
    failureParser = new SimpleJunitFailureParser(path);

    errors =
        Stream.of(errorParser.errors(), failureParser.errors())
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());

    problems =
        Stream.of(errorParser.problems(), failureParser.problems())
            .flatMap(x -> x.stream())
            .collect(Collectors.toList());
  }

  @Override
  public List<String> errors() {
    return new ArrayList<>(errors);
  }

  @Override
  public List<Problem> problems() {
    return new ArrayList<>(problems);
  }
}
