package cisummarizer;

import static cisummarizer.ReportHeaderAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JunitErrorReportTest {

  @Test
  @DisplayName("when there's an error with the junit error status, the report shows that")
  public void when_status_has_error_report_shows_that() {

    SimpleJunitErrorParser mockedParser = mock(SimpleJunitErrorParser.class);
    when(mockedParser.errors()).thenReturn(List.of("foo"));

    Report report = new JunitErrorReport(mockedParser);

    assertThat(report).hasHeader("[junit error]");
    assertThat(report.summary()).isEqualTo("something bad happened:");
    assertThat(report.details()).containsExactly("|", "|-- foo");
  }

  @Test
  @DisplayName(
      "when there's no errors and no problems in the junit error status, the report shows that")
  public void when_status_has_no_errors_and_no_problems_report_shows_that() {

    SimpleJunitErrorParser mockedParser = mock(SimpleJunitErrorParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems()).thenReturn(List.of());

    Report report = new JunitErrorReport(mockedParser);

    assertThat(report).hasHeader("[junit error]");
    assertThat(report.summary()).isEqualTo("no junit error violations found");
    assertThat(report.details()).isEmpty();
  }

  @Test
  @DisplayName(
      "when there's no errors and one problem in the junit error status, the report shows that")
  public void when_status_has_no_errors_and_one_problem_report_shows_that() {

    SimpleJunitErrorParser mockedParser = mock(SimpleJunitErrorParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(List.of(new Problem("violationType", "violationLocation")));

    Report report = new JunitErrorReport(mockedParser);

    assertThat(report).hasHeader("[junit error]");
    assertThat(report.summary()).isEqualTo("junit error violations found: 1");
    assertThat(report.details()).containsExactly("|", "|-- violationType");
  }

  @Test
  @DisplayName("when there's no errors and two problems in alphabetic order, the report shows that")
  public void when_status_has_no_errors_and_two_problems_in_order_report_shows_that() {

    SimpleJunitErrorParser mockedParser = mock(SimpleJunitErrorParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(
            List.of(
                new Problem("violationTypeA", "violationLocationA"),
                new Problem("violationTypeB", "violationLocationA")));

    Report report = new JunitErrorReport(mockedParser);

    assertThat(report).hasHeader("[junit error]");
    assertThat(report.summary()).isEqualTo("junit error violations found: 2");
    assertThat(report.details()).containsExactly("|", "|-- violationTypeA", "|-- violationTypeB");
  }

  @Test
  @DisplayName(
      "when there's no errors and multiple problems out of order, the report lists them in order")
  public void when_status_has_no_errors_and_multiple_out_of_order_problems_report_show_in_order() {

    SimpleJunitErrorParser mockedParser = mock(SimpleJunitErrorParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(
            List.of(
                new Problem("violationTypeD", "violationLocationD"),
                new Problem("violationTypeC", "violationLocationC"),
                new Problem("violationTypeB", "violationLocationB"),
                new Problem("violationTypeA", "violationLocationA")));

    Report report = new JunitErrorReport(mockedParser);

    assertThat(report).hasHeader("[junit error]");
    assertThat(report.summary()).isEqualTo("junit error violations found: 4");
    assertThat(report.details())
        .containsExactly(
            "|",
            "|-- violationTypeA",
            "|-- violationTypeB",
            "|-- violationTypeC",
            "|-- violationTypeD");
  }
}
