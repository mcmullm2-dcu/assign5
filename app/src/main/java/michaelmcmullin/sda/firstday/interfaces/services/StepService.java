package michaelmcmullin.sda.firstday.interfaces.services;

import android.support.v4.util.Consumer;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Describes a service that interacts with {@link Step} data.
 */
public interface StepService {

  /**
   * Gets a list of {@link Step} instances and passes them to a consumer method.
   * @param procedureId The unique ID of the {@link Procedure} to retrieve {@link Step}s for.
   * @param consumer The method to call with the resulting list.
   * @param error Text to process if no steps are found.
   */
  void GetSteps(String procedureId, final Consumer<ArrayList<Step>> consumer, final String error);
}
