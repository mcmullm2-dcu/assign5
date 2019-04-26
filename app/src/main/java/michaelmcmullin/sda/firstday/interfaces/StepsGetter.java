package michaelmcmullin.sda.firstday.interfaces;

import java.util.List;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Interface to help retrieve a list of steps.
 */
public interface StepsGetter {

  /**
   * Gets a list of {@link Step} objects related to this instance.
   * @return Returns a list of {@link Step} objects.
   */
  public List<Step> getSteps();
}
