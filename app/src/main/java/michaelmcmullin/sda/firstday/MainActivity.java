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

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.User;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;
import michaelmcmullin.sda.firstday.utils.ProcedureFilter;

public class MainActivity extends AppCompatActivity implements ProcedureFilterGetter {

  /**
   * A reference to the currently signed-in user
   */
  User user = new CurrentUser();

  /**
   * The search view widget to search for procedures.
   */
  SearchView searchView;

  /**
   * Called when {@link MainActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Set up the search dialog
    // Main instructions from https://developer.android.com/guide/topics/search/search-dialog
    // Amendments from: https://stackoverflow.com/a/45536817
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = findViewById(R.id.search_view);
    if (searchManager != null) {
      searchView.setSearchableInfo(
          searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
    }
    searchView.setIconifiedByDefault(false);
    searchView.clearFocus();
    searchView.setFocusable(false);

    // Set up the 'add procedure' button
    FloatingActionButton AddProcedureButton = findViewById(R.id.procedure_add_button);
    AddProcedureButton.setOnClickListener(v -> {
      Intent intent = new Intent(MainActivity.this, ProcedureFormActivity.class);
      startActivity(intent);
    });
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
    getMenuInflater().inflate(R.menu.main_menu, menu);
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
      case R.id.menuitem_log_out:
        user.logOut(this);
        return true;
    }
    return false;
  }

  /**
   * Launches the QR scanner activity.
   */
  public void TakeQrCodePhoto(View v) {
    Log.i(AppConstants.TAG, "Handler Called: " + v.toString());

    Intent qrIntent = new Intent(this, QrReaderActivity.class);
    startActivity(qrIntent);
  }

  /**
   * Launches the image labelling activity.
   */
  public void TakeLabelledImagePhoto(View v) {
    Log.i(AppConstants.TAG, "Handler Called: " + v.toString());

    Intent imageIntent = new Intent(this, ImageReaderActivity.class);
    startActivity(imageIntent);
  }

  /**
   * Gets the filter to apply to procedure searches. For this activity, it returns all procedures
   * belong to the current user (MINE).
   *
   * @return Returns the filter to apply to procedure searches.
   */
  @Override
  public ProcedureFilter getFilter() {
    return ProcedureFilter.MINE;
  }
}
