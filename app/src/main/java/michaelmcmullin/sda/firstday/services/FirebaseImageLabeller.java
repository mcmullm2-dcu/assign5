package michaelmcmullin.sda.firstday.services;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;
import android.util.Log;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import michaelmcmullin.sda.firstday.interfaces.services.ImageLabelService;
import michaelmcmullin.sda.firstday.models.ImageLabel;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * An {@link ImageLabelService} implementation that uses Firebase's ML-Kit service to label the
 * content of a Bitmap image.
 */
class FirebaseImageLabeller implements ImageLabelService {

  /**
   * Attempts to label any objects found in an image's content.
   *
   * @param bitmap The Bitmap image to analyse for content.
   * @param consumer The method to call with the resulting list of labels.
   * @param error Text to log in the event of an error.
   */
  @Override
  public void ReadImageLabels(Bitmap bitmap, Consumer<List<ImageLabel>> consumer, String error) {
    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
    FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
    // FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();

    labeler.processImage(image)
        .addOnSuccessListener((List<FirebaseVisionImageLabel> labels) -> {
          Log.i(AppConstants.TAG, "Successfully labelled image");

          List<ImageLabel> imageLabels = new ArrayList<>();
          if (labels.size() > 0) {
            // Convert list to a list of ImageLabel objects
            for (FirebaseVisionImageLabel l : labels) {
              ImageLabel il = new ImageLabel(l.getText(), l.getConfidence());
              imageLabels.add(il);
            }

            // Sort the list so most confident labels appear first.
            Collections.sort(imageLabels);

            consumer.accept(imageLabels);
          }
        })
        .addOnFailureListener((@NonNull Exception e) -> {
          Log.i(AppConstants.TAG, error + ": " + e.getMessage());
          consumer.accept(null);
        });
  }
}
