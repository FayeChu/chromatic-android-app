package edu.uw.wuyiz.chromatic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetMoodBoardInfoActivity extends AppCompatActivity {

    private static final String TAG = "Set Mood Board Info Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mood_board_info);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final String MOOD_BOARD_COLLECTION_STORAGE_KEY = getString(R.string.mood_board_collection_storage_key);
        final DatabaseReference databaseReference =
                firebaseDatabase.getReference().child(MOOD_BOARD_COLLECTION_STORAGE_KEY);

        final EditText moodBoardNameUserInput = findViewById(R.id.mood_board_name);
        final EditText moodBoardAuthorUserInput = findViewById(R.id.mood_board_author);
        final EditText moodBoardDateUserInput = findViewById(R.id.mood_board_date);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        moodBoardDateUserInput.setText((Calendar.getInstance().getTime()).toString());

        final EditText moodBoardNotesUserInput = findViewById(R.id.mood_board_notes);
        final EditText moodBoardDescriptionUserInput = findViewById(R.id.mood_board_description);

//        moodBoardDescriptionUserInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    InputMethodManager inputMethodManager =
//                            (InputMethodManager) getSystemService(
//                                    Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(
//                            v.getWindowToken(), 0);
//                }
//                return false;
//        }});

        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String moodBoardName = moodBoardNameUserInput.getText().toString();
                final String moodBoardAuthor = moodBoardAuthorUserInput.getText().toString();
                final String moodBoardDate = moodBoardDateUserInput.getText().toString();
                final String moodBoardNotes = moodBoardNotesUserInput.getText().toString();
                final String moodBoardDescription = moodBoardDescriptionUserInput.getText().toString();
                final String moodBoardId = moodBoardName + moodBoardAuthor;

                if (moodBoardName == null || moodBoardName.trim().length() <= 0) {

                    Toast.makeText(
                            SetMoodBoardInfoActivity.this,
                            "Please enter a name for the mood board",
                            Toast.LENGTH_SHORT).show();

//                        } else if (dataSnapshot.child(moodBoardId).exists()) {
//                            Toast.makeText(
//                                    SetMoodBoardInfoActivity.this,
//                                    "Mood Board "
//                                            + moodBoardId
//                                            + " already exists, please enter a new name or author",
//                                    Toast.LENGTH_SHORT).show();

                } else {

                    MoodBoard moodBoard = new MoodBoard(moodBoardId,
                            moodBoardName,
                            moodBoardAuthor,
                            moodBoardDate,
                            moodBoardNotes,
                            moodBoardDescription,
                            getImageUri(SetMoodBoardInfoActivity.this, CreateMoodBoardActivity.createdMoodBoardBitmap).toString());

                    databaseReference.child(databaseReference.push().getKey()).setValue(moodBoard);

                    Toast.makeText(
                            SetMoodBoardInfoActivity.this,
                            "Mood Board "
                                    + moodBoard.moodBoardName
                                    + " saved successfully",
                            Toast.LENGTH_SHORT).show();

                    Intent galleryIntent = new Intent(
                            getApplicationContext(),
                            MoodBoardGalleryScreenActivity.class);
                    startActivity(galleryIntent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

//    public String bitmapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b = baos.toByteArray();
//        String bitmapString = Base64.encodeToString(b, Base64.DEFAULT);
//        return bitmapString;
//    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}