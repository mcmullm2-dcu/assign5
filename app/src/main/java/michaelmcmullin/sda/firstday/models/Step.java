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
import michaelmcmullin.sda.firstday.AppConstants;

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

  /**
   * Firestore requires a constructor with no arguments.
   */
  public Step() {}

  /**
   * Creates an instance of {@link Step} populating its main fields.
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
   * @return The unique ID of this {@link Step} instance.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique ID of this {@link Step} instance.
   * @param id The unique ID of this {@link Step} instance.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the sequence number of this {@link Step} instance.
   * @return Returns the sequence number of this {@link Step} instance.
   */
  public int getSequence() {
    return sequence;
  }
  /**
   * Gets the name of this {@link Step} instance.
   * @return Returns the name of this {@link Step} instance.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets a more detailed description of this {@link Step} instance.
   * @return Returns a description of this {@link Step} instance.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the photo to a scaled version of a given bitmap.
   * @param bitmap
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
  public void savePhoto() {
    if (!hasPhoto()) {
      Log.w(AppConstants.TAG, "No photo available to save");
      return;
    }

    if (photoId == null || photoId.isEmpty()) {
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
      return;
    } catch (IOException ex) {
      Log.w(AppConstants.TAG, "Problem writing file: " + ex.getMessage());
      return;
    }
  }

  /**
   * Loads the local copy of the photo
   */
  public void loadPhoto() {
    if (photoId == null || photoId.isEmpty()) {
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
   * @return Returns the file where the photo is stored.
   */
  public File getLocalPhotoFile() {
    if (photoId == null || photoId.isEmpty()) {
      return null;
    }
    File dir = getLocalDirectory();
    if (dir == null) {
      return null;
    }

    return new File(dir, photoId + ".jpg");
  }

  /**
   * Gets a reference to the local directory to store photos.
   * @return Returns the base directory where photos will be stored.
   */
  public File getLocalDirectory() {
    try {
      String path = Environment.getExternalStorageDirectory().getAbsolutePath()
          + "FirstDayTmp";
      File dir = new File(path);
      if(!dir.exists()) {
        dir.mkdirs();
      }
      return dir;
    } catch (SecurityException ex) {
      Log.w(AppConstants.TAG, "Can't get local directory: " + ex.getMessage());
      return null;
    }
  }

  /**
   * Gets the photo associated with this step.
   * @return Returns a Bitmap of the photo for this step.
   */
  public Bitmap getPhoto() {
    return photo;
  }

  /**
   * Indicates whether this step has a photo available.
   * @return Returns <code>true</code> if there is a photo
   * assigned to this step.
   */
  public boolean hasPhoto() {
    return photo != null;
  }

  /**
   * Sets the unique ID of the photo from cloud storage.
   * @param id
   */
  public void setPhotoId(String id) {
    photoId = id;
  }

  /**
   * Generate a unique ID for the photo, called the first time a
   * photo needs to be sent to online storage to uniquely identify it.
   * Code adapted from StackOverflow: https://stackoverflow.com/a/2982751
   */
  public void generatePhotoId() {
    photoId = java.util.UUID.randomUUID().toString();
  }

  /**
   * Gets the unique ID for identifying the photo in online storage.
   * @return Returns the photo's unique ID.
   */
  public String getPhotoId() {
    return photoId;
  }

  /**
   * Resizes a photo while respecting the original aspect ratio.
   * @param image The source image to scale.
   * @param maxWidth The maximum width the resulting image should be.
   * @param maxHeight The minimum width the resulting image should be.
   * @return The resized original bitmap.
   * Code taken from StackOverflow: https://stackoverflow.com/a/28367226
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
        finalWidth = (int) ((float)maxHeight * ratioBitmap);
      } else {
        finalHeight = (int) ((float)maxWidth / ratioBitmap);
      }
      image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
      return image;
    } else {
      return image;
    }
  }
}
