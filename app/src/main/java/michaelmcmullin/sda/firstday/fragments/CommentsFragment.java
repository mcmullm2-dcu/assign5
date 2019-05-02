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
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Consumer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.CommentAdapter;
import michaelmcmullin.sda.firstday.dialogs.AddCommentDialogFragment;
import michaelmcmullin.sda.firstday.interfaces.ProcedureIdGetter;
import michaelmcmullin.sda.firstday.interfaces.services.CommentService;
import michaelmcmullin.sda.firstday.models.Comment;
import michaelmcmullin.sda.firstday.services.Services;

/**
 * Fragment that displays comments for a procedure.
 */
public class CommentsFragment extends Fragment {

  /**
   * This is used to get the procedure Id from the calling activity.
   */
  private ProcedureIdGetter procedureIdGetter;

  /**
   * Service used to handle {@link Comment} data.
   */
  private final CommentService CommentService = Services.CommentService;

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

    // Get the required comments
    Consumer<ArrayList<Comment>> consumer = this::loadComments;
    CommentService.GetComments(
        procedureIdGetter.getProcedureId(),
        consumer,
        getString(R.string.error_finding_comments)
    );
  }

  /**
   * Loads the comments to the screen
   */
  private void loadComments(ArrayList<Comment> comments) {
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
