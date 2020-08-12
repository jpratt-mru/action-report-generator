package cisummarizer;

import java.nio.file.Paths;

public class Main {

  public static void main(String[] args) {
    Summarizer summarizer = new Summarizer();
    System.out.println("starting");
    addPmd(summarizer);
    addCheckstyle(summarizer);
    addJunit(summarizer);
    System.out.println("done");
  }

  private static void addJunit(Summarizer summarizer) {
    ProblemCollector junitCollector = new JunitProblemCollector(Paths.get("pmd-results.xml"));
    Presenter testPresenter = new TestPresenter("junit");
    Summary testSummary = new Summary(junitCollector, testPresenter);

    summarizer.add(testSummary);
  }

  private static void addCheckstyle(Summarizer summarizer) {
    ProblemCollector checkstyleCollector =
        new CheckstyleProblemCollector(Paths.get("checkstyle-results.xml"));
    Presenter checkstylePresenter = new PassFailPresenter("checkstyle");
    Summary checkstyleSummary = new Summary(checkstyleCollector, checkstylePresenter);

    summarizer.add(checkstyleSummary);
  }

  private static void addPmd(Summarizer summarizer) {
    ProblemCollector pmdCollector = new PmdProblemCollector(Paths.get("junit-results.txt"));
    Presenter pmdPresenter = new PassFailPresenter("pmd");
    Summary pmdSummary = new Summary(pmdCollector, pmdPresenter);

    summarizer.add(pmdSummary);
  }
}
