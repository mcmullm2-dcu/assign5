package michaelmcmullin.sda.firstday.interfaces;

import michaelmcmullin.sda.firstday.models.Procedure;

/**
 * Interface to help retrieve a full Procedure
 */
public interface ProcedureGetter {

  /**
   * Gets a Procedure associated with an implementing interface.
   * @return Returns a {@link Procedure} associated with the calling instance.
   */
  Procedure getProcedure();
}
