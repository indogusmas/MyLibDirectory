package com.gzeinnumer.mylibdirectory;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gzeinnumer.gzndirectory.helper.FGDir;
import com.gzeinnumer.gzndirectory.helper.FGFile;
import com.gzeinnumer.gzndirectory.helper.imagePicker.FileCompressor;
import com.gzeinnumer.gzndirectory.helper.imagePicker.ImageUtil;
import com.karumi.dexter.BuildConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    File imageCamera;
    FileCompressor fileCompressor;
    private String TAG = getClass().getSimpleName();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.image_watermark);
        String externalFolderName = getApplication().getString(R.string.app_name);
        FGDir.initExternalDirectoryName(externalFolderName);
        fileCompressor = new FileCompressor(this);
        fileCompressor.setQuality(20);
        dispatchTakePictureIntent();

    }

    private void dispatchTakePictureIntent() {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Intent mIntentImageCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mIntentImageCamera.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = FGFile.createImageFile(this, fileName);
            } catch (Exception e) {

            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, "com.gzeinnumer.mylibdirectory" + ".provider", photoFile);
                imageCamera = photoFile;
                Log.e(TAG, "dispatchTakePictureIntent: "+ imageCamera.getPath());
                mIntentImageCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(mIntentImageCamera, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //    try {
            if (resultCode == RESULT_OK){
                if (requestCode == 1){
                  //  try {
                    int file_size = Integer.parseInt(String.valueOf(imageCamera.length()/1024));
                    Log.e(TAG, "onActivityResult: "+ file_size);
                    try {
                        int file_size_affter = Integer.parseInt(String.valueOf(imageCamera.length()/1024));
                        imageCamera = fileCompressor.addWatermark(imageCamera);
                        Log.e(TAG, "onActivityResult: "+ file_size_affter);

                    } catch (Exception e) {
                       // Log.e(TAG, "onActivityResult: "+ e.getMessage());
                        e.printStackTrace();
                    }
                    Glide.with(CameraActivity.this).load(imageCamera).into(imageView);
                  //  }catch (Exception e){
                   //     Log.e(TAG, "onActivityResult: "+ e.getMessage());
                 //   }
                }
            }
//        }catch (Exception e){
//            Log.e(TAG, "onActivityResult: "+ e.getMessage() );
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
















