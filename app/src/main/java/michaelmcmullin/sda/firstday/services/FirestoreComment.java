package michaelmcmullin.sda.firstday.services;

import android.net.Uri;
import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.interfaces.services.CommentService;
import michaelmcmullin.sda.firstday.models.Comment;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.User;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * An implementation of the {@link CommentService} that uses Firestore as a database.
 */
public class FirestoreComment implements CommentService {

  /**
   * Name of the procedure ID field in other collections.
   */
  private static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'comment' collection in Firestore
   */
  private final CollectionReference commentCollection = db.collection("procedure_comment");

  /**
   * Gets a list of {@link Comment} instances and passes them to a consumer method.
   *
   * @param procedureId The unique ID of the {@link Procedure} to retrieve {@link Comment}s
   *     for.
   * @param consumer The method to call with the resulting list.
   * @param error Text to process if no comments are found.
   */
  @Override
  public void GetComments(String procedureId, Consumer<ArrayList<Comment>> consumer, String error) {
    final ArrayList<Comment> comments = new ArrayList<>();

    // Populate comments from Firestore
    // Based on code from StackOverflow https://stackoverflow.com/a/48807510/5233918
    // Author Alex Mamo https://stackoverflow.com/users/5246885/alex-mamo
    Query query = commentCollection
        .whereEqualTo(PROCEDURE_KEY, procedureId)
        .orderBy("created", Direction.DESCENDING);
    query.addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (e != null) {
        Log.w(AppConstants.TAG, error, e);
        return;
      }

      if (queryDocumentSnapshots != null) {
        comments.clear();
        for (DocumentSnapshot comment : queryDocumentSnapshots) {
          String message = comment.getString("message");
          String authorName = comment.getString("author_name");
          String authorImage = comment.getString("author_picture");

          Uri photo = null;
          if (authorImage != null && !authorImage.isEmpty()) {
            photo = new Uri.Builder().path(authorImage).build();
          }

          User author = new User(authorName, photo);
          comments.add(new Comment(author, null, message));
        }

        consumer.accept(comments);
      }
    });
  }
}
