package michaelmcmullin.sda.firstday.services;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.interfaces.services.StepService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * An implementation of the {@link StepService} that uses Firestore as a database.
 */
public class FirestoreStep implements StepService {

  /**
   * Name of the procedure ID field in the step collection.
   */
  private static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Name of the Step 'name' field in Firestore.
   */
  private static final String STEP_NAME_KEY = "name";

  /**
   * Name of the Step 'description' field in Firestore.
   */
  private static final String STEP_DESCRIPTION_KEY = "description";

  /**
   * Name of the Step 'photo_id' field in Firestore.
   */
  private static final String STEP_PHOTO_ID_KEY = "photo_id";

  /**
   * Name of the ordering field.
   */
  private static final String STEP_ORDER_KEY = "sequence";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'step' collection in Firestore
   */
  private final CollectionReference stepCollection = db.collection("step");

  /**
   * Gets a list of {@link Step} instances and passes them to a consumer method.
   *
   * @param procedureId The unique ID of the {@link Procedure} to retrieve {@link Step}s for.
   * @param consumer The method to call with the resulting list.
   * @param error Text to process if no steps are found.
   */
  @Override
  public void GetSteps(String procedureId, Consumer<ArrayList<Step>> consumer, String error) {
    // Populate steps from Firestore
    // Based on code from StackOverflow https://stackoverflow.com/a/48807510/5233918
    // Author Alex Mamo https://stackoverflow.com/users/5246885/alex-mamo
    Query query = stepCollection
        .whereEqualTo(PROCEDURE_KEY, procedureId)
        .orderBy(STEP_ORDER_KEY);
    query.addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (e != null) {
        Log.w(AppConstants.TAG, error, e);
        return;
      }

      ArrayList<Step> steps = new ArrayList<>();
      if (queryDocumentSnapshots != null) {
        int sequence = 1;
        for (DocumentSnapshot document : queryDocumentSnapshots) {
          String name = document.getString(STEP_NAME_KEY);
          String description = document.getString(STEP_DESCRIPTION_KEY);
          String photoId = document.getString(STEP_PHOTO_ID_KEY);
          Step step = new Step(sequence++, name, description);
          step.setPhotoId(photoId);
          steps.add(step);
        }
      }

      consumer.accept(steps);
    });
  }
}
