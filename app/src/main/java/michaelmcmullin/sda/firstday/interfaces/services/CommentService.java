package michaelmcmullin.sda.firstday.interfaces.services;

import android.support.v4.util.Consumer;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.models.Comment;
import michaelmcmullin.sda.firstday.models.Procedure;

/**
 * Describes a service that interacts with {@link Comment} data.
 */
public interface CommentService {
  /**
   * Gets a list of {@link Comment} instances and passes them to a consumer method.
   * @param procedureId The unique ID of the {@link Procedure} to retrieve {@link Comment}s for.
   * @param consumer The method to call with the resulting list.
   * @param error Text to process if no comments are found.
   */
  void GetComments(String procedureId, final Consumer<ArrayList<Comment>> consumer, final String error);
}
