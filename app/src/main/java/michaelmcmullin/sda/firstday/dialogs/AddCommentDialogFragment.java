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

package michaelmcmullin.sda.firstday.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

/**
 * Dialog for entering comments
 */
public class AddCommentDialogFragment extends DialogFragment {

  /**
   * Gets a reference to the comment entered by the user.
   */
  private EditText editComment;

  /**
   * Unique ID of the procedure to add comments to.
   */
  private String procedureId;

  public AddCommentDialogFragment() {
    // Empty constructor required.
  }

  public static AddCommentDialogFragment newInstance(String procedureId) {
    AddCommentDialogFragment fragment = new AddCommentDialogFragment();
    fragment.procedureId = procedureId;
    return fragment;
  }

  /**
   * Instantiates this DialogFragment's user interface, called after onCreate.
   *
   * @param inflater The LayoutInflator object that can be used to inflate views within this
   *     DialogFragment.
   * @param container The parent view that the UI should be attached to, if not null.
   * @param savedInstanceState Saved state data that can reconstruct this DialogFragment if
   *     necessary.
   * @return Returns the DialogFragment's UI view.
   */
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_add_comment, container);
    editComment = v.findViewById(R.id.edit_text_comment);

    Button button = v.findViewById(R.id.button_dialog_add_comment);
    button.setOnClickListener(view -> {
      String comment = editComment.getText().toString().trim();
      if (!comment.isEmpty()) {
        CurrentUser user = new CurrentUser();
        Map<String, Object> newComment = new HashMap<>();
        newComment.put("author_id", user.getUserId());
        newComment.put("author_name", user.getDisplayName());
        newComment.put("message", comment);
        newComment.put("procedure_id", procedureId);
        newComment.put("created", new Date());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collection = db.collection("procedure_comment");
        collection.add(newComment).addOnSuccessListener(
            documentReference -> Log.i(AppConstants.TAG, "Comment added")).addOnFailureListener(
            e -> Log.w(AppConstants.TAG, "Comment failed"));

      }
      dismiss();
    });

    Button cancel = v.findViewById(R.id.button_cancel);
    cancel.setOnClickListener(view -> dismiss());
    return v;
  }

  /**
   * Called when onCreateView has returned, but before saved state has been restored, giving an
   * opportunity to initialise any element of this DialogFragment's view hierarchy (but not its
   * parent's).
   *
   * @param view The view returned by onCreateView.
   * @param savedInstanceState Saved state data that can reconstruct this DialogFragment if
   *     necessary.
   */
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().setTitle(R.string.app_name);
  }
}
