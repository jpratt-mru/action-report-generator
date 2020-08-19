package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  private static final Path REPORT_DIR = Paths.get("reports");
  private static final Path RESULTS_DIR = Paths.get("results");

  public static void main(String[] args) {

    new Main().run(args);
  }

  private void run(String[] args) {

    String summaryHeader = "[default header]";

    if (args.length != 0) {
      summaryHeader = args[0];
    }

    Status compilationStatus =
        new SimpleCompilationParser(RESULTS_DIR.resolve("compilation-results.txt"));
    Report compilationReport = new CompilationReport(compilationStatus);

    Status checkstyleStatus =
        new SimpleCheckstyleParser(RESULTS_DIR.resolve("checkstyle-results.xml"));
    Report checkstyleReport = new CheckstyleReport(checkstyleStatus);

    Status pmdStatus = new SimplePmdParser(RESULTS_DIR.resolve("pmd-results.xml"));
    Report pmdReport = new PmdReport(pmdStatus);

    Status junitStatus = new ConsolidatedJunitParser(RESULTS_DIR.resolve("junit-results.xml"));
    Report junitReport = new JunitReport(junitStatus);

    String summary =
        String.join(
            "\n\n",
            summaryHeader,
            compilationReport.allContent(),
            checkstyleReport.allContent(),
            pmdReport.allContent(),
            junitReport.allContent());

    writeReport(summary, "summary-report.txt");
  }

  private void writeReport(String report, String fileName) {
    try {
      Files.write(REPORT_DIR.resolve(fileName), report.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
