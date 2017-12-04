package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetMoodBoardInfoActivity extends AppCompatActivity {

    private static final String TAG = "Set Mood Board Info Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mood_board_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                final String moodBoardId = moodBoardName;

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(moodBoardId).exists()) {
                            Toast.makeText(
                                    SetMoodBoardInfoActivity.this,
                                    "Mood Board "
                                            + moodBoardId
                                            + " already exists, please enter a new name",
                                    Toast.LENGTH_LONG).show();

                        } else {

                            MoodBoard newMoodBoardObject = new MoodBoard(moodBoardId,
                                    moodBoardName,
                                    moodBoardAuthor,
                                    moodBoardDate,
                                    moodBoardNotes,
                                    moodBoardDescription,
                                    CreateMoodBoardActivity.createdMoodBoardBitmap);

                            databaseReference.child(moodBoardId).setValue(newMoodBoardObject);

                            Toast.makeText(
                                    SetMoodBoardInfoActivity.this,
                                    "Mood Board "
                                            + newMoodBoardObject.moodBoardId
                                            + " saved successfully",
                                    Toast.LENGTH_LONG).show();

                            Intent galleryIntent = new Intent(
                                    getApplicationContext(),
                                    GalleryScreenActivity.class);
                            startActivity(galleryIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}