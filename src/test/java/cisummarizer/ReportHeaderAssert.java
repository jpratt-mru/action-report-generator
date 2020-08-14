package cisummarizer;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

/**
 * My very first assertj custom matcher. They grow up so fast.....
 *
 * @author jpratt
 */
class ReportHeaderAssert extends AbstractAssert<ReportHeaderAssert, Report> {

  public ReportHeaderAssert(Report actual) {
    super(actual, ReportHeaderAssert.class);
  }

  public static ReportHeaderAssert assertThat(Report actual) {
    return new ReportHeaderAssert(actual);
  }

  public ReportHeaderAssert hasHeader(String header) {
    isNotNull();

    Assertions.assertThat(actual.header()).as("header").isEqualTo(header);

    return this;
  }
}
