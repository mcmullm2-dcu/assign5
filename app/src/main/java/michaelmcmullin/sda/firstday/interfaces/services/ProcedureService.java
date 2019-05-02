package michaelmcmullin.sda.firstday.interfaces.services;

import android.support.v4.util.Consumer;
import java.util.ArrayList;
import java.util.List;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.utils.ProcedureFilter;

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

  /**
   * Retrieves a list of procedures matching a given criteria and passes them to a consumer method.
   *
   * @param filter The type of {@link ProcedureFilter} to apply to the query.
   * @param search A search term, which is required for some filters, but ignored in others.
   * @param consumer The method to call with the resulting procedure list.
   * @param error Text to process if there is an error.
   */
  void ListProcedures(ProcedureFilter filter, String search,
      final Consumer<ArrayList<Procedure>> consumer, final String error);

  /**
   * Adds a new procedure along with all its steps and tags to the database, before calling a given
   * function.
   *
   * @param procedure The {@link Procedure} to save to a database.
   * @param steps A list of {@link Step} objects to save to the database.
   * @param tags A list of tag strings to save to the database.
   * @param consumer A method to call after the {@link Procedure} has been added.
   * @param error An error message to process if there is an error.
   */
  void AddProcedure(Procedure procedure, List<Step> steps, List<String> tags,
      final Consumer<Boolean> consumer, final String error);
}
