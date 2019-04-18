package michaelmcmullin.sda.firstday;

/**
 * A central class designed to hold internal constants that can be used throughout the app code.
 */
public class AppConstants {

  /**
   * A tag to help label debugging messages.
   */
  public static final String TAG = "FirstDay";

  /**
   * Request code used for FirebaseUI authentication intent.
   */
  public static final int RC_SIGN_IN = 1;

  /**
   * Request code used for launching camera intent for QR code scanning.
   */
  public static final int REQUEST_TAKE_QR_PHOTO = RC_SIGN_IN + 1;

}
