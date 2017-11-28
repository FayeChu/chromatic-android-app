package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GalleryScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final String KEY_IMAGE_URI = "image_uri";

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_screen);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_moodboards:
                                Intent moodBoardsIntent = new Intent(getApplicationContext(), MoodBoardActivity.class);
                                startActivity(moodBoardsIntent);
                            case R.id.action_gallery:
//                                Intent galleryIntent = new Intent(getApplicationContext(), GalleryScreenActivity.class);
//                                startActivity(galleryIntent);
                                return true;
                            case R.id.action_creations:
                                Intent creationsIntent = new Intent(getApplicationContext(), CreationsActivity.class);
                                startActivity(creationsIntent);

                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_IMAGE_URI, imageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Uri uri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
        if (uri != null) {
            //onImagePicked(uri);
            Intent intent = new Intent(this, MoodPreviewActivity.class);
            intent.putExtra("uri", uri.toString());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick_image:
                onPickImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
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

    private void onPickImage() {

        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(pickImageIntent,
                getString(R.string.action_pick_image));
        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE);
    }

}
