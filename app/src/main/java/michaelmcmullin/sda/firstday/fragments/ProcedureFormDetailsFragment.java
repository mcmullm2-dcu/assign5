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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Date;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.User;
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
   * Reference to the 'Description' edit text view.
   */
  private static EditText editDescription;

  /**
   * Reference to the 'Status' spinner.
   */
  private static Spinner spinnerStatus;

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
   * @param storer The ProcedureStorer class to associate with this fragment.
   * @return A new {@link ProcedureFormDetailsFragment} instance with its {@link ProcedureStorer}
   * property set.
   */
  public static final ProcedureFormDetailsFragment newInstance(ProcedureStorer storer) {
    ProcedureFormDetailsFragment fragment = new ProcedureFormDetailsFragment();
    fragment.setProcedureStorer(storer);
    return fragment;
  }

  /**
   * Sets the ProcedureStorer activity for this fragment.
   * @param storer The ProcedureStorer class to associate with this fragment.
   */
  public void setProcedureStorer(ProcedureStorer storer) {
    this.procedureStorer = storer;
  }

  /**
   * Initialises the fragment's user interface.
   * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
   * @param container If non-null, this is the parent view that the fragment's UI should be attached
   *     to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_procedure_form_details, container, false);
    editName = v.findViewById(R.id.edit_text_procedure_form_name);
    editDescription = v.findViewById(R.id.edit_text_procedure_form_description);
    spinnerStatus = v.findViewById(R.id.spinner_procedure_form_status);
    return v;
  }

  /**
   * Called when this fragment is first attached to its context.
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureStorer) {
      procedureStorer = (ProcedureStorer)context;
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
   * @return The date this procedure was created.
   */
  public Date GetCreatedDate() {
    if (createdDate == null) {
      createdDate = new Date();
    }
    return createdDate;
  }

  /**
   * Sets the date this procedure was created.
   * @param date The date this procedure was created.
   */
  public void SetCreatedDate(Date date) {
    this.createdDate = date;
  }

  /**
   * Persists its data in a form that can be read by other classes.
   */
  @Override
  public void SetData() {
    if (editName != null && editDescription != null && spinnerStatus != null) {
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
