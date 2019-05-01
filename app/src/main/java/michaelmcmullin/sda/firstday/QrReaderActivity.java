package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.util.Consumer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.camerakit.CameraKitView;
import michaelmcmullin.sda.firstday.interfaces.services.QrService;
import michaelmcmullin.sda.firstday.services.FirebaseQr;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;

public class QrReaderActivity extends AppCompatActivity {

  /**
   * A reference to the CameraKitView element, where the camera input is displayed.
   */
  private CameraKitView cameraKitView;

  /**
   * The service to use for QR codes
   */
  private QrService qr = Services.QrService;

  /**
   * The tag to uniquely identify extra data used by this activity.
   */
  public static final String EXTRA_QR_CODE = "michaelmcmullin.sda.firstday.QR_CODE";

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
    cameraKitView = findViewById(R.id.camera);
  }

  /**
   * Handler for pressing the 'Scan QR Code' button.
   * @param v
   */
  public void takePhoto(View v) {
    cameraKitView.captureImage(new CameraKitView.ImageCallback() {
      @Override
      public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
        // Sets up the consumer method that responds to the return value from the QR reading service.
        // This uses a Lambda expression which requires using Java 1.8 in the project. However, it's
        // still compatible with Android API 23 (Source:
        // https://developer.android.com/studio/write/java8-support).
        // The general approach of providing a callback function allows this app to decouple the
        // implementation details (in this case, of reading QR codes) to a separate service, while
        // allowing the service to operate asynchronously, as many Firebase services do.
        Consumer<String> consumer = (x) -> returnIntent(x);

        // capturedImage contains the image from the CameraKitView.
        qr.ReadQrCode(
            BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length),
            consumer,
            getString(R.string.message_no_qr_code)
        );
      }
    });
  }

  /**
   * Utility function to take a captured image and interpret any QR Code present
   * @param bitmap
   * @return
   */
  /* public void readQrCode(Bitmap bitmap) {
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
              returnIntent(getString(R.string.message_no_qr_code));
            }
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            returnIntent(getString(R.string.message_no_qr_code));
          }
        });
  } */

  /**
   * Method called when the camera image has been processed.
   * @param value The value picked up by the QR reading service.
   */
  protected void returnIntent(String value) {
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

  /**
   * Overrides the standard Activity.onStart() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onStart() {
    super.onStart();
    cameraKitView.onStart();
  }

  /**
   * Overrides the standard Activity.onResume() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onResume() {
    super.onResume();
    cameraKitView.onResume();
  }

  /**
   * Overrides the standard Activity.onPause() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onPause() {
    cameraKitView.onPause();
    super.onPause();
  }

  /**
   * Overrides the standard Activity.onStop() method to ensure the correct initialisation of the
   * CameraKitView.
   */
  @Override
  protected void onStop() {
    cameraKitView.onStop();
    super.onStop();
  }

  /**
   * Overrides the standard Activity.onRequestPermissionsResult() method to ensure the correct
   * initialisation of the CameraKitView.
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
