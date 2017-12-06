package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by wuyiz on 11/29/17.
 */

public class ShowCreatedMoodBoardActivity extends AppCompatActivity {

    public ImageView createdMoodBoardImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_created_mood_board);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createdMoodBoardImage = findViewById(R.id.created_mood_board);
        createdMoodBoardImage.setImageBitmap(CreateMoodBoardActivity.createdMoodBoardBitmap);

        findViewById(R.id.save_mood_board_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(
//                        ShowCreatedMoodBoardActivity.this,
//                        "Click this button will save the mood board",
//                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowCreatedMoodBoardActivity.this,
                        SetMoodBoardInfoActivity.class));
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
}