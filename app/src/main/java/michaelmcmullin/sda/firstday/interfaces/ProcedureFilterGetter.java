package michaelmcmullin.sda.firstday.interfaces;

import michaelmcmullin.sda.firstday.ProcedureFilter;

/**
 * Interface used for classes that need to filter a list of procedures.
 */
public interface ProcedureFilterGetter {
  /**
   * Gets the filter to apply to procedure searches.
   * @return Returns the filter to apply to procedure searches.
   */
  ProcedureFilter getFilter();
}
