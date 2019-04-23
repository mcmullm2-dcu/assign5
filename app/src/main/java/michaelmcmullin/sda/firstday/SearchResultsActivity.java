package michaelmcmullin.sda.firstday;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity to display any procedures found via a search query.
 * Reference: https://developer.android.com/guide/topics/search/search-dialog#java
 */
public class SearchResultsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_results);

    // Get the intent, verify the action and get the query
    Intent intent = getIntent();
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
      search(query);
    }
  }

  private void search(String query) {
    // TODO: populate list
  }
}
