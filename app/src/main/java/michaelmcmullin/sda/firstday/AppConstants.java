package michaelmcmullin.sda.firstday;

import android.app.Application;

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

  // ===============================================================================================
  // Shared Preference Keys
  // ===============================================================================================

  public static final String PREFS_BASE = "michaelmcmullin.sda.firstday";

  public static final String PREFS_PROCEDURE_NAME = PREFS_BASE + ".Procedure.Name";
  public static final String PREFS_PROCEDURE_DESCRIPTION = PREFS_BASE + ".Procedure.Description";
  public static final String PREFS_PROCEDURE_CREATED = PREFS_BASE + ".Procedure.Created";
  public static final String PREFS_PROCEDURE_IS_DRAFT = PREFS_BASE + ".Procedure.IsDraft";
  public static final String PREFS_PROCEDURE_IS_PUBLIC = PREFS_BASE + ".Procedure.IsPublic";
  public static final String PREFS_STEP_COUNT = PREFS_BASE + ".Step.Count";
  public static final String PREFS_STEP_NAME = PREFS_BASE + ".Step.Name";
  public static final String PREFS_STEP_DESCRIPTION = PREFS_BASE + ".Step.Description";
  public static final String PREFS_TAGS = PREFS_BASE + ".Tags";

  // ===============================================================================================
  // Formatting Strings
  // ===============================================================================================
  public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
