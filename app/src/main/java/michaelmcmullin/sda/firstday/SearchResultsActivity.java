package michaelmcmullin.sda.firstday;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.Searchable;
import michaelmcmullin.sda.firstday.utils.CurrentUser;
import org.w3c.dom.Text;

/**
 * Activity to display any procedures found via a search query.
 * Reference: https://developer.android.com/guide/topics/search/search-dialog#java
 */
public class SearchResultsActivity extends AppCompatActivity
    implements ProcedureFilterGetter, Searchable {

  /**
   * The term to search for.
   */
  private String searchTerm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_results);

    // Set activity title to include the search term.
    TextView title = findViewById(R.id.search_results_title);
    String titleFormat = getString(R.string.title_search_results);
    title.setText(String.format(titleFormat, searchTerm));

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
    getMenuInflater().inflate(R.menu.main_menu, menu);
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
      case R.id.menuitem_log_out:
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
      searchTerm = intent.getStringExtra(SearchManager.QUERY).trim().toLowerCase();
    }
    return searchTerm;
  }
}
