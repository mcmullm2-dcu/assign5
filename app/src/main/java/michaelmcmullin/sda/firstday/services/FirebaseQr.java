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

package michaelmcmullin.sda.firstday.services;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import java.util.List;
import android.support.v4.util.Consumer;
import michaelmcmullin.sda.firstday.interfaces.services.QrService;
import net.glxn.qrgen.android.QRCode;

public class FirebaseQr implements QrService {

  /**
   * Reads a QR code from a supplied Bitmap image and returns its content as a String.
   *
   * @param image The source image to read the QR code from.
   * @param consumer The method to call with the resulting String.
   */
  @Override
  public void ReadQrCode(Bitmap image, final Consumer<String> consumer, final String error) {
    String code = null;
    FirebaseVisionBarcodeDetectorOptions options =
        new FirebaseVisionBarcodeDetectorOptions.Builder()
            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            .build();
    FirebaseVisionImage fbImage = FirebaseVisionImage.fromBitmap(image);
    FirebaseVisionBarcodeDetector detector = FirebaseVision.getInstance()
        .getVisionBarcodeDetector(options);
    Task<List<FirebaseVisionBarcode>> barcodeResult = detector.detectInImage(fbImage)
        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
          @Override
          public void onSuccess(List<FirebaseVisionBarcode> barcodes) {
            if (barcodes != null && barcodes.size() > 0) {
              consumer.accept(barcodes.get(0).getRawValue());
            } else {
              consumer.accept(error);
            }
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            consumer.accept(error);
          }
        });
  }

  /**
   * Converts a String into a QR code Bitmap image.
   * @param code The String to convert to a QR code.
   * @return Returns a Bitmap of the generated QR code.
   */
  @Override
  public Bitmap GenerateQrCode(String code) {
    return QRCode.from(code).bitmap();
  }
}
