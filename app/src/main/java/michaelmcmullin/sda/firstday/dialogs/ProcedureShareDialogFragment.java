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

package michaelmcmullin.sda.firstday.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import michaelmcmullin.sda.firstday.interfaces.services.QrService;
import michaelmcmullin.sda.firstday.services.Services;
import michaelmcmullin.sda.firstday.utils.AppConstants;
import michaelmcmullin.sda.firstday.R;
import michaelmcmullin.sda.firstday.models.Procedure;
import michaelmcmullin.sda.firstday.models.Step;

/**
 * Dialog to enter a user's email address to share this procedure with.
 */
public class ProcedureShareDialogFragment extends DialogFragment {
  /**
   * The service to use for QR codes
   */
  private QrService qr = Services.QrService;

  /**
   * The procedure to share.
   */
  private Procedure procedure;

  /**
   * The list of steps that comprise this procedure.
   */
  private List<Step> steps;

  /**
   * The email address entered by the user
   */
  private EditText emailAddress;

  /**
   * Empty constructor required.
   */
  public ProcedureShareDialogFragment() {
  }

  /**
   * Creates a new instance of this dialog.
   * @param procedure The procedure to share with users.
   * @param steps The list of steps that go with this procedure.
   * @return An instance of this dialog fragment.
   */
  public static ProcedureShareDialogFragment newInstance(Procedure procedure, List<Step> steps) {
    ProcedureShareDialogFragment fragment = new ProcedureShareDialogFragment();
    fragment.procedure = procedure;
    fragment.steps = steps;
    return fragment;
  }
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_share_procedure, container);

    emailAddress = v.findViewById(R.id.edit_text_email);

    Button button = v.findViewById(R.id.button_dialog_share_procedure);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        // Send email
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("*/*");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress.getText().toString()});
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_share_procedure));
        email.putExtra(Intent.EXTRA_TEXT, emailText());

        // Create a QR code to include in the email.
        if (procedure != null) {
          Bitmap attachment = qr.GenerateQrCode(procedure.getId());

          // Convert the Bitmap to a URI
          Uri imageUri = saveImage(attachment);
          email.putExtra(Intent.EXTRA_STREAM, imageUri);
          email.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        if (email.resolveActivity(getContext().getPackageManager()) != null) {
          startActivity(email);
        }
        dismiss();
      }
    });

    Button cancel = v.findViewById(R.id.button_cancel);
    cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    return v;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().setTitle(R.string.app_name);
  }

  /**
   * Gets a string to use as the body of an email.
   * @return Returns the email body text.
   */
  public String emailText() {
    StringBuilder sb = new StringBuilder();

    if (procedure != null) {
      sb.append(procedure.getName());
      sb.append("\n");
      sb.append(procedure.getDescription());
      sb.append("\n\n");
    }

    if (steps != null && steps.size() > 0) {
      sb.append(getString(R.string.title_steps));
      sb.append("\n");
      int i = 1;
      for(Step step : steps) {
        sb.append(i++);
        sb.append(": ");
        sb.append(step.getName());
        sb.append("\n");
        sb.append(step.getDescription());
        sb.append("\n\n");
      }
    }

    sb.append(getString(R.string.message_procedure_shared_see_qr));

    return sb.toString();
  }

  /**
   * Saves the image as PNG to the app's cache directory.
   * @param image Bitmap to save.
   * @return Uri of the saved file or null
   * Adapted from StackOverflow: https://stackoverflow.com/a/50924037
   */
  private Uri saveImage(Bitmap image) {
    //TODO - Should be processed in another thread
    File imagesFolder = new File(getContext().getCacheDir(), "images");
    Uri uri = null;
    try {
      imagesFolder.mkdirs();
      File file = new File(imagesFolder, "shared_image.png");

      FileOutputStream stream = new FileOutputStream(file);
      image.compress(Bitmap.CompressFormat.PNG, 90, stream);
      stream.flush();
      stream.close();
      uri = FileProvider.getUriForFile(getContext(), "michaelmcmullin.sda.firstday.provider", file);

    } catch (IOException e) {
      Log.d(AppConstants.TAG, "IOException while trying to write file for sharing: " + e.getMessage());
    }
    return uri;
  }
}
