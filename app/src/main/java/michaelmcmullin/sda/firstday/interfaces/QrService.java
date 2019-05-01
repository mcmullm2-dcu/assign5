package michaelmcmullin.sda.firstday.interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Defines a service for reading and writing QR codes.
 */
public interface QrService {

  /**
   * Reads a QR code from a supplied Bitmap image and returns its content as a String.
   * @param image The source image to read the QR code from.
   * @return A String representation of a QR code's content, or null.
   */
  String ReadQrCode(Bitmap image);

  /**
   * Converts a String into a QR code image, returning its Uri.
   * @param code The String to convert to a QR code.
   * @return The Uri of the generated QR code.
   */
  Uri GenerateQrCode(String code);
}
