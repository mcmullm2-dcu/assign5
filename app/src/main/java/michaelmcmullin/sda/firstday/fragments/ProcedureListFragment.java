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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.ProcedureActivity;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.ProcedureAdapter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;
import michaelmcmullin.sda.firstday.interfaces.Searchable;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

/**
 * Fragment that displays a list of procedures filtered appropriately.
 */
public class ProcedureListFragment extends Fragment {
  /**
   * Name of the Procedure 'name' field in Firestore.
   */
  private static final String PROCEDURE_NAME_KEY = "name";

  /**
   * Name of the Procedure 'description' field in Firestore.
   */
  private static final String PROCEDURE_DESCRIPTION_KEY = "description";

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
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'procedure' collection in Firestore
   */
  private final CollectionReference procedureCollection = db.collection("procedure");

  /**
   * The query to filter procedures by.
   */
  private Query query;

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
      setQuery();
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
   * Sets the query with appropriate filters.
   */
  private void setQuery() {
    if (procedureFilterGetter == null) {
      return;
    }

    CurrentUser user = new CurrentUser();

    switch (procedureFilterGetter.getFilter()) {
      case MINE:
        if (user.getUserId() != null && !user.getUserId().isEmpty()) {
          query = procedureCollection.whereEqualTo("owner", user.getUserId()).orderBy("name");
        }
        break;
      case SEARCH_RESULTS:
        if (searchable == null) {
          throw new RuntimeException("Searchable interface has not been implemented");
        }
        String searchTerm = searchable.getSearchTerm();
        query = procedureCollection.whereArrayContains("tags", searchTerm).orderBy("name");
        break;
      case PUBLIC:
        query = procedureCollection
            .whereEqualTo("is_public", true).orderBy("name");
        break;
      // TODO: Populate other query filters
      default:
        break;
    }
  }

  /**
   * Use the onStart method to populate the list view.
   */
  @Override
  public void onStart() {
    super.onStart();

    // Only run a query if it exists
    if (query == null) {
      return;
    }

    // Don't run a search query if there's nothing to search for
    if (searchable != null
        && (searchable.getSearchTerm() == null || searchable.getSearchTerm().isEmpty())) {
      return;
    }

    // Fetch the procedures that apply to the filtered query
    query.addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (e != null) {
        Log.w(AppConstants.TAG, "Listen failed.", e);
        return;
      }

      final ArrayList<Procedure> procedures = new ArrayList<>();
      if (queryDocumentSnapshots != null) {
        int sequence = 1;
        for (DocumentSnapshot document : queryDocumentSnapshots) {
          String name = document.getString(PROCEDURE_NAME_KEY);
          String description = document.getString(PROCEDURE_DESCRIPTION_KEY);
          Procedure procedure = new Procedure(name, description);
          procedure.setId(document.getId());

          procedures.add(procedure);
        }
      }

      // Create a ProcedureAdapter class and tie it in with the procedures list.
      final ProcedureAdapter adapter = new ProcedureAdapter(getActivity(), procedures);
      View v = getView();
      if (v != null) {
        ListView listView = getView().findViewById(R.id.list_view_procedures);
        listView.setAdapter(adapter);

        // Add click event listener to each procedure to open up its details
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
          Procedure clicked = procedures.get(i);
          Intent procedureIntent = new Intent(getActivity(), ProcedureActivity.class);
          procedureIntent.putExtra(ProcedureActivity.EXTRA_ID, clicked.getId());
          startActivity(procedureIntent);
        });
      }
    });
  }
}