package cisummarizer;

import java.util.List;

/**
 * You can ask a report things. Things like:
 * <li>"what header should I show if I print you?"
 * <li>"what is the pithy summary of your contents?"
 * <li>"what are the pertinent descriptions of the problems you'd like to talk about?"
 *
 * @author jpratt
 */
interface Report {

  String header();

  String summary();

  List<String> details();

  String allContent();
}
