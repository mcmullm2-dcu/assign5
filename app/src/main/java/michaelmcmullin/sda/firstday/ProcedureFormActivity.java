/*
 * Copyright (C) 2019 Michael McMullin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package michaelmcmullin.sda.firstday;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.util.Consumer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import michaelmcmullin.sda.firstday.adapters.ProcedureFormAdapter;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.models.User;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

public class ProcedureFormActivity extends AppCompatActivity implements ProcedureStorer {

  /**
   * Service that handles {@link Procedure} data.
   */
  private final ProcedureService ProcedureService = Services.ProcedureService;

  // Indices to each tab
  private final int MAIN_TAB_INDEX = 0;
  private final int STEPS_TAB_INDEX = MAIN_TAB_INDEX + 1;

  /**
   * The layout that contains the tabs themselves.
   */
  private TabLayout tabLayout;

  /**
   * An instance of this app's shared preferences.
   */
  private SharedPreferences prefs;

  /**
   * Stores a working Procedure instance that will eventually get saved to Firestore.
   */
  private Procedure workingProcedure;

  /**
   * Stores a working list of Step instances that will eventually get saved to Firestore.
   */
  private ArrayList<Step> workingSteps;

  /**
   * Stores a working set of tag strings that will eventually get saved to Firestore.
   */
  private HashSet<String> workingTags;

  /**
   * A list of tab content accessors.
   */
  private ArrayList<GetterSetter> workingTabs;

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
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_close);
    }
    setTitle(R.string.title_new_procedure);

    // Set up tabs
    tabLayout = findViewById(R.id.tab_layout);
    String [] tabTitles = getResources().getStringArray(R.array.procedure_form_tabs);
    for (String tabTitle : tabTitles) {
      Log.i(AppConstants.TAG, "Creating tab: " + tabTitle);
      tabLayout.addTab(tabLayout.newTab());
    }
    // Ensure 'Main' tab is presented to the user
    setTab(MAIN_TAB_INDEX);

    // Create the adapter that will return a fragment for each of the
    // primary sections of the activity.
    final ViewPager pager = findViewById(R.id.pager);
    final ProcedureFormAdapter formAdapter = new ProcedureFormAdapter(getSupportFragmentManager(),
        tabTitles, this, null, prefs);

    pager.setAdapter(formAdapter);

    tabLayout.setupWithViewPager(pager);

    // Set up a list of data accessors for each tab.
    workingTabs = new ArrayList<>();
    for (int i = 0; i < tabTitles.length; i++) {
      Fragment tabData = formAdapter.getItem(i);
      if (tabData instanceof GetterSetter) {
        workingTabs.add((GetterSetter) tabData);
      }
    }
  }

  /**
   * Specify the menu to display for this activity.
   *
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
   *
   * @param item The menu item selected by the user
   * @return Returns <code>true</code> if the menu item functionality is being handled here rather
   *     than by the normal system menu processing.
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save_procedure:
        SaveProcedure();
        return true;
    }
    ClearData();
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
      Consumer<Boolean> consumer = this::OnProcedureAdded;
      ProcedureService.AddProcedure(
          workingProcedure,
          workingSteps,
          new ArrayList<>(workingTags),
          consumer,
          getString(R.string.error_failed_adding_procedure)
      );
    }
  }

  /**
   * Method to call after a form has been submitted.
   * @param success Indicates that the {@link Procedure} was added successfully.
   */
  private void OnProcedureAdded(boolean success) {
    if (!success) {
      Log.w(AppConstants.TAG, getString(R.string.error_failed_adding_procedure));
      Toast
          .makeText(ProcedureFormActivity.this, getString(R.string.error_failed_adding_procedure),
              Toast.LENGTH_SHORT).show();
    } else {
      String format = getString(R.string.message_procedure_added);
      String message = String.format(format, workingProcedure.getName());
      Toast.makeText(ProcedureFormActivity.this, message, Toast.LENGTH_SHORT).show();
      ClearData();
      finish();
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
   * Cleans up existing data so it doesn't interfere with the next procedure added
   */
  private void ClearData() {
    workingProcedure = null;
    workingSteps = null;
    workingTags = null;
    workingTabs = null;

    prefs.edit().clear().apply();
  }

  /**
   * Sets the given tab index as the selected tab.
   *
   * @param index The index of the tab to display.
   */
  private void setTab(int index) {
    if (tabLayout != null && tabLayout.getTabCount() > index) {
      Tab tab = tabLayout.getTabAt(index);
      if (tab != null) {
        tab.select();
      }
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
        editor.putString(AppConstants.PREFS_STEP_PHOTO_ID + "." + i, current.getPhotoId());
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
          String photoId = prefs.getString(AppConstants.PREFS_STEP_PHOTO_ID + "." + i, "");
          Step step = new Step(sequence, name, description);
          step.setPhotoId(photoId);
          step.loadLocalPhoto();
          steps.add(step);
        }
      }
      workingSteps = (ArrayList<Step>) steps;
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
