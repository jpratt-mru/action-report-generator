package cisummarizer;

import java.util.List;

/**
 * A bit of a vague word, but...in this case, if something is a parser, we can ask it "what errors
 * did you find?" and "what problems did you find?".
 *
 * <p>What's the difference between an error and a problem? Good question!
 *
 * <p>An <b>error</b> is something unexpectedly bad that happened to the parser as it was doing its
 * job. (these will almost always be exceptions of some kind)
 *
 * <p>For example, in pmd or checkstyle, we'd be talking about a run of the tool missing a config
 * file or with bad flags. Missing result files are another common source of errors.
 *
 * <p>A <b>problem</b> is something undesirable that the tool in question caught - we hope these
 * things aren't there, but we're fine if we see them, because that means our tool is doing its job
 * and we can address that problem.
 *
 * <p>For example, a junit parser would call test failures and test errors "problems". If your're
 * forgetting your braces with an if-statement and checkstyle doesn't like that, that's a problem.
 *
 * <p>We'll take the Linux philosphy of "no news is good news" - if a status has no errors and no
 * problems, then everything is great as far as we're concerned.
 *
 * <p>For example, a compilation status with no errors and no problems means that everything
 * compiled. A junit status with no errors and no problems would mean that all tests passed.
 *
 * @author jpratt
 */
interface Parser {
  List<String> errors();

  List<Problem> problems();
}
