package michaelmcmullin.sda.firstday.interfaces.services;

import android.support.v4.util.Consumer;
import michaelmcmullin.sda.firstday.models.Procedure;

/**
 * Describes a service that interacts with {@link Procedure} data.
 */
public interface ProcedureService {

  /**
   * Finds a {@link Procedure} with a given ID and passes it to a consumer method.
   *
   * @param procedureId The unique ID of the {@link Procedure} to locate.
   * @param consumer The method to call with the resulting {@link Procedure}.
   * @param error Text to process if no {@link Procedure} is found.
   */
  void FindProcedure(String procedureId, final Consumer<Procedure> consumer, final String error);
}
