package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

public class ProcedureActivity extends AppCompatActivity implements ProcedureIdGetter {

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
    procedureDoc = db.collection("procedure").document(getProcedureId());
    populateViews();

    // Add back button
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

  }

  /**
   * Specify the menu to display for this activity.
   * @param menu The options menu to place the menu items into.
   * @return Returns <code>true</code> if the menu is to be displayed.
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.procedure_menu, menu);
    return true;
  }

  /**
   * Responds to a menu item being selected.
   * @param item The menu item selected by the user
   * @return Returns <code>true</code> if the menu item functionality is being handled here rather
   *     than by the normal system menu processing.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menuitem_share:
        // TODO: find the Procedure and Steps and pass them to the dialog.
        FragmentManager fm = getSupportFragmentManager();
        ProcedureShareDialogFragment shareDialog = ProcedureShareDialogFragment.newInstance(null, null);
        shareDialog.show(fm, "dialog_share_procedure");
        return true;
      case R.id.menuitem_log_out:
        CurrentUser user = new CurrentUser();
        user.logOut(this);
        return true;
    }
    return false;
  }

  /**
   * Populate the activity's view with a Procedure's details.
   */
  private void populateViews() {
    // Gets the main procedure details. Adding a Snapshot listener seems like it might be overkill,
    // so this assumes the procedure isn't going to be continually updated as users are watching it.
    // The snapshot listeners will be more appropriate for, say, the comments section.
    procedureDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@android.support.annotation.NonNull Task<DocumentSnapshot> task) {
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
  }

  /**
   * Gets the unique ID of the procedure being displayed by this class.
   * @return Returns the unique ID of this activities procedure.
   */
  @Override
  public String getProcedureId() {
    if (procedureId == null || procedureId.isEmpty()) {
      Intent intent = getIntent();
      procedureId = intent.getStringExtra(EXTRA_ID);
    }
    return procedureId;
  }
}
