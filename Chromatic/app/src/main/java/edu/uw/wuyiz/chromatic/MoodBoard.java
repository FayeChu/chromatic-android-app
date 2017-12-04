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

    public String getMoodBoardId() {
        return moodBoardId;
    }

    public void setMoodBoardId(String moodBoardId) {
        this.moodBoardId = moodBoardId;
    }

    public String getMoodBoardName() {
        return moodBoardName;
    }

    public void setMoodBoardName(String moodBoardName) {
        this.moodBoardName = moodBoardName;
    }

    public String getMoodBoardAuthor() {
        return moodBoardAuthor;
    }

    public void setMoodBoardAuthor(String moodBoardAuthor) {
        this.moodBoardAuthor = moodBoardAuthor;
    }

    public String getMoodBoardDate() {
        return moodBoardDate;
    }

    public void setMoodBoardDate(String moodBoardDate) {
        this.moodBoardDate = moodBoardDate;
    }

    public String getMoodBoradNotes() {
        return moodBoradNotes;
    }

    public void setMoodBoradNotes(String moodBoradNotes) {
        this.moodBoradNotes = moodBoradNotes;
    }

    public String getMoodBoradDescription() {
        return moodBoradDescription;
    }

    public void setMoodBoradDescription(String moodBoradDescription) {
        this.moodBoradDescription = moodBoradDescription;
    }

    public Bitmap getMoodBoardBitmap() {
        return moodBoardBitmap;
    }

    public void setMoodBoardBitmap(Bitmap moodBoardBitmap) {
        this.moodBoardBitmap = moodBoardBitmap;
    }
}