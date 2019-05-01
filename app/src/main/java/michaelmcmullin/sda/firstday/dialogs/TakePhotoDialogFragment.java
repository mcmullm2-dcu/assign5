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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.camerakit.CameraKitView;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.interfaces.BitmapSaver;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Class to handle the display of a camera dialog for taking photos for a {@link Step}.
 */
public class TakePhotoDialogFragment extends DialogFragment {

  /**
   * Reference to the CameraKitView to display the camera input.
   */
  private CameraKitView cameraKitView;

  /**
   * Service to help pass a Bitmap from one entity to another. For example, pass the Bitmap taken by
   * the camera in this DialogFragment and pass it to its parent Fragment.
   */
  private BitmapSaver target;

  /**
   * Empty constructor required.
   */
  public TakePhotoDialogFragment() {
  }

  /**
   * Creates a new instance of the {@link TakePhotoDialogFragment} class.
   *
   * @return Returns a new instance of the {@link TakePhotoDialogFragment} class.
   */
  public static TakePhotoDialogFragment newInstance() {
    return new TakePhotoDialogFragment();
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
    View v = inflater.inflate(R.layout.dialog_take_photo, container);
    cameraKitView = v.findViewById(R.id.camera);
    target = (BitmapSaver) getTargetFragment();

    Button button = v.findViewById(R.id.button_take_photo);
    button.setOnClickListener(view -> cameraKitView.captureImage((cameraKitView, capturedImage) -> {
      // capturedImage contains the image from the CameraKitView.
      Bitmap image = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
      target.PassBitmap(image);
      dismiss();
    }));

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

  /**
   * Overrides the standard Activity.onStart() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  public void onStart() {
    super.onStart();
    cameraKitView.onStart();
  }

  /**
   * Overrides the standard Activity.onResume() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  public void onResume() {
    super.onResume();
    cameraKitView.onResume();
  }

  /**
   * Overrides the standard Activity.onPause() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  public void onPause() {
    cameraKitView.onPause();
    super.onPause();
  }

  /**
   * Overrides the standard Activity.onStop() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  public void onStop() {
    cameraKitView.onStop();
    super.onStop();
  }

  /**
   * Overrides the standard Activity.onRequestPermissionsResult() method to ensure the correct
   * initialisation of the CameraKitView.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
