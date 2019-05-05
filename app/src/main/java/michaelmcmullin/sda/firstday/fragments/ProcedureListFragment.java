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
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.ProcedureActivity;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.ProcedureAdapter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.Searchable;
import michaelmcmullin.sda.firstday.interfaces.services.ProcedureService;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * Fragment that displays a list of procedures filtered appropriately.
 */
public class ProcedureListFragment extends Fragment {

  /**
   * Service used to handle {@link Procedure} data.
   */
  private final ProcedureService ProcedureService = Services.ProcedureService;

  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureFilterGetter procedureFilterGetter;

  /**
   * If search results are required, use this field to give access to the calling activity's search
   * term.
   */
  private Searchable searchable;

  /**
   * A required empty public constructor.
   */
  public ProcedureListFragment() {
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
    return inflater.inflate(R.layout.fragment_procedure_list, container, false);
  }

  /**
   * Called when this fragment is first attached to its context.
   *
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    if (context instanceof Searchable) {
      searchable = (Searchable) context;
    }

    if (context instanceof ProcedureFilterGetter) {
      procedureFilterGetter = (ProcedureFilterGetter) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement ProcedureFilterGetter");
    }
  }

  /**
   * Called when this fragment is no longer attached to its activity.
   */
  @Override
  public void onDetach() {
    super.onDetach();
    procedureFilterGetter = null;
  }


  /**
   * Use the onStart method to populate the list view.
   */
  @Override
  public void onStart() {
    super.onStart();

    String search = null;
    if (searchable != null) {
      search = searchable.getSearchTerm();
      if (search == null || search.isEmpty()) {
        return;
      }
    }

    // Get procedure data
    Consumer<ArrayList<Procedure>> consumer = this::ProcessList;
    ProcedureService.ListProcedures(
        procedureFilterGetter.getFilter(),
        search,
        consumer,
        getString(R.string.error_listing_procedures)
    );
  }

  /**
   * Display the list of procedures.
   *
   * @param procedures The list of procedures to display.
   */
  private void ProcessList(ArrayList<Procedure> procedures) {
    // Create a ProcedureAdapter class and tie it in with the procedures list.
    final ProcedureAdapter adapter = new ProcedureAdapter(getActivity(), procedures);

    View v = getView();
    if (v != null) {
      ListView listView = getView().findViewById(R.id.list_view_procedures);
      TextView emptyMessageTextView = getView().findViewById(R.id.empty_procedure_list);
      if (procedures.size() == 0) {
        emptyMessageTextView.setText(getEmptyProceduresMessage());
      }
      listView.setEmptyView(emptyMessageTextView);
      listView.setAdapter(adapter);
      setListViewHeightBasedOnChildren(listView, adapter);

      // Add click event listener to each procedure to open up its details
      listView.setOnItemClickListener((adapterView, view, i, l) -> {
        Procedure clicked = procedures.get(i);
        Intent procedureIntent = new Intent(getActivity(), ProcedureActivity.class);
        procedureIntent.putExtra(ProcedureActivity.EXTRA_ID, clicked.getId());
        startActivity(procedureIntent);
      });
    }
  }

  /**
   * Get an appropriate message to display when there are no procedures listed.
   * @return
   */
  private String getEmptyProceduresMessage() {
    if (procedureFilterGetter != null) {
      switch (procedureFilterGetter.getFilter()) {
        case MINE:
          return getString(R.string.procedure_list_empty_mine);
        case SEARCH_RESULTS:
          return getString(R.string.procedure_list_empty_search);
        default:
          break;
      }
    }

    return getString(R.string.procedure_list_empty_default);
  }

  /**
   * Set the height of a list view based on its content. The purpose of this .
   * @param listView The ListView to change.
   * @param listAdapter The ProcedureAdapter used by the ListView.
   * Adapted from: https://stackoverflow.com/a/9486390/5233918
   */
  public static void setListViewHeightBasedOnChildren(ListView listView, ProcedureAdapter listAdapter) {
    if (listAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = 0;
    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
      totalHeight += listItem.getMeasuredHeight();

      // Original code omitted extra elements that could affect the height. Adding them here.
      totalHeight += listItem.getPaddingTop();
      totalHeight += (listItem.getPaddingBottom() * 2);
      totalHeight += listView.getDividerHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight;
    listView.setLayoutParams(params);
    listView.requestLayout();
  }
}
