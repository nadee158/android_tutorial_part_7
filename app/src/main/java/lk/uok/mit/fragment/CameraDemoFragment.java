package lk.uok.mit.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import lk.uok.mit.helloworld.R;
import lk.uok.mit.util.DemoUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraDemoFragment extends Fragment {

    //a code to identify the requested context of the capera app
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    //to hold the bitmap of the captured image
    private Bitmap bitmap;

    //the image view
    private ImageView imageView;

    //the button to capture image
    private Button buttonCaptureImage;

    //the constructed storage path of the captured image
    private String imageStoragePath;

    //the context
    private Context context;

    //permission array required execute the code here
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initialize the image view
        imageView = view.findViewById(R.id.imageViewCameraImage);
        //initialize the button
        buttonCaptureImage = view.findViewById(R.id.buttonCameraApp);
        //initalize the context
        context = getContext();
        //set the onclick listener of the button
        buttonCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if user has given the required permissions
                boolean hasUserPermissions = DemoUtil.hasUserPermissions(context, requiredPermissions);
                if (!(hasUserPermissions)) {
                    //if read contact permission is not already granted, request permission
                    requestPermissions(requiredPermissions, DemoUtil.UNIQUE_APP_REQUEST_CODE);
                } else {
                    //check if the camera is available in this device
                    boolean cameraAvailable = DemoUtil.isSystemFeatureAvailable(context, PackageManager.FEATURE_CAMERA);
                    if (cameraAvailable) {
                        //if camera is available, capture the image
                        captureImage();
                    } else {
                        //else notify the user
                        Toast.makeText(context,
                                "Camera is not Available!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }


    /**
     * Capturing Camera Image will launch camera app requested image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //get a unique name for the image file
        String fileName = DemoUtil.getUniqueFileName(DemoUtil.IMAGE_PREFIX, DemoUtil.IMAGE_EXTENSION);
        //construct a file to store the captured image
        File file = DemoUtil.constructOutputMediaFile(fileName);
        if (file != null) {
            //if the file is created and returned, get its path
            imageStoragePath = file.getAbsolutePath();
        }
        //get the uniform resource locator of the file
        Uri fileUri = DemoUtil.getOutputMediaFileUri(context, file);
        //put the URI in to camera intent as data, it will populate this file
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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
                DemoUtil.refreshGallery(context, imageStoragePath);
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
     * Display image from gallery
     */
    private void previewCapturedImage() {
        //construct the bit map ater reducing the file seize
        Bitmap bitmap = DemoUtil.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
        //set the generated bitmap to the image view
        imageView.setImageBitmap(bitmap);
    }


}
