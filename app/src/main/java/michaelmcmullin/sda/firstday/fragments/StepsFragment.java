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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.StepAdapter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.interfaces.StepsSetter;
import michaelmcmullin.sda.firstday.interfaces.services.StepService;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * Fragment that displays the steps involved in a procedure.
 */
public class StepsFragment extends Fragment {

  /**
   * Service used to handle {@link Step} data.
   */
  private final StepService StepService = Services.StepService;

  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureIdGetter procedureIdGetter;

  /**
   * Used to pass a list of steps back to the calling activity.
   */
  private StepsSetter stepsSetter;

  /**
   * A required empty public constructor.
   */
  public StepsFragment() {
    // Required empty public constructor
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
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_procedure_steps, container, false);
  }

  /**
   * Called when this fragment is first attached to its context.
   *
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureIdGetter) {
      procedureIdGetter = (ProcedureIdGetter) context;
      stepsSetter = (StepsSetter) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement ProcedureIdGetter");
    }
  }

  /**
   * Called when this fragment is no longer attached to its activity.
   */
  @Override
  public void onDetach() {
    super.onDetach();
    procedureIdGetter = null;
  }

  /**
   * Use the onStart method to populate the list view.
   */
  @Override
  public void onStart() {
    super.onStart();

    // Get the requested steps
    Consumer<ArrayList<Step>> consumer = this::ProcessSteps;
    StepService.GetSteps(
        procedureIdGetter.getProcedureId(),
        consumer,
        getString(R.string.error_finding_steps)
    );
  }

  /**
   * Process the steps returned from the data service.
   *
   * @param steps The list of steps to process.
   */
  private void ProcessSteps(ArrayList<Step> steps) {
    // Create a StepAdapter class and tie it in with the steps list.
    Activity activity = getActivity();

    if (activity == null) {
      Log.w(AppConstants.TAG, "Can't find activity");
      return;
    }
    final StepAdapter adapter = new StepAdapter(getActivity(), steps, false);
    View v = getView();
    if (v != null) {
      ListView listView = getView().findViewById(R.id.list_view_steps);
      listView.setAdapter(adapter);
    }

    // Pass the steps back to the calling activity
    if (stepsSetter != null) {
      stepsSetter.SetSteps(steps);
    }
  }
}
