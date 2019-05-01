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

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import michaelmcmullin.sda.firstday.GlideApp;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * A DialogFragment to display a photo belonging to a procedure {@link Step}.
 */
public class ShowPhotoDialogFragment extends DialogFragment {

  /**
   * A reference to Firebase storage
   */
  private final FirebaseStorage storage = FirebaseStorage.getInstance();

  /**
   * A reference to the Firebase storage path.
   */
  private Step step;

  /**
   * Indicates that this dialog should try and retrieve a local copy of the photo.
   */
  private boolean local;

  /**
   * Empty constructor required
   */
  public ShowPhotoDialogFragment() {
  }

  /**
   * Creates a new instance of this dialog.
   *
   * @param step The step that contains the photo.
   * @param local indicates whether to try and display a local copy of the photo.
   * @return An instance of this dialog fragment.
   */
  public static ShowPhotoDialogFragment newInstance(Step step, boolean local) {
    ShowPhotoDialogFragment fragment = new ShowPhotoDialogFragment();
    fragment.step = step;
    fragment.local = local;
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
    View v = inflater.inflate(R.layout.dialog_show_photo, container);

    Button button = v.findViewById(R.id.button_dismiss);
    button.setOnClickListener(view -> dismiss());
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
          photo
              .setImageBitmap(BitmapFactory.decodeFile(step.getLocalPhotoFile().getAbsolutePath()));
        }
      }
    }
  }
}
