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
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.List;

public class ImageReaderActivity extends AppCompatActivity {

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