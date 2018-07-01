package lk.uok.mit.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import lk.uok.mit.helloworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraDemoFragment extends Fragment {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    public static final String IMAGE_EXTENSION = "jpg";
    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "CameraDemo";
    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    private Bitmap bitmap;
    private ImageView imageView;

    private Button buttonCameraApp;
    private Button buttonCameraAPI;

    private String imageStoragePath;

    private Context context;
    private int uniqueAppRequestCode = 1234;


    private String[] requiredPermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};


    public CameraDemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //set the text appear in title bar
        getActivity().setTitle("Camera Demo");
        context = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_demo, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        context = getContext();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.imageViewCameraImage);
        buttonCameraApp = view.findViewById(R.id.buttonCameraApp);
        buttonCameraAPI = view.findViewById(R.id.buttonCameraAPI);
        context = getContext();

        buttonCameraApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasUserPermissions = hasUserPermissions(requiredPermissions);
                if (!(hasUserPermissions)) {
                    //if read contact permission is not already granted, request permission
                    requestPermissions(requiredPermissions);
                } else {
                    boolean cameraAvailable = isCameraAvailable();
                    if (cameraAvailable) {
                        captureImage();
                    }
                }
            }
        });
    }

    //a method to check if a camera is available in device
    private boolean isCameraAvailable() {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    // Request runtime permissions from app user.
    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(getActivity(), permissions, uniqueAppRequestCode);
    }

    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = getOutputMediaFile();
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = getOutputMediaFileUri(context, file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */
    public static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                GALLERY_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(GALLERY_DIRECTORY_NAME, "Failed to CREATE create "
                        + GALLERY_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Preparing media file naming convention
        // adds timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Calendar.getInstance().getTime());

        String fileName = "IMG_" + timeStamp + "." + IMAGE_EXTENSION;
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);

        Log.e("GALLERY_DIRECTORY", "Failed to CREATE create " + mediaFile);
        return mediaFile;
    }

    public Uri getOutputMediaFileUri(Context context, File file) {
        Log.d("THE CONTEXT", context.toString());
        Log.d("THE FILE", file.toString());
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Activity result method will be called after closing the camera
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Refreshing the gallery
                refreshGallery(context, imageStoragePath);
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(context,
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(context,
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Refreshes gallery on adding new image/video. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    /**
     * Display image from gallery
     */
    private void previewCapturedImage() {
        try {

            Bitmap bitmap = optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            imageView.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        // bitmap factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = sampleSize;

        return BitmapFactory.decodeFile(filePath, options);
    }

    // Check whether user has all the passed permissions or not.
    private boolean hasUserPermissions(String[] permissions) {
        boolean ret = true;
        for (int i = 0; i < permissions.length; i++) {
            // return permission grant status.
            int hasPermission = ContextCompat.checkSelfPermission(context, permissions[i]);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ret = false;
            }
        }
        return ret;
    }


}
