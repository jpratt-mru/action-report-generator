package cisummarizer;

import static cisummarizer.ReportHeaderAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CheckstyleReportTest {

  @Test
  @DisplayName("when there's an error with the checkstyle status, the report shows that")
  public void when_status_has_error_report_shows_that() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of("foo"));

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("something bad happened:");
    assertThat(report.details()).containsExactly("|", "|-- foo");
  }

  @Test
  @DisplayName(
      "when there's no errors and no problems in the checkstyle status, the report shows that")
  public void when_status_has_no_errors_and_no_problems_report_shows_that() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems()).thenReturn(List.of());

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("no checkstyle violations found");
    assertThat(report.details()).isEmpty();
  }

  @Test
  @DisplayName(
      "when there's no errors and one problem in the checkstyle status, the report shows that")
  public void when_status_has_no_errors_and_one_problem_report_shows_that() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(List.of(new Problem("violationType", "violationLocation")));

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("checkstyle violations found: 1");
    assertThat(report.details()).containsExactly("|", "|-- violationType");
  }

  @Test
  @DisplayName("when there's no errors and two problems in alphabetic order, the report shows that")
  public void when_status_has_no_errors_and_two_problems_in_order_report_shows_that() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(
            List.of(
                new Problem("violationTypeA", "violationLocationA"),
                new Problem("violationTypeB", "violationLocationA")));

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("checkstyle violations found: 2");
    assertThat(report.details()).containsExactly("|", "|-- violationTypeA", "|-- violationTypeB");
  }

  @Test
  @DisplayName(
      "when there's no errors and two problems of the same type but different locations, the report shows only one")
  public void
      when_status_has_no_errors_and_two_problems_with_same_type_but_different_locations_report_shows_one() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(
            List.of(
                new Problem("violationTypeA", "violationLocationA"),
                new Problem("violationTypeA", "violationLocationB")));

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("checkstyle violations found: 1");
    assertThat(report.details()).containsExactly("|", "|-- violationTypeA");
  }

  @Test
  @DisplayName(
      "when there's no errors and multiple problems out of order, the report lists them in order")
  public void when_status_has_no_errors_and_multiple_out_of_order_problems_report_show_in_order() {

    SimpleCheckstyleParser mockedParser = mock(SimpleCheckstyleParser.class);
    when(mockedParser.errors()).thenReturn(List.of());
    when(mockedParser.problems())
        .thenReturn(
            List.of(
                new Problem("violationTypeD", "violationLocationD"),
                new Problem("violationTypeC", "violationLocationC"),
                new Problem("violationTypeB", "violationLocationB"),
                new Problem("violationTypeA", "violationLocationA")));

    Report report = new CheckstyleReport(mockedParser);

    assertThat(report).hasHeader("[checkstyle]");
    assertThat(report.summary()).isEqualTo("checkstyle violations found: 4");
    assertThat(report.details())
        .containsExactly(
            "|",
            "|-- violationTypeA",
            "|-- violationTypeB",
            "|-- violationTypeC",
            "|-- violationTypeD");
  }
}
