package cisummarizer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SummarizerTest {
  @Test
  @DisplayName("a newly created one should return an empty result")
  public void a_newly_created_one_should_return_an_empty_result() {
    Summarizer summarizer = new Summarizer();

    assertThat(summarizer.results()).isEmpty();
  }

  @Test
  @DisplayName("you should be able to add a parser to it")
  public void you_should_be_able_to_add_a_parser_to_it() {
    Summarizer summarizer = new Summarizer();
    SummarizingParser parser = new DummySummarizingParser();

    summarizer.add(parser);
  }
}
