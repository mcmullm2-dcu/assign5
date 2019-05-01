package michaelmcmullin.sda.firstday.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.adapters.StepAdapter;
import michaelmcmullin.sda.firstday.dialogs.TakePhotoDialogFragment;
import michaelmcmullin.sda.firstday.interfaces.BitmapSaver;
import michaelmcmullin.sda.firstday.interfaces.GetterSetter;
import michaelmcmullin.sda.firstday.interfaces.ProcedureStorer;
import michaelmcmullin.sda.firstday.interfaces.StepsSetter;
import michaelmcmullin.sda.firstday.models.Step;
import michaelmcmullin.sda.firstday.utils.AppConstants;


/**
 * A Fragment that displays a form for entering a Procedure's steps. Activities that contain this
 * fragment must implement the {@link ProcedureStorer} interface.
 */
public class ProcedureFormStepFragment extends Fragment
    implements GetterSetter<List<Step>>, OnClickListener, BitmapSaver {

  /**
   * An interface implemented by the parent activity to allow this fragment to store and retrieve
   * its details.
   */
  private ProcedureStorer procedureStorer;

  /**
   * An interface that allows a list of steps to be processed.
   */
  private StepsSetter stepsSetter;

  /**
   * A list of steps already entered.
   */
  private static List<Step> steps;

  /**
   * Reference to the 'Name' edit text view.
   */
  private EditText editName;

  /**
   * Reference to the 'Description' edit text view.
   */
  private EditText editDescription;

  /**
   * Reference to the 'Camera' button.
   */
  private ImageView buttonCamera;

  /**
   * Reference to the 'Gallery' button.
   */
  private ImageView buttonGallery;

  /**
   * Reference to the 'Preview' image.
   */
  private ImageView previewImage;

  /**
   * Reference to the 'Steps' list view.
   */
  private ListView stepsList;

  /**
   * Stores the photo
   */
  private Bitmap photo;

  /**
   * A required empty public constructor.
   */
  public ProcedureFormStepFragment() {
    // Required empty public constructor
  }

  /**
   * Creates a new instance of the {@link ProcedureFormStepFragment} class.
   * @param storer The ProcedureStorer class to associate with this fragment.
   * @return A new {@link ProcedureFormStepFragment} instance with its {@link ProcedureStorer}
   * property set.
   */
  public static final ProcedureFormStepFragment newInstance(ProcedureStorer storer) {
    ProcedureFormStepFragment fragment = new ProcedureFormStepFragment();
    fragment.setProcedureStorer(storer);
    fragment.steps = fragment.GetData();
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
    View v = inflater.inflate(R.layout.fragment_procedure_form_step, container, false);
    editName = v.findViewById(R.id.edit_text_procedure_form_step_name);
    editDescription = v.findViewById(R.id.edit_text_procedure_form_step_description);
    previewImage = v.findViewById(R.id.image_view_preview);

    // Set up the 'Add Step' button
    Button button = v.findViewById(R.id.button_procedure_form_add_step);
    stepsList = v.findViewById(R.id.list_view_steps);
    button.setOnClickListener(this);

    // Set up the picture buttons
    buttonCamera = v.findViewById(R.id.image_view_procedure_form_step_photo);
    buttonGallery = v.findViewById(R.id.image_view_procedure_form_step_gallery);

    buttonCamera.setOnClickListener(this);
    buttonGallery.setOnClickListener(this);

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
   * Saves a newly added step.
   */
  public void SaveStep() {
    if (steps == null) {
      steps = new ArrayList<>();
    }
    int sequence = steps.size() + 1;
    String name = editName.getText().toString();
    String description = editDescription.getText().toString();
    Step step = new Step(sequence, name, description);
    step.generatePhotoId();
    step.setPhoto(photo);
    step.saveLocalPhoto();
    steps.add(step);

    editName.getText().clear();
    editDescription.getText().clear();
    previewImage.setImageResource(android.R.color.transparent);
    photo = null;
  }

  /**
   * Persists its data in a form that can be read by other classes.
   */
  @Override
  public void SetData() {
    procedureStorer.StoreSteps(steps);
  }

  /**
   * Gets the persisted data.
   *
   * @return The persisted data, or null.
   */
  @Override
  public List<Step> GetData() {
    steps = procedureStorer.GetSteps();
    return steps;
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button_procedure_form_add_step:
        SaveStep();
        // Create a StepAdapter class and tie it in with the steps list.
        final StepAdapter adapter = new StepAdapter(getActivity(), (ArrayList<Step>)steps, true);
        stepsList.setAdapter(adapter);
        break;
      case R.id.image_view_procedure_form_step_photo:
        Log.i(AppConstants.TAG, "Taking Photo...");
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TakePhotoDialogFragment photoDialog = TakePhotoDialogFragment.newInstance();
        photoDialog.setTargetFragment(this, 0);
        photoDialog.show(fm, "dialog_take_photo");
        break;
      case R.id.image_view_procedure_form_step_gallery:
        Log.i(AppConstants.TAG, "Loading Gallery...");
        break;
    }
  }

  /**
   * Stores the bitmap taken by the camera.
   *
   * @param bitmap The Bitmap instance to pass.
   */
  @Override
  public void PassBitmap(Bitmap bitmap) {
    previewImage.setImageBitmap(Step.resize(bitmap, 96, 96));
    photo = bitmap;
  }
}
