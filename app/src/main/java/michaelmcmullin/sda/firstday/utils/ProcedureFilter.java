package michaelmcmullin.sda.firstday.utils;

/**
 * A variety of methods to filter procedures.
 */
public enum ProcedureFilter {
  /**
   * Displays only my procedures.
   */
  MINE,

  /**
   * Displays only my draft procedures.
   */
  MY_DRAFTS,

  /**
   * Displays only procedures matching a search term
   */
  SEARCH_RESULTS,

  /**
   * Displays procedures that have been shared with me.
   */
  SHARED,

  /**
   * Displays procedures that are marked public.
   */
  PUBLIC,

  /**
   * Displays all procedures that I have access to.
   */
  ALL
}
