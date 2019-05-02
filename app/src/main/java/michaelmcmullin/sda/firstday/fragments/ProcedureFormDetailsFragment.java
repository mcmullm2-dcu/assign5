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

package michaelmcmullin.sda.firstday.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Date;
import java.util.Locale;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.User;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;


/**
 * A Fragment that displays a form for entering a Procedure's details. Activities that contain this
 * fragment must implement the {@link ProcedureStorer} interface.
 */
public class ProcedureFormDetailsFragment extends Fragment implements GetterSetter<Procedure> {

  private static final int SPINNER_DRAFT = 0;
  private static final int SPINNER_PRIVATE = SPINNER_DRAFT + 1;
  private static final int SPINNER_PUBLIC = SPINNER_PRIVATE + 1;

  /**
   * An interface implemented by the parent activity to allow this fragment to store and retrieve
   * its details.
   */
  private ProcedureStorer procedureStorer;

  /**
   * Reference to the 'Name' edit text view.
   */
  private static EditText editName;

  /**
   * Content of the editName EditText
   */
  private String nameValue;

  /**
   * Reference to the 'Description' edit text view.
   */
  private static EditText editDescription;

  /**
   * Content of the editDescription EditText
   */
  private String descriptionValue;

  /**
   * Reference to the 'Status' spinner.
   */
  private static Spinner spinnerStatus;

  /**
   * Selected index of the spinnerStatus Spinner
   */
  private int statusIndex;

  /**
   * The date this procedure was created.
   */
  private Date createdDate;

  /**
   * A required empty public constructor.
   */
  public ProcedureFormDetailsFragment() {
    // Required empty public constructor
  }

  /**
   * Creates a new instance of the {@link ProcedureFormDetailsFragment} class.
   *
   * @param storer The ProcedureStorer class to associate with this fragment.
   * @return A new {@link ProcedureFormDetailsFragment} instance with its {@link ProcedureStorer}
   *     property set.
   */
  public static ProcedureFormDetailsFragment newInstance(ProcedureStorer storer) {
    ProcedureFormDetailsFragment fragment = new ProcedureFormDetailsFragment();
    fragment.setProcedureStorer(storer);
    return fragment;
  }

  /**
   * Sets the ProcedureStorer activity for this fragment.
   *
   * @param storer The ProcedureStorer class to associate with this fragment.
   */
  private void setProcedureStorer(ProcedureStorer storer) {
    this.procedureStorer = storer;
  }

  /**
   * Initialises the fragment's user interface.
   *
   * @param inflater The LayoutInflater object that can be used to inflate any views in the
   *     fragment
   * @param container If non-null, this is the parent view that the fragment's UI should be
   *     attached to. The fragment should not add the view itself, but this can be used to generate
   *     the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a
   *     previous saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_procedure_form_details, container, false);
    editName = v.findViewById(R.id.edit_text_procedure_form_name);
    editDescription = v.findViewById(R.id.edit_text_procedure_form_description);
    spinnerStatus = v.findViewById(R.id.spinner_procedure_form_status);

    if (savedInstanceState != null) {
      RestoreValues(savedInstanceState);
      editName.setText(nameValue);
      editDescription.setText(descriptionValue);
      spinnerStatus.setSelection(statusIndex);
    }

    return v;
  }

  /**
   * Saves the current state of this fragment's elements.
   * @param outState The bundle where the state data is stored.
   */
  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    if (editName != null)
      outState.putString("name", editName.getText().toString());
    if (editDescription != null)
      outState.putString("description", editDescription.getText().toString());
    if (spinnerStatus != null)
      outState.putInt("status", spinnerStatus.getSelectedItemPosition());
  }

  /**
   * Restores values from saved state.
   * @param bundle The data saved during the previous onSaveInstanceState method.
   */
  private void RestoreValues(@Nullable Bundle bundle) {
    if (bundle != null) {
      nameValue = bundle.getString("name");
      descriptionValue = bundle.getString("description");
      statusIndex = bundle.getInt("status");
    }
  }

  /**
   * Called when this fragment is first attached to its context.
   *
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureStorer) {
      procedureStorer = (ProcedureStorer) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement ProcedureStorer");
    }
  }

  /**
   * Called when this fragment is no longer attached to its activity.
   */
  @Override
  public void onDetach() {
    super.onDetach();
    procedureStorer = null;
  }

  /**
   * Gets the date this procedure was created.
   *
   * @return The date this procedure was created.
   */
  private Date GetCreatedDate() {
    if (createdDate == null) {
      createdDate = new Date();
    }
    return createdDate;
  }

  /**
   * Persists its data in a form that can be read by other classes.
   */
  @Override
  public void SetData() {
    if (editName != null && editDescription != null && spinnerStatus != null) {
      Log.i(AppConstants.TAG, "Main creator block.");

      String name = editName.getText().toString();
      String description = editDescription.getText().toString();
      int spinnerIndex = spinnerStatus.getSelectedItemPosition();
      boolean is_draft = spinnerIndex == SPINNER_DRAFT;
      boolean is_public = spinnerIndex == SPINNER_PUBLIC;
      User owner = new User(new CurrentUser());
      Date created = GetCreatedDate();

      Procedure procedure = new Procedure(name, description, owner, created, is_public, is_draft);
      procedureStorer.StoreProcedure(procedure);
    }
  }

  /**
   * Gets the persisted data.
   *
   * @return The persisted data, or null.
   */
  @Override
  public Procedure GetData() {
    return procedureStorer.GetProcedure();
  }
}
