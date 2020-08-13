package cisummarizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

  private static final Path REPORT_DIR = Paths.get("reports");

  public static void main(String[] args) {
    Summarizer summarizer = new Summarizer();

    System.out.println("processing result files...");
    System.out.println("   adding summarizer for pmd...");
    addPmd(summarizer);
    System.out.println("   adding summarizer for checkstyle...");
    addCheckstyle(summarizer);
    System.out.println("   adding summarizer for junit...");
    addJunit(summarizer);

    System.out.println("generating reports...");
    String problemNumberReport = summarizer.problemNumbers();
    writeReport(problemNumberReport, "problem-numbers.json");
    System.out.println("   wrote problem numbers report...");

    String problemSummaryReport = summarizer.problemSummary();
    writeReport(problemSummaryReport, "problem-summary.txt");
    System.out.println("   wrote problem summary report...");

    System.out.println("done");
  }

  private static void writeReport(String report, String fileName) {
    try {
      Files.write(REPORT_DIR.resolve(fileName), report.getBytes());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private static void addJunit(Summarizer summarizer) {
    ProblemCollector junitCollector =
        new JunitProblemCollector(REPORT_DIR.resolve("junit-results.txt"));
    Summary testSummary = new DetailedTestSummary(junitCollector, "junit");

    summarizer.add(testSummary);
  }

  private static void addCheckstyle(Summarizer summarizer) {
    ProblemCollector checkstyleCollector =
        new CheckstyleProblemCollector(REPORT_DIR.resolve("checkstyle-results.xml"));
    Summary checkstyleSummary = new PassFailSummary(checkstyleCollector, "checkstyle");

    summarizer.add(checkstyleSummary);
  }

  private static void addPmd(Summarizer summarizer) {
    ProblemCollector pmdCollector = new PmdProblemCollector(REPORT_DIR.resolve("pmd-results.xml"));
    Summary pmdSummary = new PassFailSummary(pmdCollector, "pmd");

    summarizer.add(pmdSummary);
  }
}
