package cisummarizer;

import java.util.List;

interface Report {

  String summary();

  List<String> problemDescriptions();

  String header();
}
