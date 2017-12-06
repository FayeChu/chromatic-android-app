package edu.uw.wuyiz.chromatic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

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
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        });
    }

    private void pickImageFromAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE || requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                //onImagePicked(data.getData());
                Intent intent = new Intent(this, MoodPreviewActivity.class);
                intent.putExtra("uri", data.getData().toString());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}