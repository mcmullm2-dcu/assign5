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

public class ProcedureFormActivity extends AppCompatActivity {

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
   * TODO: may not be necessary for this activity, consider removing and updating the ProcedureFormAdapter code.
   */
  SharedPreferences prefs;

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
    final ProcedureFormAdapter formAdapter = new ProcedureFormAdapter(getSupportFragmentManager(), tabTitles, prefs);

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
}
