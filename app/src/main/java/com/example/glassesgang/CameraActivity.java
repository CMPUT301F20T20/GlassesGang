package com.example.glassesgang;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Activity that handles Camera actions
 * (request camera permissions, open camera, and save picture taken)
 */
public class CameraActivity extends AppCompatActivity {
    public ImageView bookImageView;
    private static Bitmap bitmap;
    private final int CAMERA_PERM_CODE = 101;
    private final int CAMERA_REQ_CODE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LayoutInflater factory = getLayoutInflater();
        final View view = factory.inflate(R.layout.add_edit_book, null);
        bookImageView = view.findViewById(R.id.login_book_image_view);

        requestCameraPermission();
    }

    /**
     * Requests for camera permission
     * if permission was granted, open camera
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA
            }, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // user denied request
                Toast.makeText(this, "Camera Permission was denied. Go to settings to grant permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Open user's camera
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQ_CODE) {
            if (data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
        }
        finish();
    }

    /**
     * Returns the book image
     * @return bitmap object containing the picture taken from the user's camera
     */
    public static Bitmap getBookImage() {
        return bitmap;
    }

    /**
     * Clean static object variable
     */
    public static void cleanBookImage() {
        bitmap = null;
    }

}
