package michaelmcmullin.sda.firstday.interfaces;

/**
 * Interface used for classes that need to retrieve a search term.
 */
public interface Searchable {

  /**
   * Gets the search term to use for queries
   * @return The search term being supplied to this interface
   */
  String getSearchTerm();
}
