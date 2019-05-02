package michaelmcmullin.sda.firstday.services;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;
import michaelmcmullin.sda.firstday.utils.ProcedureFilter;

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
   * Name of the Procedure 'Description' field in Firestore.
   */
  private static final String TAGS_KEY = "tags";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'procedure' collection in Firestore
   */
  private final CollectionReference procedureCollection = db.collection("procedure");

  /**
   * Holds a reference to the 'step' collection in Firestore
   */
  private final CollectionReference stepCollection = db.collection("step");

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

          Object rawTags = document.get(TAGS_KEY);
          if (rawTags != null && rawTags instanceof ArrayList<?>) {
            ArrayList<String> tags = (ArrayList<String>) rawTags;
            result.setTags(tags);
          }

          consumer.accept(result);
        } else {
          Log.d(AppConstants.TAG, error);
        }
      } else {
        Log.d(AppConstants.TAG, error, task.getException());
      }
    });
  }

  /**
   * Retrieves a list of procedures matching a given criteria and passes them to a consumer method.
   *
   * @param filter The type of {@link ProcedureFilter} to apply to the query.
   * @param search A search term, which is required for some filters, but ignored in others.
   * @param consumer The method to call with the resulting procedure list.
   * @param error Text to process if there is an error.
   */
  @Override
  public void ListProcedures(ProcedureFilter filter, String search,
      Consumer<ArrayList<Procedure>> consumer, String error) {
    Query query = GetQuery(filter, search);

    if (query == null) {
      Log.w(AppConstants.TAG, "Procedure query is null.");
      return;
    }

    final ArrayList<Procedure> procedures = new ArrayList<>();

    // Fetch the procedures that apply to the filtered query
    query.addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (e != null) {
        Log.w(AppConstants.TAG, error, e);
        return;
      }

      if (queryDocumentSnapshots != null) {
        for (DocumentSnapshot document : queryDocumentSnapshots) {
          String name = document.getString(NAME_KEY);
          String description = document.getString(DESCRIPTION_KEY);
          Procedure procedure = new Procedure(name, description);
          procedure.setId(document.getId());

          procedures.add(procedure);
        }

        consumer.accept(procedures);
      }
    });
  }

  /**
   * Adds a new procedure along with all its steps and tags to the database, before calling a given
   * function.
   *
   * @param procedure The {@link Procedure} to save to a database.
   * @param steps A list of {@link Step} objects to save to the database.
   * @param consumer A method to call after the {@link Procedure} has been added, taking a
   *     success argument.
   * @param error An error message to process if there is an error.
   */
  @Override
  public void AddProcedure(Procedure procedure, List<Step> steps, Consumer<Boolean> consumer,
      String error) {
    Map<String, Object> newProcedure = new HashMap<>();
    newProcedure.put("name", procedure.getName());
    newProcedure.put("description", procedure.getDescription());
    newProcedure.put("created", new Date());
    newProcedure.put("is_public", procedure.isPublic());
    newProcedure.put("is_draft", procedure.isDraft());
    newProcedure.put("owner", procedure.getOwner().getId());
    newProcedure.put("tags", procedure.getTags());

    procedureCollection.add(newProcedure).addOnSuccessListener(
        documentReference -> {
          // The procedure has been added. Now add the steps.
          String newId = documentReference.getId();
          Log.i(AppConstants.TAG, "Procedure added, attempting to write steps.");
          WriteBatch batch = db.batch();
          for (Step step : steps) {
            // Add a step to Firestore batch.
            DocumentReference doc = stepCollection.document();
            Map<String, Object> newStep = new HashMap<>();
            newStep.put("sequence", step.getSequence());
            newStep.put("name", step.getName());
            newStep.put("description", step.getDescription());
            newStep.put("procedure_id", newId);
            if (step.hasPhoto()) {
              newStep.put("photo_id", step.getPhotoId());

              // Save the image to Firebase Storage
              step.setCloud(new FirebaseImageStorage());
              step.saveCloudPhoto();
            }
            batch.set(doc, newStep);
          }
          batch.commit().addOnCompleteListener(task -> consumer.accept(true));
        }).addOnFailureListener(e -> {
      Log.w(AppConstants.TAG, error);
      consumer.accept(false);
    });
  }

  /**
   * Sets the query with appropriate filters.
   */
  private Query GetQuery(ProcedureFilter filter, String search) {
    Query query = null;

    if (filter == null) {
      return null;
    }

    CurrentUser user = new CurrentUser();

    switch (filter) {
      case MINE:
        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
          query = procedureCollection.whereEqualTo("owner", user.getUserId()).orderBy("name");
        }
        break;
      case SEARCH_RESULTS:
        query = procedureCollection.whereArrayContains("tags", search).orderBy("name");
        break;
      case PUBLIC:
        query = procedureCollection
            .whereEqualTo("is_public", true).orderBy("name");
        break;
      // TODO: Populate other query filters
      default:
        break;
    }

    return query;
  }
}
