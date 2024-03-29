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

package michaelmcmullin.sda.firstday.models;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import michaelmcmullin.sda.firstday.interfaces.services.CloudImageService;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * Class for storing information about a single {@link Step} in a {@link Procedure}.
 */
public class Step {

  private String id;
  private int sequence;
  private String name;
  private String description;
  private String photoId;
  private Bitmap photo;
  private CloudImageService cloud;

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Step() {
  }

  /**
   * Creates an instance of {@link Step} populating its main fields.
   *
   * @param sequence The order that this {@link Step} appears.
   * @param name The name of this {@link Step} instance.
   * @param description A more detailed description of this {@link Step} instance.
   */
  public Step(int sequence, String name, String description) {
    this.sequence = sequence;
    this.name = name;
    this.description = description;
  }

  /**
   * Gets the unique ID of this {@link Step} instance.
   *
   * @return The unique ID of this {@link Step} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Step} instance.
   *
   * @param id The unique ID of this {@link Step} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the sequence number of this {@link Step} instance.
   *
   * @return Returns the sequence number of this {@link Step} instance.
   */
  public int getSequence() {
    return sequence;
  }

  /**
   * Gets the name of this {@link Step} instance.
   *
   * @return Returns the name of this {@link Step} instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a more detailed description of this {@link Step} instance.
   *
   * @return Returns a description of this {@link Step} instance.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the photo to a scaled version of a given bitmap.
   *
   * @param bitmap The Bitmap image to scale.
   */
  public void setPhoto(Bitmap bitmap) {
    if (bitmap == null) {
      photo = null;
      photoId = null;
    } else {
      photo = resize(bitmap, 300, 300);
    }
  }

  /**
   * Saves a local copy of the photo.
   */
  public void saveLocalPhoto() {
    if (!hasPhoto()) {
      Log.w(AppConstants.TAG, "No photo available to save");
      return;
    }

    if (!hasPhotoId()) {
      generatePhotoId();
    }

    File file = getLocalPhotoFile();
    if (file == null) {
      Log.w(AppConstants.TAG, "Problem getting a reference to the local file");
      return;
    }

    try {
      FileOutputStream out = new FileOutputStream(file);
      photo.compress(CompressFormat.JPEG, 90, out);
      out.flush();
      out.close();
    } catch (FileNotFoundException ex) {
      Log.w(AppConstants.TAG, "Photo file cannot be created: " + ex.getMessage());
    } catch (IOException ex) {
      Log.w(AppConstants.TAG, "Problem writing file: " + ex.getMessage());
    }
  }

  /**
   * Loads the local copy of the photo
   */
  public void loadLocalPhoto() {
    if (!hasPhotoId()) {
      return;
    }

    File file = getLocalPhotoFile();
    if (file == null) {
      Log.w(AppConstants.TAG, "Problem getting a reference to the local file");
      return;
    }

    photo = BitmapFactory.decodeFile(file.getAbsolutePath());
  }

  /**
   * Gets a reference to the locally stored photo.
   *
   * @return Returns the file where the photo is stored.
   */
  public File getLocalPhotoFile() {
    if (!hasPhotoId()) {
      return null;
    }
    File dir = getLocalDirectory();
    if (dir == null) {
      return null;
    }

    File localFile = new File(dir, photoId + ".jpg");

    if (!localFile.exists()) {
      try {
        if (!localFile.createNewFile()) {
          return null;
        }
      } catch (IOException ex) {
        Log.w(AppConstants.TAG, "Problem creating local file: " + ex.getMessage());
        return null;
      }
    }

    return localFile;
  }

  /**
   * Gets a reference to the local directory to store photos.
   *
   * @return Returns the base directory where photos will be stored.
   */
  private File getLocalDirectory() {
    try {
      String path = Environment.getExternalStorageDirectory().getAbsolutePath()
          + "FirstDayTmp";
      File dir = new File(path);
      if (!dir.exists()) {
        if (!dir.mkdirs()) {
          return null;
        }
      }
      return dir;
    } catch (SecurityException ex) {
      Log.w(AppConstants.TAG, "Can't get local directory: " + ex.getMessage());
      return null;
    }
  }

  /**
   * Gets the photo associated with this step.
   *
   * @return Returns a Bitmap of the photo for this step.
   */
  public Bitmap getPhoto() {
    return photo;
  }

  /**
   * Indicates whether this step has a photo available.
   *
   * @return Returns <code>true</code> if there is a photo assigned to this step.
   */
  public boolean hasPhoto() {
    return photo != null;
  }

  /**
   * Indicates whether this step has a photo ID available.
   *
   * @return Returns <code>true</code> if there is a photo ID assigned to this step.
   */
  public boolean hasPhotoId() {
    return !(photoId == null || photoId.isEmpty());
  }

  /**
   * Sets the unique ID of the photo from cloud storage.
   *
   * @param id The cloud service ID that references the required photo.
   */
  public void setPhotoId(String id) {
    photoId = id;
  }

  /**
   * Generate a unique ID for the photo, called the first time a photo needs to be sent to online
   * storage to uniquely identify it. Code adapted from StackOverflow:
   * https://stackoverflow.com/a/2982751
   */
  public void generatePhotoId() {
    photoId = java.util.UUID.randomUUID().toString();
  }

  /**
   * Gets the unique ID for identifying the photo in online storage.
   *
   * @return Returns the photo's unique ID.
   */
  public String getPhotoId() {
    return photoId;
  }

  /**
   * Gets the cloud storage referenced to this photo.
   *
   * @return Returns the cloud reference path to download this photo
   */
  public String getCloudPath() {
    if (photoId == null || photoId.isEmpty()) {
      return null;
    }

    return "firstday/steps/" + photoId + ".jpg";
  }

  /**
   * Sets a service that can be used to store images to the cloud and retrieve them later.
   *
   * @param cloud A {@link CloudImageService} implementation to use for cloud operations.
   */
  public void setCloud(CloudImageService cloud) {
    this.cloud = cloud;
  }

  /**
   * Gets the current service being used to store and retrieve images from the cloud.
   *
   * @return Returns a {@link CloudImageService} implementation used by this step to save and
   *     retrieve photos.
   */
  public CloudImageService getCloud() {
    return cloud;
  }

  /**
   * Saves this instance's photo to cloud storage.
   */
  public void saveCloudPhoto() {
    String path = getCloudPath();
    if (path == null) {
      Log.w(AppConstants.TAG, "Can't get a path to cloud storage.");
      return;
    }

    if (cloud == null) {
      Log.w(AppConstants.TAG, "No cloud service defined.");
      return;
    }

    if (photo != null) {
      cloud.UploadBitmap(path, photo, CompressFormat.JPEG);
      return;
    }

    File localPhotoFile = getLocalPhotoFile();
    if (localPhotoFile != null) {
      cloud.UploadFile(path, localPhotoFile);
      return;
    }

    Log.w(AppConstants.TAG, "Couldn't find a bitmap or local file to upload.");
  }

  /**
   * Downloads this instance's photo from cloud storage.
   */
  public void loadCloudPhoto() {
    String path = getCloudPath();
    if (path == null) {
      Log.w(AppConstants.TAG, "Can't get a path to cloud storage.");
      return;
    }

    if (cloud == null) {
      Log.w(AppConstants.TAG, "No cloud service defined.");
      return;
    }

    cloud.DownloadImage(path, getLocalPhotoFile());
    loadLocalPhoto();
  }

  /**
   * Resizes a photo while respecting the original aspect ratio.
   *
   * @param image The source image to scale.
   * @param maxWidth The maximum width the resulting image should be.
   * @param maxHeight The minimum width the resulting image should be.
   * @return The resized original bitmap. Code taken from StackOverflow:
   *     https://stackoverflow.com/a/28367226
   */
  public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
    if (maxHeight > 0 && maxWidth > 0) {
      int width = image.getWidth();
      int height = image.getHeight();
      float ratioBitmap = (float) width / (float) height;
      float ratioMax = (float) maxWidth / (float) maxHeight;

      int finalWidth = maxWidth;
      int finalHeight = maxHeight;
      if (ratioMax > ratioBitmap) {
        finalWidth = (int) ((float) maxHeight * ratioBitmap);
      } else {
        finalHeight = (int) ((float) maxWidth / ratioBitmap);
      }
      image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
      return image;
    } else {
      return image;
    }
  }
}
