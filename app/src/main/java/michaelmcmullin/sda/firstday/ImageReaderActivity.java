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

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.camerakit.CameraKitView;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.List;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CameraKitBase;

/**
 * Activity for capturing an image for labelling, so a photo can be searched by attempting to
 * interpret its contents.
 */
public class ImageReaderActivity extends CameraKitBase {

  /**
   * Called when {@link ImageReaderActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_reader);
    SetCameraKitView(findViewById(R.id.camera));
  }

  /**
   * Handler for photo-taking button.
   *
   * @param v The button that was pressed to trigger this handler.
   */
  public void takePhoto(View v) {
    Log.i(AppConstants.TAG, "Handler Called: " + v.toString());

    GetCameraKitView().captureImage((CameraKitView cameraKitView, final byte[] capturedImage) -> {
      // capturedImage contains the image from the CameraKitView.
      readImage(BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length));
    });
  }

  /**
   * Utility function to take a captured image and interpret any image present. Adapted from code
   * from the Firebase documentation: https://firebase.google.com/docs/ml-kit/android/label-images
   *
   * @param bitmap The image to analyse for labels.
   */
  private void readImage(Bitmap bitmap) {
    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
    FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
    // FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();
    final Bitmap test = image.getBitmapForDebugging();

    labeler.processImage(image)
        .addOnSuccessListener((List<FirebaseVisionImageLabel> labels) -> {
          Log.i(AppConstants.TAG, "Successfully labelled image");

          if (labels.size() > 0) {
            // Only grab one item, maybe the one with the greatest confidence.
            String query = "";
            float confidence = 0.0f;
            for (FirebaseVisionImageLabel label : labels) {
              if (label.getConfidence() > confidence) {
                query = label.getText();
                confidence = label.getConfidence();
              }
            }
            Intent intent = new Intent(ImageReaderActivity.this, SearchResultsActivity.class);
            intent.setAction(Intent.ACTION_SEARCH);
            intent.putExtra(SearchManager.QUERY, query);
            startActivity(intent);
            finish();
          }
        })
        .addOnFailureListener((@NonNull Exception e) -> {
          Log.i(AppConstants.TAG, "Couldn't label image");
          Log.i(AppConstants.TAG, Integer.toString(test.getHeight()));
          finish();
        });
  }
}
