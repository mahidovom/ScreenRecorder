package com.bursi.screenrecorder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.hbisoft.hbrecorder.HBRecorder;
import com.hbisoft.hbrecorder.HBRecorderListener;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements HBRecorderListener {
    HBRecorder hbRecorder;
    private int SCREEN_RECORD_REQUEST_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hbRecorder = new HBRecorder(this, this);
        quickSettings();
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    public void HBRecorderOnStart() {
        Log.e("3233", "HBRecorderOnStart: " );

    }

    @Override
    public void HBRecorderOnComplete() {
        Log.e("3233", "HBRecorderOnComplete: " );

    }

    @Override
    public void HBRecorderOnError(int errorCode, String reason) {
        Log.e("3233", reason );

    }
    private void startRecordingScreen() {
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = mediaProjectionManager != null ? mediaProjectionManager.createScreenCaptureIntent() : null;
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Start screen recording
                setOutputPath();
                //Start screen recording
                hbRecorder.startScreenRecording(data, resultCode, this);
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hbRecorder.stopScreenRecording();
                    }
                },28000);

            }
        }
    }

    @Override
    protected void onStop() {
        Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void quickSettings() {
        hbRecorder.setAudioBitrate(128000);
        hbRecorder.setAudioSamplingRate(44100);
        hbRecorder.recordHDVideo(false);
        hbRecorder.isAudioEnabled(true);
        //Customise Notification
       // hbRecorder.setNotificationSmallIcon(drawable2ByteArray(R.drawable.icon));
        hbRecorder.setNotificationTitle("Recording your screen");
        hbRecorder.setNotificationDescription("Drag down to stop the recording");

    }
    ContentResolver resolver;
    ContentValues contentValues;
    Uri mUri;
    private void setOutputPath() {
        String filename = generateFileName();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            resolver = getContentResolver();
//            contentValues = new ContentValues();
//            contentValues.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "HBRecorder");
//            contentValues.put(MediaStore.Video.Media.TITLE, filename);
//            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
//            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
//            mUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
//            //FILE NAME SHOULD BE THE SAME
//            hbRecorder.setFileName(filename);
//            hbRecorder.setOutputUri(mUri);
//        }else{
        try {
            createFolder();
            hbRecorder.setOutputPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) +"/468545878548/");
            hbRecorder.setFileName("a.mp4");

//        }Environment.getExternalStorageDirectory().getAbsolutePath()+"/468545878548"
        }catch (Exception e){
            Log.e("taghiryyr", e.toString() );
        }
            Log.e("taghiryyr", "setOutputPath: " );

    }

    //Generate a timestamp to be used as a file name
    private String generateFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate).replace(" ", "");
    }
    private void createFolder() {
        File f1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS) ,"/468545878548");
        if (!f1.exists()) {
            if (f1.mkdirs()) {
                Log.i("Folder ", "created");
            }
        }
    }
}