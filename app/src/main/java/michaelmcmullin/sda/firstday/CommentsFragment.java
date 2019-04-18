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
import michaelmcmullin.sda.firstday.models.Comment;

/**
 * Fragment that displays comments for a procedure.
 */
public class CommentsFragment extends Fragment {

  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureIdGetter procedureIdGetter;

  /**
   * Name of the procedure ID field in other collections.
   */
  public static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'comment' collection in Firestore
   */
  private CollectionReference commentCollection = db.collection("procedure_comment");

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
    View v = inflater.inflate(R.layout.fragment_procedure_comments, container, false);
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

    // Populate comments from Firestore
    // Based on code from StackOverflow https://stackoverflow.com/a/48807510/5233918
    // Author Alex Mamo https://stackoverflow.com/users/5246885/alex-mamo
    Query query = commentCollection
        .whereEqualTo(PROCEDURE_KEY, procedureIdGetter.getProcedureId())
        .orderBy("created", Direction.DESCENDING);
    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
      @Override
      public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
          @javax.annotation.Nullable FirebaseFirestoreException e) {
        if (e != null) {
          Log.w(AppConstants.TAG, "Listen failed.", e);
          return;
        }

        ArrayList<Comment> comments = new ArrayList<>();
        if (queryDocumentSnapshots != null) {
          for (DocumentSnapshot comment : queryDocumentSnapshots) {
            String message = comment.getString("message");
            String authorId = comment.getString("author");

            // TODO: Find best way to store author information and retrieve it here.

            comments.add(new Comment(null, null, message));
          }
        }

        // Create a CommentAdapter class and tie it in with the comments list.
        final CommentAdapter adapter = new CommentAdapter(getActivity(), comments);
        ListView listView = getView().findViewById(R.id.list_view_comments);
        listView.setAdapter(adapter);
      }
    });
  }
}
