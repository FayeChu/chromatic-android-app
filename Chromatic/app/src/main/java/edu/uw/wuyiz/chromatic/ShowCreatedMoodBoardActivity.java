package edu.uw.wuyiz.chromatic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by wuyiz on 11/29/17.
 */

public class ShowCreatedMoodBoardActivity extends AppCompatActivity {

    ImageView createdMoodBoardImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_created_mood_board);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createdMoodBoardImage = (ImageView) findViewById(R.id.image);
        createdMoodBoardImage.setImageBitmap(CreateMoodBoardActivity.createdMoodBoardBitmap);
    }
}
