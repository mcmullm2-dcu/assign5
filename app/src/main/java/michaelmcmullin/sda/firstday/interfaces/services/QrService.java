package michaelmcmullin.sda.firstday.interfaces.services;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.Consumer;

/**
 * Defines a service for reading and writing QR codes.
 */
public interface QrService {

  /**
   * Reads a QR code from a supplied Bitmap image passes it to another method.
   * @param image The source image to read the QR code from.
   * @param consumer The method to call with the resulting String.
   * @param error Text to pass to consumer if no QR code is found.
   */
  void ReadQrCode(Bitmap image, final Consumer<String> consumer, final String error);

  /**
   * Converts a String into a QR code Bitmap image.
   * @param code The String to convert to a QR code.
   * @return Returns a Bitmap of the generated QR code.
   */
  Bitmap GenerateQrCode(String code);
}
