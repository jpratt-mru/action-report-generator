package cisummarizer;

/**
 * A problem for the cisummarizer is something that a given tool (javac, junit, checkstyle, pmd,
 * ...) is meant to detect. (Yes, the javac is a bit of a stretch.)
 *
 * <p>Currently, I think I only care about the location of a problem (what file it occurred in) and
 * the type of problem.
 *
 * <p>What's a type? Yeah...that's a pretty generic term. Let me elucidate via example:
 * <li>For javac, the type would just be "compilation". Terribly exciting.
 * <li>For junit, the type could be "test failure", with the location being the test class and test
 *     name involved. It could also be "test error" (with the location being the same as for
 *     failure). I kind of actually cheat here - forced into a corner by the format of xml files
 *     that junit5 generates - the location is just the name of the test class, while the type is a
 *     combination of "test blah:" and name_of_failing_or_errord_test)
 * <li>For checkstyle and pmd, the type would be the name of the violation and the location would be
 *     the file where the violation occurred.
 *
 * @author jpratt
 */
class Problem {

  private String type;
  private String location;

  public Problem(String type) {
    this(type, "");
  }

  public Problem(String type, String location) {
    this.type = type;
    this.location = location;
  }

  public String getLocation() {
    return location;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return String.format("{\"location\": \"%s\", \"type\": \"%s\"}", location, type);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((location == null) ? 0 : location.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Problem other = (Problem) obj;
    if (location == null) {
      if (other.location != null) {
        return false;
      }
    } else if (!location.equals(other.location)) {
      return false;
    }
    if (type == null) {
      if (other.type != null) {
        return false;
      }
    } else if (!type.equals(other.type)) {
      return false;
    }
    return true;
  }
}
