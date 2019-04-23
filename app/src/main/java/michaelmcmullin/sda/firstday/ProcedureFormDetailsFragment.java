package michaelmcmullin.sda.firstday;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;


/**
 * A Fragment that displays a form for entering a Procedure's details.
 */
public class ProcedureFormDetailsFragment extends Fragment {

  /**
   * An interface implemented by the parent activity to allow this fragment to store and retrieve
   * its details.
   */
  private ProcedureStorer procedureStorer;

  /**
   * A required empty public constructor.
   */
  public ProcedureFormDetailsFragment() {
    // Required empty public constructor
  }

  /**
   * Creates a new instance of the {@link ProcedureFormDetailsFragment} class.
   * @param storer The ProcedureStorer class to associate with this fragment.
   * @return A new {@link ProcedureFormDetailsFragment} instance with its {@link ProcedureStorer}
   * property set.
   */
  public static final ProcedureFormDetailsFragment newInstance(ProcedureStorer storer) {
    ProcedureFormDetailsFragment fragment = new ProcedureFormDetailsFragment();
    fragment.setProcedureStorer(storer);
    return fragment;
  }

  /**
   * Sets the ProcedureStorer activity for this fragment.
   * @param storer The ProcedureStorer class to associate with this fragment.
   */
  public void setProcedureStorer(ProcedureStorer storer) {
    this.procedureStorer = storer;
  }

  /**
   * Initialises the fragment's user interface.
   * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
   * @param container If non-null, this is the parent view that the fragment's UI should be attached
   *     to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
   * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
   *     saved state as given here.
   * @return Return the View for the fragment's UI, or null.
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_procedure_form_details, container, false);
  }

  /**
   * Called when this fragment is first attached to its context.
   * @param context The context to attach this fragment to.
   */
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ProcedureStorer) {
      procedureStorer = (ProcedureStorer)context;
    } else {
      throw new RuntimeException(context.toString() + " must implement ProcedureStorer");
    }
  }

  /**
   * Called when this fragment is no longer attached to its activity.
   */
  @Override
  public void onDetach() {
    super.onDetach();
    procedureStorer = null;
  }
}
