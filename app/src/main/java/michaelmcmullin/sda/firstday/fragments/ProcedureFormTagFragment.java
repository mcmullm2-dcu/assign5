package michaelmcmullin.sda.firstday.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;
import me.gujun.android.taggroup.TagGroup;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;

/**
 * A Fragment that displays a form for entering a Procedure's tags. Activities that contain this
 * fragment must implement the {@link ProcedureStorer} interface.
 */
public class ProcedureFormTagFragment extends Fragment implements GetterSetter<Set<String>> {
  /**
   * An interface implemented by the parent activity to allow this fragment to store and retrieve
   * its details.
   */
  private ProcedureStorer procedureStorer;

  /**
   * A reference to the TagGroup widget
   */
  private static TagGroup tagGroup;

  /**
   * An array of tag strings used to populate the TagGroup
   */
  private String[] tags = {};

  /**
   * A required empty public constructor.
   */
  public ProcedureFormTagFragment() {
    // Required empty public constructor
  }

  /**
   * Creates a new instance of the {@link ProcedureFormTagFragment} class.
   * @param storer The ProcedureStorer class to associate with this fragment.
   * @return A new {@link ProcedureFormTagFragment} instance with its {@link ProcedureStorer}
   * property set.
   */
  public static final ProcedureFormTagFragment newInstance(ProcedureStorer storer) {
    ProcedureFormTagFragment fragment = new ProcedureFormTagFragment();
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
    View v = inflater.inflate(R.layout.fragment_procedure_form_tags, container, false);
    tagGroup = v.findViewById(R.id.tag_group);
    GetData();

    return v;
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

  /**
   * Persists its data in a form that can be read by other classes.
   */
  @Override
  public void SetData() {
    Set<String> data = new HashSet<>();
    if (tagGroup != null) {
      for (String s : tagGroup.getTags()) {
        data.add(s);
      }
    }
    procedureStorer.StoreTags(data);
  }

  /**
   * Gets the persisted data.
   *
   * @return The persisted data, or null.
   */
  @Override
  public Set<String> GetData() {
    Set<String> savedTags = procedureStorer.GetTags();
    if (savedTags == null) {
      return null;
    }
    int size = savedTags.size();
    tags = new String[size];
    tags = savedTags.toArray(tags);
    if (tagGroup != null) {
      tagGroup.setTags(tags);
    }
    return savedTags;
  }
}