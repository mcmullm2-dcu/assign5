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

package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.camerakit.CameraKitView;
import michaelmcmullin.sda.firstday.interfaces.services.QrService;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CameraKitBase;

/**
 * Activity for capturing an image for QR code recognition.
 */
public class QrReaderActivity extends CameraKitBase {

  /**
   * The service to use for QR codes
   */
  private final QrService qr = Services.QrService;

  /**
   * Called when {@link QrReaderActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qr_reader);
    SetCameraKitView(findViewById(R.id.camera));
  }

  /**
   * Handler for pressing the 'Scan QR Code' button.
   *
   * @param v The button that was pressed to trigger this handler.
   */
  public void takePhoto(View v) {
    Log.i(AppConstants.TAG, "Handler Called: " + v.toString());

    GetCameraKitView().captureImage((CameraKitView cameraKitView, final byte[] capturedImage) -> {
      // Sets up the consumer method that responds to the return value from the QR reading service.
      // This uses a Lambda expression which requires using Java 1.8 in the project. However, it's
      // still compatible with Android API 23 (Source:
      // https://developer.android.com/studio/write/java8-support).
      // The general approach of providing a callback function allows this app to decouple the
      // implementation details (in this case, of reading QR codes) to a separate service, while
      // allowing the service to operate asynchronously, as many Firebase services do.
      Consumer<String> consumer = this::returnIntent;

      // capturedImage contains the image from the CameraKitView. Use the app's QrService to read it.
      qr.ReadQrCode(
          BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length),
          consumer,
          getString(R.string.message_no_qr_code)
      );
    });
  }

  /**
   * Method called when the camera image has been processed.
   *
   * @param value The value picked up by the QR reading service.
   */
  private void returnIntent(String value) {
    Log.i(AppConstants.TAG, "QR code: " + value);
    String error = getString(R.string.message_no_qr_code);

    // If there's a valid QR code, pass it to the procedure activity for viewing.
    if (!value.equals(error)) {
      Intent procedureIntent = new Intent(this, ProcedureActivity.class);
      procedureIntent.putExtra(ProcedureActivity.EXTRA_ID, value);
      startActivity(procedureIntent);
    } else {
      Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
      finish();
    }
  }
}
