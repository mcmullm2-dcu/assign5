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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.CommentAdapter;
import michaelmcmullin.sda.firstday.dialogs.AddCommentDialogFragment;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.models.Comment;
import michaelmcmullin.sda.firstday.models.User;
import michaelmcmullin.sda.firstday.utils.AppConstants;

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
  private static final String PROCEDURE_KEY = "procedure_id";

  /**
   * Holds a reference to the Firestore database instance.
   */
  private final FirebaseFirestore db = FirebaseFirestore.getInstance();

  /**
   * Holds a reference to the 'comment' collection in Firestore
   */
  private final CollectionReference commentCollection = db.collection("procedure_comment");

  /**
   * A required empty public constructor.
   */
  public CommentsFragment() {
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
    View v = inflater.inflate(R.layout.fragment_procedure_comments, container, false);
    ImageView addCommentButton = v.findViewById(R.id.button_add_comment);
    addCommentButton.setOnClickListener(view -> {
      Activity activity = getActivity();
      if (activity != null) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddCommentDialogFragment commentDialog = AddCommentDialogFragment
            .newInstance(procedureIdGetter.getProcedureId());
        commentDialog.show(fm, "dialog_add_comment");
      }
    });
    return v;
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
    loadComments();
  }

  /**
   * Loads the comments to the screen
   */
  private void loadComments() {
    final ArrayList<Comment> comments = new ArrayList<>();

    // Populate comments from Firestore
    // Based on code from StackOverflow https://stackoverflow.com/a/48807510/5233918
    // Author Alex Mamo https://stackoverflow.com/users/5246885/alex-mamo
    Query query = commentCollection
        .whereEqualTo(PROCEDURE_KEY, procedureIdGetter.getProcedureId())
        .orderBy("created", Direction.DESCENDING);
    query.addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (e != null) {
        Log.w(AppConstants.TAG, "Listen failed.", e);
        return;
      }

      if (queryDocumentSnapshots != null) {
        comments.clear();
        for (DocumentSnapshot comment : queryDocumentSnapshots) {
          String message = comment.getString("message");
          String authorName = comment.getString("author_name");
          String authorImage = comment.getString("author_picture");

          Uri photo = null;
          if (authorImage != null && !authorImage.isEmpty()) {
            photo = new Uri.Builder().path(authorImage).build();
          }

          User author = new User(authorName, photo);
          comments.add(new Comment(author, null, message));
        }
      }
    });

    // Create a CommentAdapter class and tie it in with the comments list.
    final CommentAdapter adapter = new CommentAdapter(getActivity(), comments, Glide.with(this));
    View v = getView();
    if (v != null) {
      ListView listView = getView().findViewById(R.id.list_view_comments);
      if (listView != null) {
        listView.setAdapter(adapter);
      }
    }
  }
}
