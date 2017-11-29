package edu.uw.wuyiz.chromatic;

import android.graphics.Bitmap;

/**
 * Created by wuyiz on 11/28/17.
 */

public class MoodBoard {

    public String moodBoardName;
    public String moodBoardDescription;
    public String moodBoardNotes;
    public String moodBoradAuthor;
    public String moodBoradCreatedTime;

    public Bitmap moodBoardImageContentBitmap;

    public MoodBoard(){
        this.moodBoardName = null;
        this.moodBoardDescription = null;
        this.moodBoardNotes = null;
        this.moodBoradAuthor = null;
        this.moodBoradCreatedTime = null;
        this.moodBoardImageContentBitmap = null;
    }

    public String getMoodBoardName() {
        return moodBoardName;
    }

    public void setMoodBoardName(String moodBoardName) {
        this.moodBoardName = moodBoardName;
    }

    public String getMoodBoardDescription() {
        return moodBoardDescription;
    }

    public void setMoodBoardDescription(String moodBoardDescription) {
        this.moodBoardDescription = moodBoardDescription;
    }

    public String getMoodBoardNotes() {
        return moodBoardNotes;
    }

    public void setMoodBoardNotes(String moodBoardNotes) {
        this.moodBoardNotes = moodBoardNotes;
    }

    public String getMoodBoradAuthor() {
        return moodBoradAuthor;
    }

    public void setMoodBoradAuthor(String moodBoradAuthor) {
        this.moodBoradAuthor = moodBoradAuthor;
    }

    public String getMoodBoradCreatedTime() {
        return moodBoradCreatedTime;
    }

    public void setMoodBoradCreatedTime(String moodBoradCreatedTime) {
        this.moodBoradCreatedTime = moodBoradCreatedTime;
    }

    public Bitmap getMoodBoardImageContentBitmap() {
        return moodBoardImageContentBitmap;
    }

    public void setMoodBoardImageContentBitmap(Bitmap moodBoardImageContentBitmap) {
        this.moodBoardImageContentBitmap = moodBoardImageContentBitmap;
    }
}