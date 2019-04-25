package michaelmcmullin.sda.firstday.interfaces;

import android.graphics.Bitmap;

/**
 * Interface to facilitate passing a Bitmap from one entity to another
 */
public interface BitmapSaver {

  /**
   * Passes a Bitmap instance to another entity.
   * @param bitmap The Bitmap instance to pass.
   */
  public void PassBitmap(Bitmap bitmap);
}
