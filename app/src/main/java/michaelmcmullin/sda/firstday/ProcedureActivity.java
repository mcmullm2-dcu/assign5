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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Consumer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import michaelmcmullin.sda.firstday.dialogs.AddCommentDialogFragment;
import michaelmcmullin.sda.firstday.dialogs.ProcedureShareDialogFragment;
import michaelmcmullin.sda.firstday.interfaces.ProcedureGetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.interfaces.StepsSetter;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

public class ProcedureActivity extends AppCompatActivity implements ProcedureIdGetter,
    ProcedureGetter, StepsSetter {

  /**
   * Used as an Intent Extra tag to pass the procedure ID to this activity.
   */
  public static final String EXTRA_ID = "michaelmcmullin.sda.firstday.ID";

  /**
   * The service to use for handling {@link Procedure} data.
   */
  private final ProcedureService procedureService = Services.ProcedureService;

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
   * A list of steps associated with this procedure.
   */
  private List<Step> steps;

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

    // Get the requested procedure
    Consumer<Procedure> consumer = this::populateViews;
    procedureService.FindProcedure(
        getProcedureId(),
        consumer,
        getString(R.string.error_finding_procedure)
    );

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
    getMenuInflater().inflate(R.menu.procedure_menu, menu);
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
      case R.id.menu_item_share:
        // Find the Procedure and Steps and pass them to the dialog.
        FragmentManager fm = getSupportFragmentManager();
        ProcedureShareDialogFragment shareDialog = ProcedureShareDialogFragment
            .newInstance(getProcedure(), steps);
        shareDialog.show(fm, "dialog_share_procedure");
        return true;
      case R.id.menu_item_log_out:
        CurrentUser user = new CurrentUser();
        user.logOut(this);
        return true;
      case R.id.menu_item_comment:
        FragmentManager fmComment = getSupportFragmentManager();
        AddCommentDialogFragment commentDialog = AddCommentDialogFragment
            .newInstance(getProcedureId());
        commentDialog.show(fmComment, "dialog_add_comment");
        return true;
    }
    return false;
  }

  /**
   * Populate the activity's view with a Procedure's details.
   */
  private void populateViews(Procedure procedure) {
    if (procedure == null) {
      Toast.makeText(this, getString(R.string.message_unrecognised_qr_code), Toast.LENGTH_SHORT).show();
      finish();
      return;
    }

    if (procedureHeading != null) {
      String name = procedure.getName();
      if (procedure.isDraft()) {
        name += " (" + getString(R.string.draft) + ")";
      }
      procedureHeading.setText(name);
    }
    if (procedureDescription != null) {
      procedureDescription.setText(procedure.getDescription());
    }
  }

  /**
   * Gets the unique ID of the procedure being displayed by this class.
   *
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

  /**
   * Gets a Procedure associated with an implementing interface.
   *
   * @return Returns a {@link Procedure} associated with the calling instance.
   */
  @Override
  public Procedure getProcedure() {
    String procedureId = getProcedureId();
    String name = null;
    String description = null;

    if (procedureHeading != null) {
      name = procedureHeading.getText().toString();
    }

    if (procedureDescription != null) {
      description = procedureDescription.getText().toString();
    }

    if (procedureId == null || name == null || description == null) {
      return null;
    }

    Procedure p = new Procedure(name, description);
    p.setId(procedureId);

    return p;
  }

  /**
   * Set a list of steps for further processing.
   *
   * @param steps The list of steps to process.
   */
  @Override
  public void SetSteps(List<Step> steps) {
    this.steps = steps;
  }
}
