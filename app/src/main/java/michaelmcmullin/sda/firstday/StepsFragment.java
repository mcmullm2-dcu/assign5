package michaelmcmullin.sda.firstday;

import android.content.Context;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Fragment that displays the steps involved in a procedure.
 */
public class StepsFragment extends Fragment {

  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureIdGetter procedureIdGetter;

  /**
   * Name of the procedure ID field in other collections.
   */
  public static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Name of the Step 'name' field in Firestore.
   */
  public static final String STEP_NAME_KEY = "name";

  /**
   * Name of the Step 'description' field in Firestore.
   */
  public static final String STEP_DESCRIPTION_KEY = "description";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'step' collection in Firestore
   */
  private CollectionReference stepCollection = db.collection("step");

  /**
   * A required empty public constructor.
   */
  public StepsFragment() {
    // Required empty public constructor
  }

  /**
   * Initilises the fragment's user interface.
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
    View v = inflater.inflate(R.layout.fragment_procedure_steps, container, false);
    return v;
  }

  /**
   * Called when this fragment is first attached to its context.
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureIdGetter) {
      procedureIdGetter = (ProcedureIdGetter)context;
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

    // Populate steps from Firestore
    // Based on code from StackOverflow https://stackoverflow.com/a/48807510/5233918
    // Author Alex Mamo https://stackoverflow.com/users/5246885/alex-mamo
    Query query = stepCollection
        .whereEqualTo(PROCEDURE_KEY, procedureIdGetter.getProcedureId())
        .orderBy("sequence");
    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
          @javax.annotation.Nullable FirebaseFirestoreException e) {
        if (e != null) {
          Log.w(AppConstants.TAG, "Listen failed.", e);
          return;
        }

        ArrayList<Step> steps = new ArrayList<>();
        if (queryDocumentSnapshots != null) {
          int sequence = 1;
          for (DocumentSnapshot document : queryDocumentSnapshots) {
            String name = document.getString(STEP_NAME_KEY);
            String description = document.getString(STEP_DESCRIPTION_KEY);
            Step step = new Step(sequence++, name, description);
            steps.add(step);
          }
        }

        // Create a CommentAdapter class and tie it in with the comments list.
        final StepAdapter adapter = new StepAdapter(getActivity(), steps);
        ListView listView = getView().findViewById(R.id.list_view_steps);
        listView.setAdapter(adapter);
      }
    });
  }
}
