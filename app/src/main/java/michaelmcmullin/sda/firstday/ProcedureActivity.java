package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import javax.annotation.Nullable;
import michaelmcmullin.sda.firstday.models.Step;

public class ProcedureActivity extends AppCompatActivity {

  /**
   * Used as an Intent Extra tag to pass the procedure ID to this activity.
   */
  public static final String EXTRA_ID = "michaelmcmullin.sda.firstday.ID";

  /**
   * Name of the Procedure 'Name' field in Firestore.
   */
  public static final String NAME_KEY = "name";

  /**
   * Name of the Procedure 'Description' field in Firestore.
   */
  public static final String DESCRIPTION_KEY = "description";

  /**
   * Name of the procedure ID field in other collections.
   */
  public static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Name of the Step 'sequence' field in Firestore.
   */
  public static final String STEP_SEQUENCE_KEY = "sequence";

  /**
   * Name of the Step 'name' field in Firestore.
   */
  public static final String STEP_NAME_KEY = "name";

  /**
   * Name of the Step 'descriptino' field in Firestore.
   */
  public static final String STEP_DESCRIPTION_KEY = "description";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the specific procedure document in Firestore
   */
  private DocumentReference procedureDoc;

  /**
   * Holds a reference to the 'step' collection in Firestore
   */
  private CollectionReference stepCollection = db.collection("step");

  /**
   * The {@link TextView} that displays the name of this procedure.
   */
  private TextView procedureHeading;

  /**
   * The {@link TextView} that displays the procedure's summary.
   */
  private TextView procedureDescription;

  /**
   * The unique ID of the procedure to display
   */
  private String procedureId;

  /**
   * Called when {@link ProcedureActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_procedure);

    // Get the layout view elements
    procedureHeading = findViewById(R.id.text_view_procedure_name);
    procedureDescription = findViewById(R.id.text_view_procedure_description);

    // Ensure we have a reference to the selected procedure document
    if (procedureDoc == null) {
      Intent intent = getIntent();
      procedureId = intent.getStringExtra(EXTRA_ID);
      procedureDoc = db.collection("procedure").document(procedureId);
      Log.d(AppConstants.TAG, "ProcedureActivity.onCreate called with ID = " + procedureId);
    }
    populateViews();
  }

  /**
   * Called after onCreate and being used here to set up event listeners on Firestore.
   */
  @Override
  protected void onStart() {
    super.onStart();

    procedureDoc.addSnapshotListener(this,
      new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
          @Nullable FirebaseFirestoreException e) {
            // App sometimes crashed around here when calling the exists() method, as documentSnapshot
            // could sometimes be null, especially when logged out. Adding a null check fixes this.
            if (documentSnapshot != null && documentSnapshot.exists()) {
              /*String name = documentSnapshot.getString(NAME_KEY);
              String description = documentSnapshot.getString(DESCRIPTION_KEY);

              if (procedureHeading != null) {
                procedureHeading.setText(name);
              }
              if (procedureDescription != null) {
                procedureDescription.setText(description);
              }*/
            } else if (e != null) {
              Log.w(AppConstants.TAG, "Got an exception", e);
            }
          }
      });
  }

  private void populateViews() {
    if (procedureId == null || procedureId.isEmpty()) {
      Intent intent = getIntent();
      procedureId = intent.getStringExtra(EXTRA_ID);
    }

    // Gets the main procedure details. Adding a Snapshot listener seems like it might be overkill,
    // so this assumes the procedure isn't going to be continually updated as users are watching it.
    // The snapshot listeners will be more appropriate for, say, the comments section.
    procedureDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          // App sometimes crashed around here when calling the exists() method, as document could
          // sometimes be null, especially when logged out. Adding a null check fixes this.
          if (document != null && document.exists()) {
            // Populate the procedure headings
            String name = document.getString(NAME_KEY);
            String description = document.getString(DESCRIPTION_KEY);

            if (procedureHeading != null) {
              procedureHeading.setText(name);
            }
            if (procedureDescription != null) {
              procedureDescription.setText(description);
            }
          } else {
            Log.d(AppConstants.TAG, "No such procedure");
          }
        } else {
          Log.d(AppConstants.TAG, "get failed with ", task.getException());
        }
      }
    });

    // Populate steps from Firestore
    Query stepQuery = stepCollection.whereEqualTo(PROCEDURE_KEY, procedureId).orderBy("sequence");
    stepQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (task != null && task.isSuccessful()) {
          final ArrayList<Step> steps = new ArrayList<>();

          for (QueryDocumentSnapshot document : task.getResult()) {
            int sequence = (int)Math.round(document.getDouble(STEP_SEQUENCE_KEY));
            String name = document.getString(STEP_NAME_KEY);
            String description = document.getString(STEP_DESCRIPTION_KEY);
            Step step = new Step(sequence, name, description);
            steps.add(step);
          }

          StepAdapter adapter = new StepAdapter(ProcedureActivity.this, steps);
          ListView listView = findViewById(R.id.step_list);
          listView.setAdapter(adapter);
        }
      }
    });
  }
}
