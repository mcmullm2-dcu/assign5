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

package michaelmcmullin.sda.firstday.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.camerakit.CameraKitView;
import michaelmcmullin.sda.firstday.ImageReaderActivity;
import michaelmcmullin.sda.firstday.R;

/**
 * Base class to use for activities that use a CameraKitView so that boilerplate code can be kept
 * out of the main code base.
 */
public class CameraKitBase extends AppCompatActivity {
  /**
   * A reference to the CameraKitView element, where the camera input is displayed.
   */
  private CameraKitView cameraKitView;

  /**
   * Sets this instance's reference to a CameraKitView object.
   * @param view The CameraKitView to assign to this instance.
   */
  protected void SetCameraKitView(CameraKitView view) {
    this.cameraKitView = view;
  }

  /**
   * Gets this activity's CameraKitView object.
   * @return Returns this activity's CameraKitView object.
   */
  protected CameraKitView GetCameraKitView() {
    return this.cameraKitView;
  }

  /**
   * Overrides the standard Activity.onStart() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onStart() {
    super.onStart();
    if (GetCameraKitView() != null) {
      GetCameraKitView().onStart();
    }
  }

  /**
   * Overrides the standard Activity.onResume() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onResume() {
    super.onResume();
    if (GetCameraKitView() != null) {
      GetCameraKitView().onResume();
    }
  }

  /**
   * Overrides the standard Activity.onPause() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onPause() {
    if (GetCameraKitView() != null) {
      GetCameraKitView().onPause();
    }
    super.onPause();
  }

  /**
   * Overrides the standard Activity.onStop() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onStop() {
    if (GetCameraKitView() != null) {
      GetCameraKitView().onStop();
    }
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
    if (GetCameraKitView() != null) {
      GetCameraKitView().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }
}
