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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.util.Consumer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.camerakit.CameraKitView;
import java.util.List;
import michaelmcmullin.sda.firstday.interfaces.services.ImageLabelService;
import michaelmcmullin.sda.firstday.models.ImageLabel;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.utils.CameraKitBase;

/**
 * Activity for capturing an image for labelling, so a photo can be searched by attempting to
 * interpret its contents.
 */
public class ImageReaderActivity extends CameraKitBase {

  /**
   * The service to use for image labelling
   */
  private final ImageLabelService labeller = Services.ImageLabelService;

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
      // Sets up the consumer method that responds to the return value from the image labelling
      // service. This uses a Lambda expression which requires using Java 1.8 in the project.
      // However, it's still compatible with Android API 23 (Source:
      // https://developer.android.com/studio/write/java8-support). The general approach of
      // providing a callback function allows this app to decouple the implementation details (in
      // this case, of reading image labels) to a separate service, while allowing the service to
      // operate asynchronously, as many Firebase services do.
      Consumer<List<ImageLabel>> consumer = this::returnIntent;

      labeller.ReadImageLabels(
          BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length),
          consumer,
          getString(R.string.message_no_image_labels)
      );
    });
  }

  /**
   * Method called when the camera image has been processed.
   *
   * @param labels The values picked up by the image labelling service.
   */
  private void returnIntent(List<ImageLabel> labels) {
    String error = getString(R.string.message_no_image_labels);

    // If there's a valid list of labels, pass it to the search results activity.
    if (labels != null && labels.size() > 0) {
      Intent intent = new Intent(ImageReaderActivity.this, SearchResultsActivity.class);
      intent.setAction(Intent.ACTION_SEARCH);

      // There's no practical way to query an array of values due to Firestore limitations.
      // So for now, just grab the most confident label (which is the first one) and query that.
      String query = labels.get(0).getText();
      intent.putExtra(SearchManager.QUERY, query);
      startActivity(intent);
      finish();
    } else {
      Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
      finish();
    }
  }
}
