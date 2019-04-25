package michaelmcmullin.sda.firstday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import michaelmcmullin.sda.firstday.interfaces.BitmapSaver;
import michaelmcmullin.sda.firstday.models.Comment;
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_add_comment, container);
    editComment = v.findViewById(R.id.edit_text_comment);

    Button button = v.findViewById(R.id.button_dialog_add_comment);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        String comment = editComment.getText().toString().trim();
        if (comment != null && !comment.isEmpty()) {
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
              new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                  Log.i(AppConstants.TAG, "Comment added");
                }
              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Log.w(AppConstants.TAG, "Comment failed");
            }
          });

        }
        dismiss();
      }
    });

    Button cancel = v.findViewById(R.id.button_cancel);
    cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    return v;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().setTitle(R.string.app_name);
  }
}
