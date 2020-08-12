package cisummarizer;

import java.nio.file.Path;

/**
 * Collects problems in checkstyle xml reports.
 *
 * <p>A Checkstyle xml report with no errors looks like this:
 *
 * <pre>
 * <?xml version="1.0" encoding="UTF-8"?>
 * <checkstyle version="8.34">
 * <file name="C:\1501-my-drills\_just_vscode_project_in_eclipse\src\main\DrillUtil.java">
 * </file>
 * <file name="C:\1501-my-drills\_just_vscode_project_in_eclipse\src\main\Main.java">
 * </file>
 * </checkstyle>
 * </pre>
 *
 * <p>One with one error looks like this:
 *
 * <pre>
 * <?xml version="1.0" encoding="UTF-8"?>
 * <checkstyle version="8.34">
 * <file name="C:\1501-my-drills\_just_vscode_project_in_eclipse\src\main\DrillUtil.java">
 * </file>
 * <file name="C:\1501-my-drills\_just_vscode_project_in_eclipse\src\main\Main.java">
 * <error line="4" column="9" severity="error" message="Local variable name..." source="com.puppycrawl.tools.checkstyle.checks.naming.LocalVariableNameCheck"/>
 * </file>
 * </checkstyle>
 * </pre>
 *
 * @author jpratt
 */
public class CheckstyleProblemCollector extends XmlProblemCollector {

  public CheckstyleProblemCollector(Path path) {
    super(path, "checkstyle", "error", "source");
  }

  @Override
  protected String formattedProblem(String rawProblem) {
    return rawProblem.replace("com.puppycrawl.tools.checkstyle.checks.", "");
  }
}
