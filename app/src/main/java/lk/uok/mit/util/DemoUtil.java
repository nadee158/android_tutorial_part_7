package lk.uok.mit.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DemoUtil {
    //the unique permission request code
    public static int UNIQUE_APP_REQUEST_CODE = 1234;

    //extension of the image file saved
    public static final String IMAGE_EXTENSION = "jpg";

    //the prefix of the image file saved
    public static final String IMAGE_PREFIX = "IMG_";

    //the physical folder name in which the captured images are saved
    public static final String GALLERY_DIRECTORY_NAME = "CameraDemo";

    // Check whether user has all the passed permissions or not.
    public static boolean hasUserPermissions(Context context, String[] permissions) {
        boolean ret = true;
        //iterate through the permissions passed
        for (int i = 0; i < permissions.length; i++) {
            // check if the permission is available for each permission
            int hasPermission = ContextCompat.checkSelfPermission(context, permissions[i]);
            //if the permission status is not equal to granted, return false
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ret = false;
            }
        }
        return ret;
    }

    //a method to check if a camera is available in device
    public static boolean isSystemFeatureAvailable(Context context, String featureName) {
        if (context.getPackageManager().hasSystemFeature(featureName)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static String getUniqueFileName(String prefix, String fileExtension){
        //create a simple date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        // Preparing media file naming convention
        //get current time
        Date currentTime= Calendar.getInstance().getTime();
        // get the current time using the dateformat object
        String timeStamp=dateFormat.format(currentTime);
        //construct a unique file name
        String fileName =  prefix + timeStamp + "." + fileExtension;
        return fileName;
    }

    /**
     * Creates and returns the image or video file before opening the camera
     */
    public static File constructOutputMediaFile(String fileName) {
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
        //create a new file
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName);
        Log.e("GALLERY_DIRECTORY", "Failed to CREATE create " + mediaFile);
        return mediaFile;
    }

    //get uri of the media file
    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
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



}
