package michaelmcmullin.sda.firstday.interfaces;

import java.util.List;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Interface to help process a list of steps.
 */
public interface StepsSetter {

  /**
   * Set a list of steps for further processing.
   * @param steps The list of steps to process.
   */
  void SetSteps(List<Step> steps);
}
