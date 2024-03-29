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
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import michaelmcmullin.sda.firstday.interfaces.services.CloudImageService;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * Service for uploading and downloading images to Firebase Storage. Code adapted from
 * https://firebase.google.com/docs/storage/android/upload-files
 */
public class FirebaseImageStorage implements CloudImageService {

  /**
   * A reference to Firebase storage
   */
  private final FirebaseStorage storage = FirebaseStorage.getInstance();

  /**
   * Uploads a local file to the cloud service.
   *
   * @param path The cloud path to upload the file to.
   * @param localFile The local file to upload.
   */
  @Override
  public void UploadFile(String path, File localFile) {
    try {
      InputStream stream = new FileInputStream(localFile);
      StorageReference storageRef = storage.getReference();
      StorageReference imageRef = storageRef.child(path);

      UploadTask task = imageRef.putStream(stream);
      task.addOnFailureListener(e -> Log.w(AppConstants.TAG, "Upload error: " + e.getMessage()))
          .addOnSuccessListener(
              taskSnapshot -> Log.i(AppConstants.TAG, "File uploaded ok!"));
    } catch (FileNotFoundException ex) {
      Log.w(AppConstants.TAG, ex.getMessage());
    }
  }

  /**
   * Uploads a Bitmap to the cloud service.
   *
   * @param path The cloud path to upload the file to.
   * @param image The image to upload to the cloud service.
   * @param format The format (JPEG, PNG, etc) to use to compress the bitmap.
   */
  @Override
  public void UploadBitmap(String path, Bitmap image, CompressFormat format) {
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef = storageRef.child(path);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    image.compress(format, 90, stream);
    byte[] data = stream.toByteArray();

    UploadTask task = imageRef.putBytes(data);
    task.addOnFailureListener(e -> Log.w(AppConstants.TAG, "Upload error: " + e.getMessage()))
        .addOnSuccessListener(
            taskSnapshot -> Log.i(AppConstants.TAG, "File uploaded ok!"));
  }

  /**
   * Downloads an image from the cloud service.
   *
   * @param path The cloud path to download the file from.
   * @param local A reference to the local file to save the download to.
   */
  @Override
  public void DownloadImage(String path, File local) {
    StorageReference storageRef = storage.getReference();
    StorageReference imageRef = storageRef.child(path);

    imageRef.getFile(local).addOnSuccessListener(
        taskSnapshot -> Log.i(AppConstants.TAG, "File downloaded.")).addOnFailureListener(
        e -> Log.w(AppConstants.TAG, "File failed to download."));
  }
}
