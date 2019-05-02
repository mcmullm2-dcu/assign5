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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.Searchable;
import michaelmcmullin.sda.firstday.utils.CurrentUser;
import michaelmcmullin.sda.firstday.utils.ProcedureFilter;

/**
 * Activity to display any procedures found via a search query. Reference:
 * https://developer.android.com/guide/topics/search/search-dialog#java
 */
public class SearchResultsActivity extends AppCompatActivity
    implements ProcedureFilterGetter, Searchable {

  /**
   * The original term the user is searching for, before it's been processed.
   */
  private String originalSearchTerm;

  /**
   * Called when {@link SearchResultsActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_results);

    // Set activity title to include the search term.
    TextView title = findViewById(R.id.search_results_title);
    String term = getSearchTerm();
    if (originalSearchTerm != null) {
      term = originalSearchTerm;
    }
    String titleFormat = getString(R.string.title_search_results);
    title.setText(String.format(titleFormat, term));

    // Add back button
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
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
      case R.id.menu_item_log_out:
        CurrentUser user = new CurrentUser();
        user.logOut(this);
        return true;
    }
    return false;
  }

  /**
   * Gets the filter to apply to procedure searches.
   *
   * @return Returns the filter to apply to procedure searches.
   */
  @Override
  public ProcedureFilter getFilter() {
    return ProcedureFilter.SEARCH_RESULTS;
  }

  /**
   * Gets the search term to use for queries
   *
   * @return The search term being supplied to this interface
   */
  @Override
  public String getSearchTerm() {
    Intent intent = getIntent();
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String searchTerm = intent.getStringExtra(SearchManager.QUERY).trim();

      originalSearchTerm = searchTerm;

      // Firestore has a limitation which means an array of search terms or wildcards can't be used.
      // As a workaround, split the search term and just search for the first word.
      String[] terms = searchTerm.toLowerCase().split(" ", 2);
      if (terms.length > 0) {
        searchTerm = terms[0];
      }

      return searchTerm;
    }

    return null;
  }
}
