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

package michaelmcmullin.sda.firstday;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import java.util.List;
import michaelmcmullin.sda.firstday.utils.AppConstants;

/**
 * {@link LoginActivity} is the first Activity displayed when the application is launched, featuring
 * an introduction and a button to sign in. If the user is already signed in, this Activity is
 * bypassed and redirected to {@link MainActivity}.
 */
public class LoginActivity extends AppCompatActivity {

  /**
   * A list of authentication providers. Some are commented out for future configuration. For now,
   * we're only supporting Email and Google providers
   */
  private final List<IdpConfig> providers = Arrays.asList(
      new AuthUI.IdpConfig.EmailBuilder().build(),
      //new AuthUI.IdpConfig.PhoneBuilder().build(),
      new AuthUI.IdpConfig.GoogleBuilder().build()
      //new AuthUI.IdpConfig.FacebookBuilder().build(),
      //new AuthUI.IdpConfig.TwitterBuilder().build()
  );

  /**
   * The authenticated user (or null if the user isn't authenticated)
   */
  private FirebaseUser user;

  /**
   * Called when {@link LoginActivity} is started, initialising the Activity and inflating the
   * appropriate XML layout.
   *
   * @param savedInstanceState Used if this Activity is re-initialised, where it contains the
   *     most recently available data (or null).
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // If the user is already logged in, skip this activity
    if (IsLoggedIn()) {
      StartApp();
    }
    setContentView(R.layout.activity_login);
  }
  /**
   * Check if a user is logged in
   * @return Returns <code>true</code> if the user is logged in. Otherwise,
   *     return <code>false</code>
   */
  private boolean IsLoggedIn() {
    user = FirebaseAuth.getInstance().getCurrentUser();
    return user != null;
  }

  /**
   * Method to start the main app
   */
  private void StartApp() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  /**
   * Responds to the user clicking the 'Login' button.
   */
  public void Login(View view) {
    // Create and launch sign-in intent
    startActivityForResult(
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_app_icon)
            .build(),
        AppConstants.RC_SIGN_IN);
  }

  /**
   * Handles the result returned from the FirebaseUI authentication process.
   * @param requestCode The request we're responding to.
   * @param resultCode A code indicating the status of the result.
   * @param data The resulting <code>Intent</code> which can contain data through "extras".
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == AppConstants.RC_SIGN_IN) {
      IdpResponse response = IdpResponse.fromResultIntent(data);

      if (resultCode == RESULT_OK) {
        // Successfully signed in
        // Now start the app's main activity.
        StartApp();
      } else {
        // Sign in failed. If response is null the user canceled the
        // sign-in flow using the back button. Otherwise check
        // response.getError().getErrorCode() and handle the error.
        // ...
        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        if (response != null) {
          Log.w(AppConstants.TAG, response.getError().getMessage());
        }
      }
    }
  }
}
