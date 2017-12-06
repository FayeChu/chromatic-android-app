package edu.uw.wuyiz.chromatic;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
<<<<<<< Updated upstream
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
=======
>>>>>>> Stashed changes
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import java.io.File;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Su Wang on 2017/11/28.
 */

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final String KEY_IMAGE_URI = "image_uri";
    private Uri imageUri;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_photo);

        //import a photo from album
        Button btn_import_photo = (Button) findViewById(R.id.button_import_photo);
        btn_import_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromAlbum();
            }
        });

        //view gallery
        Button btn_view_gallery = (Button) findViewById(R.id.button_view_gallery);
        btn_view_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    public void run() {
//                        InputMethodManager inputMethodManager =
//                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                    }
//                }, 200);

                Intent galleryIntent = new Intent(getApplicationContext(), PaletteGalleryScreenActivity.class);
                startActivity(galleryIntent);
            }
        });

        //take a photo
        Button take_photo = (Button) findViewById(R.id.button_take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< Updated upstream

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }
                try {
                    File outputImage = createImageFile();
                    outputImage.createNewFile();
                    imageUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", outputImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                //intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(intent,REQUEST_TAKE_PHOTO);
//=======
//                File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
=======
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    try {
                        File photoFile = createImageFile();
                        if (photoFile != null) {
                            imageUri = FileProvider.getUriForFile(MainActivity.this,
                                    "edu.uw.wuyiz.chromatic.android.fileprovider",
                                    photoFile);
                            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                        }
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created

                }
//                photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
>>>>>>> Stashed changes
//                intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photo));
//                imageUri = Uri.fromFile(photo);
//                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
<<<<<<< Updated upstream
//>>>>>>> dc6b1a8d1e2545aa82a769003b0e4d4f2a74d49f
=======


//                imageUri = FileProvider.getUriForFile(MainActivity.this, getApplicationContext().getPackageName() +
//                        ".my.package.name.provider", photo);
//                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
>>>>>>> Stashed changes
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_TAKE_PHOTO){
            if (resultCode == RESULT_OK) {
                galleryAddPic();
                Intent intent = new Intent(this, MoodPreviewActivity.class);
                intent.putExtra("uri", imageUri.toString());
                startActivity(intent);
            }

        } else if (requestCode == REQUEST_CODE_PICK_IMAGE){
            if (resultCode == RESULT_OK){
                Intent intent = new Intent(this, MoodPreviewActivity.class);
                intent.putExtra("uri", data.getData().toString());
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private  File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;//创建以时间命名的文件名称
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);//创建保存的路径

        File image = new File(storageDir.getPath(), imageFileName + ".jpg");
        if (!image.exists()) {
            try {
                //在指定的文件夹中创建文件
                image.createNewFile();
            } catch (Exception e) {
            }
        }

        return image;
    }

    private void galleryAddPic()
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File("file://"+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
        Uri contentUri = Uri.fromFile(f);
        Log.v("MainActivity", contentUri.getPath());
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}