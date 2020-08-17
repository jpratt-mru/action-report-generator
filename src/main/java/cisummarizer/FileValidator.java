package cisummarizer;

import java.util.List;

interface FileValidator {
  List<String> errors();

  List<String> content();
}
