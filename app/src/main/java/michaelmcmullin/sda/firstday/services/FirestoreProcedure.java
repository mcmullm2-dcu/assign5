package michaelmcmullin.sda.firstday.services;

import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
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
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'procedure' collection in Firestore
   */
  private final CollectionReference procedureCollection = db.collection("procedure");

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
      }
    });

    consumer.accept(procedures);
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
