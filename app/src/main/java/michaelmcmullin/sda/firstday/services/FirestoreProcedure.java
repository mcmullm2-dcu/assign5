package michaelmcmullin.sda.firstday.services;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * An implementation of the {@link ProcedureService} that uses Firestore as a database.
 */
public class FirestoreProcedure implements ProcedureService {

  /**
   * Name of the Procedure 'Name' field in Firestore.
   */
  private static final String NAME_KEY = "name";

  /**
   * Name of the Procedure 'Description' field in Firestore.
   */
  private static final String DESCRIPTION_KEY = "description";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Finds a {@link Procedure} with a given ID and passes it to a consumer method.
   *
   * @param procedureId The unique ID of the {@link Procedure} to locate.
   * @param consumer The method to call with the resulting {@link Procedure}.
   * @param error Text to process if no {@link Procedure} is found.
   */
  @Override
  public void FindProcedure(String procedureId, Consumer<Procedure> consumer, String error) {
    DocumentReference procedureDoc = db.collection("procedure").document(procedureId);
    // Gets the main procedure details. Adding a Snapshot listener seems like it might be overkill,
    // so this assumes the procedure isn't going to be continually updated as users are watching it.
    // The snapshot listeners will be more appropriate for, say, the comments section.
    procedureDoc.get().addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        DocumentSnapshot document = task.getResult();
        // App sometimes crashed around here when calling the exists() method, as document could
        // sometimes be null, especially when logged out. Adding a null check fixes this.
        if (document != null && document.exists()) {
          // Populate the procedure headings
          String name = document.getString(NAME_KEY);
          String description = document.getString(DESCRIPTION_KEY);
          Procedure result = new Procedure(name, description);
          result.setId(procedureId);
          consumer.accept(result);
        } else {
          Log.d(AppConstants.TAG, error);
        }
      } else {
        Log.d(AppConstants.TAG, error, task.getException());
      }
    });
  }
}
