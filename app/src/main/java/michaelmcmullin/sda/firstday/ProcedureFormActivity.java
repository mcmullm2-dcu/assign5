package michaelmcmullin.sda.firstday;

import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;

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
  Set<String> workingTags;

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
    for(int i=0; i<tabTitles.length; i++) {
      tabLayout.addTab(tabLayout.newTab());
    }
    // Ensure 'Main' tab is presented to the user
    setTab(MAIN_TAB_INDEX);

    // Create the adapter that will return a fragment for each of the
    // primary sections of the activity.
    final ViewPager pager = findViewById(R.id.pager);
    final ProcedureFormAdapter formAdapter = new ProcedureFormAdapter(getSupportFragmentManager(),
        tabTitles, (ProcedureStorer)this, prefs);

    pager.setAdapter(formAdapter);

    tabLayout.setupWithViewPager(pager);
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
    // TODO: Save this procedure to Firestore
    finish();
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
    // TODO: Save procedure to workingProcedure via SharedPrefs.
  }

  /**
   * Retrieves a previously stored Procedure instance.
   *
   * @return Returns an instance of a previously stored instance, or null.
   */
  @Override
  public Procedure GetProcedure() {
    // TODO: Recreate a Procedure instance from SharedPrefs.
    return null;
  }

  /**
   * Stores a list of steps associated with a stored Procedure instance for later retrieval.
   *
   * @param steps The list of Step instances to store.
   */
  @Override
  public void StoreSteps(List<Step> steps) {
    // TODO: Save steps to workingSteps via SharedPrefs.
  }

  /**
   * Retrieves a previously stored list of Step instances associated with a single procedure.
   *
   * @return Returns a list of Step instances, or null.
   */
  @Override
  public List<Step> GetSteps() {
    // TODO: Recreate the Step instances from SharedPrefs.
    return null;
  }

  /**
   * Stores a set of distinct tags for a particular Procedure instance for later retrieval.
   *
   * @param tags The set of tags to store.
   */
  @Override
  public void StoreTags(Set<String> tags) {
    // TODO: Save tags to workingTags via SharedPrefs.
  }

  /**
   * Retrieves a previously stored list of tag strings, used for searching, associated with a
   * particular Procedure instance.
   *
   * @return Returns a set of tag strings, or null.
   */
  @Override
  public Set<String> GetTags() {
    // TODO: Recreate the Tag instances from SharedPrefs.
    return null;
  }
}
