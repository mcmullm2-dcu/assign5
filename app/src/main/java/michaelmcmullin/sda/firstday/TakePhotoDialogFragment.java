package michaelmcmullin.sda.firstday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.camerakit.CameraKitView;
import michaelmcmullin.sda.firstday.interfaces.BitmapSaver;

public class TakePhotoDialogFragment extends DialogFragment {
  private CameraKitView cameraKitView;

  private BitmapSaver target;

  public TakePhotoDialogFragment() {
    // Empty constructor required.
  }

  public static TakePhotoDialogFragment newInstance() {
    TakePhotoDialogFragment fragment = new TakePhotoDialogFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.dialog_take_photo, container);
    cameraKitView = v.findViewById(R.id.camera);
    target = (BitmapSaver)getTargetFragment();

    Button button = v.findViewById(R.id.button_take_photo);
    button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
          @Override
          public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
            // capturedImage contains the image from the CameraKitView.
            Bitmap image = BitmapFactory.decodeByteArray(capturedImage, 0, capturedImage.length);
            target.PassBitmap(image);
            dismiss();
          }
        });
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

  @Override
  public void onStart() {
    super.onStart();
    cameraKitView.onStart();
  }

  @Override
  public void onResume() {
    super.onResume();
    cameraKitView.onResume();
  }

  @Override
  public void onPause() {
    cameraKitView.onPause();
    super.onPause();
  }

  @Override
  public void onStop() {
    cameraKitView.onStop();
    super.onStop();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
