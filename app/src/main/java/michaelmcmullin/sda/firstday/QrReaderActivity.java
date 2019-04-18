package michaelmcmullin.sda.firstday;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class QrReaderActivity extends AppCompatActivity {

  private CameraKitView cameraKitView;
  public static final String EXTRA_QR_CODE = "michaelmcmullin.sda.firstday.QR_CODE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_qr_reader);
    cameraKitView = findViewById(R.id.camera);
  }

  public void takePhoto(View v) {
    cameraKitView.captureImage(new CameraKitView.ImageCallback() {
      @Override
      public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
        // capturedImage contains the image from the CameraKitView.
        readQrCode(BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length));
      }
    });
  }

  /**
   * Utility function to take a captured image and interpret any QR Code present
   * @param bitmap
   * @return
   */
  public void readQrCode(Bitmap bitmap) {
    String code = null;
    FirebaseVisionBarcodeDetectorOptions options =
        new FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build();
    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
    FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
        .getVisionBarcodeDetector(options);
    Task<List<FirebaseVisionBarcode>> barcodeResult = detector.detectInImage(image)
        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
          @Override
          public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
            if (barcodes != null && barcodes.size() > 0) {
              returnIntent(barcodes.get(0).getRawValue());
            } else {
              returnIntent("No QR Code Detected");
            }
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            returnIntent("No QR Code Detected");
          }
        });
  }

  protected void returnIntent(String value) {
    /*Intent intent = new Intent();
    intent.putExtra(EXTRA_QR_CODE, value);
    setResult(Activity.RESULT_OK, intent);
    finish();*/

    Intent procedureIntent = new Intent(this, ProcedureActivity.class);
    procedureIntent.putExtra(ProcedureActivity.EXTRA_ID, value);
    startActivity(procedureIntent);
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
