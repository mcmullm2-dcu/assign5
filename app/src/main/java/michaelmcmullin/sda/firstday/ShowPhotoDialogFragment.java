package michaelmcmullin.sda.firstday;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import michaelmcmullin.sda.firstday.models.Step;

public class ShowPhotoDialogFragment extends DialogFragment {
  /**
   * A reference to Firebase storage
   */
  private FirebaseStorage storage = FirebaseStorage.getInstance();

  /**
   * A reference to the Firebase storage path.
   */
  private Step step;

  /**
   * Indicates that this dialog should try and retrieve a local copy of the photo.
   */
  private boolean local;

  public ShowPhotoDialogFragment() {
    // Empty constructor required.
  }

  public static ShowPhotoDialogFragment newInstance(Step step, boolean local) {
    ShowPhotoDialogFragment fragment = new ShowPhotoDialogFragment();
    fragment.step = step;
    fragment.local = local;
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_show_photo, container);

    Button button = v.findViewById(R.id.button_dismiss);
    button.setOnClickListener(new OnClickListener() {
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
    ImageView photo = view.findViewById(R.id.image_photo);

    if (!local) {
      StorageReference storageRef = storage.getReference();
      StorageReference imageRef = storageRef.child(step.getCloudPath());
      GlideApp.with(this).load(imageRef).into(photo);
    } else {
      if (step.hasPhoto()) {
        photo.setImageBitmap(step.getPhoto());
      } else {
        if (step.getLocalPhotoFile() != null) {
          photo.setImageBitmap(BitmapFactory.decodeFile(step.getLocalPhotoFile().getAbsolutePath()));
        }
      }
    }
  }
}
