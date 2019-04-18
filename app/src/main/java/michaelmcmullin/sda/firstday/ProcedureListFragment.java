package michaelmcmullin.sda.firstday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import michaelmcmullin.sda.firstday.interfaces.ProcedureFilterGetter;

/**
 * Fragment that displays a list of procedures filtered appropriately.
 */
public class ProcedureListFragment extends Fragment {
  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureFilterGetter procedureFilterGetter;

  /**
   * Holds a reference to the Firestore database instance.
   */
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'procedure' collection in Firestore
   */
  private CollectionReference procedureCollection = db.collection("procedure");

  /**
   * The query to filter procedures by.
   */
  private Query query;

  /**
   * Initialises the fragment's user interface.
   * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
   * @param container If non-null, this is the parent view that the fragment's UI should be attached
   *     to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_procedure_list, container, false);
    return v;
  }

  /**
   * Called when this fragment is first attached to its context.
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureFilterGetter) {
      procedureFilterGetter = (ProcedureFilterGetter)context;
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

    switch(procedureFilterGetter.getFilter()) {
      // TODO: Populated query object
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
    // TODO: Run query
  }
}
