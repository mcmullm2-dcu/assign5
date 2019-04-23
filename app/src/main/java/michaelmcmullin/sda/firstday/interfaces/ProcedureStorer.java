package michaelmcmullin.sda.firstday.interfaces;

import java.util.List;
import java.util.Set;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Interface used by classes that need to store/retrieve the data from a single procedure.
 */
public interface ProcedureStorer {

  /**
   * Stores a Procedure instance for later retrieval.
   * @param procedure The Procedure instance to store.
   */
  void StoreProcedure(Procedure procedure);

  /**
   * Retrieves a previously stored Procedure instance.
   * @return Returns an instance of a previously stored instance, or null.
   */
  Procedure GetProcedure();

  /**
   * Stores a list of steps associated with a stored Procedure instance for later retrieval.
   * @param steps The list of Step instances to store.
   */
  void StoreSteps(List<Step> steps);

  /**
   * Retrieves a previously stored list of Step instances associated with a single procedure.
   * @return Returns a list of Step instances, or null.
   */
  List<Step> GetSteps();

  /**
   * Stores a set of distinct tags for a particular Procedure instance for later retrieval.
   * @param tags The set of tags to store.
   */
  void StoreTags(Set<String> tags);

  /**
   * Retrieves a previously stored list of tag strings, used for searching, associated with a
   * particular Procedure instance.
   * @return Returns a set of tag strings, or null.
   */
  Set<String> GetTags();
}
