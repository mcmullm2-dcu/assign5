package michaelmcmullin.sda.firstday;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.User;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

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
   * A value to hold the scanned in QR code
   */
  String qrCode;

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

    // Demo profile picture. If it's null, show default image (or colour in this case).
    CircleImageView profile = findViewById(R.id.profile_image);
    Uri profile_picture = user.getPhoto();
    if (profile_picture != null) {
      Glide.with(this).load(profile_picture).into(profile);
    } else {
      profile.setImageResource(R.drawable.ic_default_profile_picture);
    }

    // Demo user display name
    TextView msg = findViewById(R.id.welcome);
    msg.setText(user.getDisplayName());

    // Set up the search dialog
    // Main instructions from https://developer.android.com/guide/topics/search/search-dialog
    // Amendments from: https://stackoverflow.com/a/45536817
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    searchView = (SearchView) findViewById(R.id.search_view);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchResultsActivity.class)));
    searchView.setIconifiedByDefault(false);

    // Set up the 'add procedure' button
    FloatingActionButton AddProcedureButton = findViewById(R.id.procedure_add_button);
    AddProcedureButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ProcedureFormActivity.class);
        startActivity(intent);
      }
    });
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
    Log.i(AppConstants.TAG, "Starting onOptionsItemSelected method");

    switch (item.getItemId()) {
      case R.id.menuitem_log_out:
        user.logOut(this);
        return true;
    }
    return false;
  }

  /**
   * Launches the QR scanner activity.
   * @param view
   */
  public void TakePhoto(View view) {
    Intent qrIntent = new Intent(this, QrReaderActivity.class);
    qrIntent.putExtra(QrReaderActivity.EXTRA_QR_CODE, qrCode);
    // startActivityForResult(qrIntent, AppConstants.REQUEST_TAKE_QR_PHOTO);
    startActivity(qrIntent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == AppConstants.REQUEST_TAKE_QR_PHOTO) {
      if (resultCode == MainActivity.RESULT_OK) {
        qrCode = intent.getStringExtra(QrReaderActivity.EXTRA_QR_CODE);
        if (qrCode != null && !qrCode.isEmpty() && qrCode != getString(R.string.message_no_qr_code)) {
          Intent procedureIntent = new Intent(this, ProcedureActivity.class);
          procedureIntent.putExtra(ProcedureActivity.EXTRA_ID, qrCode);
          startActivity(procedureIntent);
        } else {
          Toast.makeText(this, getString(R.string.message_no_qr_code), Toast.LENGTH_LONG).show();
        }
      }
    }
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
