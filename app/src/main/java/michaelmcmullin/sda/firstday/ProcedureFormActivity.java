package michaelmcmullin.sda.firstday;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.models.User;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

public class ProcedureFormActivity extends AppCompatActivity implements ProcedureStorer {

  // Indices to each tab
  private final int MAIN_TAB_INDEX = 0;
  private final int STEPS_TAB_INDEX = MAIN_TAB_INDEX + 1;
  private final int TAGS_TAB_INDEX = STEPS_TAB_INDEX + 1;

  /**
   * The names of each tab
   */
  String[] tabTitles;

  /**
   * The layout that contains the tabs themselves.
   */
  TabLayout tabLayout;

  /**
   * An instance of this app's shared preferences.
   */
  SharedPreferences prefs;

  /**
   * Stores a working Procedure instance that will eventually get saved to Firestore.
   */
  Procedure workingProcedure;

  /**
   * Stores a working list of Step instances that will eventually get saved to Firestore.
   */
  ArrayList<Step> workingSteps;

  /**
   * Stores a working set of tag strings that will eventually get saved to Firestore.
   */
  HashSet<String> workingTags;

  /**
   * A list of tab content accessors.
   */
  ArrayList<GetterSetter> workingTabs;

  /**
   * Holds a reference to the Firestore database instance.
   */
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'procedure' collection in Firestore
   */
  private CollectionReference procedureCollection = db.collection("procedure");

  /**
   * Holds a reference to the 'step' collection in Firestore
   */
  private CollectionReference stepCollection = db.collection("step");

  /**
   * Called when {@link ProcedureFormActivity} is started, initialising the Activity and inflating
   * the appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_procedure_form);

    prefs = getPreferences(MODE_PRIVATE);

    // Set up toolbar and set a 'close' button in the top-left
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    setTitle(R.string.title_new_procedure);

    // Set up tabs
    tabLayout = findViewById(R.id.tab_layout);
    tabTitles = getResources().getStringArray(R.array.procedure_form_tabs);
    for(String s : tabTitles) {
      tabLayout.addTab(tabLayout.newTab());
    }
    // Ensure 'Main' tab is presented to the user
    setTab(MAIN_TAB_INDEX);

    // Create the adapter that will return a fragment for each of the
    // primary sections of the activity.
    final ViewPager pager = findViewById(R.id.pager);
    final ProcedureFormAdapter formAdapter = new ProcedureFormAdapter(getSupportFragmentManager(),
        tabTitles, this, prefs);

    pager.setAdapter(formAdapter);

    tabLayout.setupWithViewPager(pager);

    // Set up a list of data accessors for each tab.
    workingTabs = new ArrayList<>();
    for (int i=0; i<tabTitles.length; i++) {
      Fragment tabData = formAdapter.getItem(i);
      if (tabData instanceof GetterSetter) {
        workingTabs.add((GetterSetter) tabData);
      }
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
    getMenuInflater().inflate(R.menu.save_menu, menu);
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
    Log.i(AppConstants.TAG, "Starting onOptionsItemSelected method");

    switch (item.getItemId()) {
      case R.id.save_procedure:
        SaveProcedure();
        return true;
    }
    return false;
  }

  /**
   * Saves the new procedure to Firestore.
   */
  private void SaveProcedure() {
    CommitFragmentData();
    GetProcedure();
    GetSteps();
    GetTags();

    if (workingProcedure != null && workingProcedure.isNew()) {
      Map<String, Object> newProcedure = new HashMap<>();
      newProcedure.put("name", workingProcedure.getName());
      newProcedure.put("description", workingProcedure.getDescription());
      newProcedure.put("created", new Date());
      newProcedure.put("is_public", workingProcedure.isPublic());
      newProcedure.put("is_draft", workingProcedure.isDraft());
      newProcedure.put("owner", workingProcedure.getOwner().getId());
      List<String> tags = new ArrayList<String>(workingTags);
      newProcedure.put("tags", tags);

      procedureCollection.add(newProcedure).addOnSuccessListener(
          new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
              // The procedure has been added. Now add the steps.
              String newId = documentReference.getId();
              Log.i(AppConstants.TAG, "Procedure added, attempting to write steps.");
              WriteBatch batch = db.batch();
              for(Step step : workingSteps) {
                DocumentReference doc = stepCollection.document();
                Map<String, Object> newStep = new HashMap<>();
                newStep.put("sequence", step.getSequence());
                newStep.put("name", step.getName());
                newStep.put("description", step.getDescription());
                newStep.put("procedure_id", newId);
                batch.set(doc, newStep);
              }
              batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  String format = getString(R.string.message_procedure_added);
                  String message = String.format(format, workingProcedure.getName());
                  Toast.makeText(ProcedureFormActivity.this, message, Toast.LENGTH_SHORT).show();
                  finish();
                }
              });
            }
          }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Log.w(AppConstants.TAG, "Failed to save data to Firestore");
          Toast.makeText(ProcedureFormActivity.this, getString(R.string.error_failed_adding_procedure), Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  /**
   * Ensure all fragments have committed their data to SharedPreferences.
   */
  private void CommitFragmentData() {
    if (workingTabs != null) {
      for (GetterSetter tab : workingTabs) {
        tab.SetData();
      }
    }
  }

  /**
   * Sets the given tab index as the selected tab.
   * @param index The index of the tab to display.
   */
  private void setTab(int index) {
    if (tabLayout != null && tabLayout.getTabCount() > index) {
      tabLayout.getTabAt(index).select();
    }
  }

  /**
   * Stores a Procedure instance for later retrieval.
   *
   * @param procedure The Procedure instance to store.
   */
  @Override
  public void StoreProcedure(Procedure procedure) {
    if (prefs != null) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putString(AppConstants.PREFS_PROCEDURE_NAME, procedure.getName());
      editor.putString(AppConstants.PREFS_PROCEDURE_DESCRIPTION, procedure.getDescription());
      editor.putString(AppConstants.PREFS_PROCEDURE_CREATED, procedure.getCreated().toString());
      editor.putBoolean(AppConstants.PREFS_PROCEDURE_IS_DRAFT, procedure.isDraft());
      editor.putBoolean(AppConstants.PREFS_PROCEDURE_IS_PUBLIC, procedure.isPublic());
      editor.apply();

      workingProcedure = procedure;
    }
  }

  /**
   * Retrieves a previously stored Procedure instance.
   *
   * @return Returns an instance of a previously stored instance, or null.
   */
  @Override
  public Procedure GetProcedure() {
    if (workingProcedure != null) {
      return workingProcedure;
    }

    if (prefs != null) {
      String name = prefs.getString(AppConstants.PREFS_PROCEDURE_NAME, "");
      String description = prefs.getString(AppConstants.PREFS_PROCEDURE_DESCRIPTION, "");
      SimpleDateFormat date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("us"));
      Date created = new Date();
      try {
        date.parse(prefs.getString(AppConstants.PREFS_PROCEDURE_CREATED, ""));
      } catch (ParseException e) {
        Log.e(AppConstants.TAG, getString(R.string.error_parse_date));
      }
      User user = new User(new CurrentUser());
      boolean isDraft = prefs.getBoolean(AppConstants.PREFS_PROCEDURE_IS_DRAFT, true);
      boolean isPublic = prefs.getBoolean(AppConstants.PREFS_PROCEDURE_IS_PUBLIC, false);
      workingProcedure = new Procedure(name, description, user, created, isPublic, isDraft);
      return workingProcedure;
    }
    return null;
  }

  /**
   * Stores a list of steps associated with a stored Procedure instance for later retrieval.
   *
   * @param steps The list of Step instances to store.
   */
  @Override
  public void StoreSteps(List<Step> steps) {
    if (prefs == null) {
      return;
    }

    SharedPreferences.Editor editor = prefs.edit();

    if (steps != null && steps.size() > 0) {
      editor.putInt(AppConstants.PREFS_STEP_COUNT, steps.size());
      for (int i = 0; i < steps.size(); i++) {
        Step current = steps.get(i);
        editor.putString(AppConstants.PREFS_STEP_NAME + "." + i, current.getName());
        editor.putString(AppConstants.PREFS_STEP_DESCRIPTION + "." + i, current.getDescription());
      }
    } else {
      editor.putInt(AppConstants.PREFS_STEP_COUNT, 0);
    }
    editor.apply();

    workingSteps = (ArrayList<Step>) steps;
  }

  /**
   * Retrieves a previously stored list of Step instances associated with a single procedure.
   *
   * @return Returns a list of Step instances, or null.
   */
  @Override
  public List<Step> GetSteps() {
    if (workingSteps != null && workingSteps.size() > 0) {
      return workingSteps;
    }

    if (prefs != null) {
      List<Step> steps = new ArrayList<>();
      int count = prefs.getInt(AppConstants.PREFS_STEP_COUNT, 0);
      if (count > 0) {
        int sequence;
        for (int i = 0; i < count; i++) {
          sequence = i + 1;
          String name = prefs.getString(AppConstants.PREFS_STEP_NAME + "." + i, "");
          String description = prefs.getString(AppConstants.PREFS_STEP_DESCRIPTION + "." + i, "");
          Step step = new Step(sequence, name, description);
          steps.add(step);
        }
      }
      workingSteps = (ArrayList<Step>)steps;
      return steps;
    }
    return null;
  }

  /**
   * Stores a set of distinct tags for a particular Procedure instance for later retrieval.
   *
   * @param tags The set of tags to store.
   */
  @Override
  public void StoreTags(Set<String> tags) {
    if (prefs == null) {
      return;
    }

    SharedPreferences.Editor editor = prefs.edit();
    editor.putStringSet(AppConstants.PREFS_TAGS, tags);
    editor.apply();

    workingTags = (HashSet<String>) tags;
  }

  /**
   * Retrieves a previously stored list of tag strings, used for searching, associated with a
   * particular Procedure instance.
   *
   * @return Returns a set of tag strings, or null.
   */
  @Override
  public Set<String> GetTags() {
    if (workingTags != null && workingTags.size() > 0) {
      return workingTags;
    }

    if (prefs != null) {
      return prefs.getStringSet(AppConstants.PREFS_TAGS, null);
    }
    return null;
  }
}
