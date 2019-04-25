package michaelmcmullin.sda.firstday.interfaces;

import android.graphics.Bitmap;
import java.io.File;

/**
 * Describes methods for uploading and downloading images from a cloud service
 */
public interface CloudImageService {

  /**
   * Uploads a local file to the cloud service.
   * @param path The cloud path to upload the file to.
   * @param localFile The local file to upload.
   */
  void UploadFile(String path, File localFile);

  /**
   * Uploads a Bitmap to the cloud service.
   * @param path The cloud path to upload the file to.
   * @param image The image to upload to the cloud service.
   * @param format The format (JPEG, PNG, etc) to use to compress the bitmap.
   */
  void UploadBitmap(String path, Bitmap image, Bitmap.CompressFormat format);

  /**
   * Downloads an image from the cloud service to local file.
   * @param path The cloud path to download the file from.
   * @param local A reference to the local file to save the download to.
   * @return A reference to the locally downloaded file.
   */
  void DownloadImage(String path, File local);
}
