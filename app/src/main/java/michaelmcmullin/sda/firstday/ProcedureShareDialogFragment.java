package michaelmcmullin.sda.firstday;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.utils.CurrentUser;

/**
 * Dialog to enter a user's email address to share this procedure with.
 */
public class ProcedureShareDialogFragment extends DialogFragment {
  /**
   * The procedure to share.
   */
  private Procedure procedure;

  /**
   * The list of steps that comprise this procedure.
   */
  private List<Step> steps;

  public ProcedureShareDialogFragment() {
    // Empty constructor required.
  }

  /**
   * Creates a new instance of this dialog.
   * @param procedure The procedure to share with users.
   * @param steps The list of steps that go with this procedure.
   * @return An instance of this dialog fragment.
   */
  public static ProcedureShareDialogFragment newInstance(Procedure procedure, List<Step> steps) {
    ProcedureShareDialogFragment fragment = new ProcedureShareDialogFragment();
    fragment.procedure = procedure;
    fragment.steps = steps;
    return fragment;
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_share_procedure, container);

    Button button = v.findViewById(R.id.button_dialog_share_procedure);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO: send email or similar
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
