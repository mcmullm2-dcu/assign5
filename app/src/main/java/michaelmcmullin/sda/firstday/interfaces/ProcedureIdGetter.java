package michaelmcmullin.sda.firstday.interfaces;

/**
 * Interface used for classes that need to retrieve a Procedure's unique ID.
 */
public interface ProcedureIdGetter {

  /**
   * Gets the unique ID of the procedure that implementing classes need to be able to access.
   * @return The unique ID of a relevant procedure.
   */
  String getProcedureId();
}
