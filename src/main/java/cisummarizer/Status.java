package cisummarizer;

import java.util.List;

/**
 * A bit of a vague word, but...in this case, if something is a status, we can ask it "what errors
 * do you have?" and "what problems do you have?".
 *
 * <p>What's the difference between an error and a problem? Good question.
 *
 * <p>An error is something unexpectedly bad that happened to the thing that generated the status.
 *
 * <p>For example, in pmd or checkstyle, we'd be talking about a run of the tool missing a config
 * file or with bad flags. Errors in the status of a junit run would indicate anything that
 * generates a junit *error* as opposed to test *failure*.
 *
 * <p>A problem is something undesirable that the tool in question caught - we hope these things
 * aren't there, but we're fine if we see them, because that means our tool is doing its job and we
 * can address that problem.
 *
 * <p>For example, a junit status would call test failures "problems". If your're forgetting your
 * braces with an if-statement and checkstyle doesn't like that, that's a problem.
 *
 * <p>We'll take the Linux philosphy of "no news is good news" - if a status has no errors and no
 * problems, then everything is great as far as we're concerned.
 *
 * <p>For example, a compilation status with no errors and no problems means that everything
 * compiled. A junit status with no errors and no problems would mean that all tests passed.
 *
 * @author jpratt
 */
interface Status {
  List<String> errors();

  List<Problem> problems();
}
