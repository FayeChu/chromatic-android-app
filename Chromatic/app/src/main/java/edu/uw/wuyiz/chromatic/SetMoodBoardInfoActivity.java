package edu.uw.wuyiz.chromatic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetMoodBoardInfoActivity extends AppCompatActivity {

    private static String moodBoardName;
    private static String moodBoardAuthor;
    private static String moodBoardDate;
    private static String moodBoardNotes;
    private static String moodBoardDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_mood_board_info);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        moodBoardName = new String();
        moodBoardAuthor = new String();
        moodBoardDate = new String();
        moodBoardNotes = new String();
        moodBoardDescription = new String();

        final EditText moodBoardNameUserInput = (EditText) findViewById(R.id.mood_board_name);
        final EditText moodBoardAuthorUserInput = (EditText) findViewById(R.id.mood_board_author);
        final EditText moodBoardDateUserInput = (EditText) findViewById(R.id.mood_board_date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        moodBoardDateUserInput.setText(simpleDateFormat.format(new Date()));

        final EditText moodBoardNotesUserInput = (EditText) findViewById(R.id.mood_board_notes);
        final EditText moodBoardDescriptionUserInput = (EditText) findViewById(R.id.mood_board_description);

//        moodBoardDescriptionUserInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    InputMethodManager inputMethodManager =
//                            (InputMethodManager) v.getContext().getSystemService(
//                                    Context.INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(
//                            v.getWindowToken(), 0);
//                }
//                return false;
//        }});

        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moodBoardName = moodBoardNameUserInput.getText().toString();
                moodBoardAuthor = moodBoardAuthorUserInput.getText().toString();
                moodBoardDate = moodBoardDateUserInput.getText().toString();
                moodBoardNotes = moodBoardNotesUserInput.getText().toString();
                moodBoardDescription = moodBoardDescriptionUserInput.getText().toString();
            }
        });
    }
}