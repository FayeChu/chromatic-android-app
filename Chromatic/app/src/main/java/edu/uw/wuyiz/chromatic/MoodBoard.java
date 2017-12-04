package edu.uw.wuyiz.chromatic;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by wuyiz on 11/28/17.
 */

@IgnoreExtraProperties
public class MoodBoard {

    public String moodBoardId;
    public String moodBoardName;
    public String moodBoardAuthor;
    public String moodBoardDate;
    public String moodBoradNotes;
    public String moodBoradDescription;

    public Bitmap moodBoardBitmap;

    public MoodBoard() {
    }

    public MoodBoard (String moodBoardId,
                      String moodBoardName,
                      String moodBoardAuthor,
                      String moodBoardDate,
                      String moodBoradNotes,
                      String moodBoradDescription,
                      Bitmap moodBoardBitmap) {
        this.moodBoardId = moodBoardId;
        this.moodBoardName = moodBoardName;
        this.moodBoardAuthor = moodBoardAuthor;
        this.moodBoardDate = moodBoardDate;
        this.moodBoradNotes = moodBoradNotes;
        this.moodBoradDescription = moodBoradDescription;
        this.moodBoardBitmap = moodBoardBitmap;
    }
}