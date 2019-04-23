package michaelmcmullin.sda.firstday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProcedureFormStepListFragment extends Fragment {

  /**
   * A required empty public constructor.
   */
  public ProcedureFormStepListFragment() {
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
    View v = inflater.inflate(R.layout.fragment_procedure_form_step_list, container, false);
    return v;
  }

}
