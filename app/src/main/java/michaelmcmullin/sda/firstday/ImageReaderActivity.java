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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.camerakit.CameraKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.List;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * Activity for capturing an image for labelling, so a photo can be searched by attempting to
 * interpret its contents.
 */
public class ImageReaderActivity extends AppCompatActivity {

  /**
   * A reference to the CameraKitView element, where the camera input is displayed.
   */
  private CameraKitView cameraKitView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_reader);
    cameraKitView = findViewById(R.id.camera);
  }

  public void takePhoto(View v) {
    cameraKitView.captureImage(new CameraKitView.ImageCallback() {
      @Override
      public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
        // capturedImage contains the image from the CameraKitView.
        readImage(BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length));
      }
    });
  }

  /**
   * Utility function to take a captured image and interpret any image present
   * @param bitmap
   * Ref: https://firebase.google.com/docs/ml-kit/android/label-images
   */
  public void readImage(Bitmap bitmap) {
    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
    FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
    // FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();
    final Bitmap test = image.getBitmapForDebugging();

    labeler.processImage(image)
        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
          @Override
          public void onSuccess(List<FirebaseVisionImageLabel> labels) {
            Log.i(AppConstants.TAG, "Successfully labelled image");

            if (labels.size() > 0) {
              // Only grab one item, maybe the one with the greatest confidence.
              String query = "";
              float confidence = 0.0f;
              for(FirebaseVisionImageLabel label : labels) {
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
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.i(AppConstants.TAG, "Couldn't label image");
            Log.i(AppConstants.TAG, Integer.toString(test.getHeight()));

            finish();
          }
        });
  }

  @Override
  protected void onStart() {
    super.onStart();
    cameraKitView.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
    cameraKitView.onResume();
  }

  @Override
  protected void onPause() {
    cameraKitView.onPause();
    super.onPause();
  }

  @Override
  protected void onStop() {
    cameraKitView.onStop();
    super.onStop();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
